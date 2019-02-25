package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zm.order.bussiness.component.OpenInterfaceUtil;
import com.zm.order.bussiness.component.OrderComponentUtil;
import com.zm.order.bussiness.component.ShareProfitComponent;
import com.zm.order.bussiness.component.expressrule.ExpressRuleStrategy;
import com.zm.order.bussiness.convertor.OrderConvertUtil;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.dao.OrderOpenInterfaceMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.bussiness.service.OrderOpenInterfaceService;
import com.zm.order.constants.Constants;
import com.zm.order.exception.ParameterException;
import com.zm.order.exception.RuleCheckException;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.ButtJointOrder;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.OrderStatus;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.UserInfo;
import com.zm.order.pojo.bo.DealOrderDataBO;
import com.zm.order.pojo.bo.ExpressRule;
import com.zm.order.pojo.bo.OrderGoodsCompleteBO;
import com.zm.order.pojo.bo.TaxFeeBO;
import com.zm.order.utils.JSONUtil;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class OrderOpenInterfaceServiceImpl implements OrderOpenInterfaceService {

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	OrderMapper orderMapper;

	@Resource
	OrderOpenInterfaceMapper orderOpenInterfaceMapper;

	@Resource
	ShareProfitComponent shareProfitComponent;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	OrderComponentUtil orderComponentUtil;

	@Resource
	CacheAbstractService cacheAbstractService;

	@Override
	public ResultModel addOrder(String order) throws Exception {

		ButtJointOrder orderInfo = null;
		try {
			orderInfo = JSONUtil.parse(order, ButtJointOrder.class);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
					ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
		}

		List<String> orderIds = orderMapper.isExist(orderInfo);
		if (orderIds.size() > 0) {
			return new ResultModel(false, ErrorCodeEnum.REPEAT_ERROR.getErrorCode(),
					ErrorCodeEnum.REPEAT_ERROR.getErrorMsg());
		}

		// 验证参数有效性
		ResultModel resultModel = OpenInterfaceUtil.paramValidate(orderInfo);
		if (resultModel != null) {
			return resultModel;
		}

		// 判断对应仓库的模板规则
		List<ExpressRule> ruleList = orderMapper.listExpressRule(orderInfo.getSupplierId());
		if (ruleList != null && ruleList.size() > 0) {
			try {
				ExpressRuleStrategy strategy = new ExpressRuleStrategy(ruleList);
				strategy.judgeExpressRule(orderInfo);
			} catch (ParameterException e) {
				LogUtil.writeErrorLog("供应商模板配置出错：ID为" + orderInfo.getSupplierId());
				return new ResultModel(false, ErrorCodeEnum.PARAM_ERROR.getErrorCode(),
						ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
			} catch (RuleCheckException e) {
				return new ResultModel(false, ErrorCodeEnum.OUT_OF_PRICE.getErrorCode(), e.getMessage());
			}
		}

		// 注册用户，获取userId
		UserInfo user = OpenInterfaceUtil.packUser(orderInfo);
		resultModel = userFeignClient.registerUser(Constants.FIRST_VERSION, user, "erp");
		if (!resultModel.isSuccess()) {
			return resultModel;
		}
		orderInfo.setUserId(Integer.valueOf(resultModel.getObj().toString()));

		// 判断费用
		DealOrderDataBO bo = OrderConvertUtil.convertToDealOrderDataBO(orderInfo, null, false, true);
		resultModel = orderComponentUtil.doOrderGoodsDeal(bo, orderInfo.getCreateType());
		if (!resultModel.isSuccess()) {
			return resultModel;
		}
		// 处理订单
		// 邮费和税费初始值
		Double postFee = 0.0;
		TaxFeeBO taxFee = new TaxFeeBO();// 税费对象
		double amount = orderInfo.getOrderGoodsList().stream().mapToDouble(OrderGoods::getItemPrice).sum();
		List<OrderGoodsCompleteBO> boList = JSONUtil.parse(JSONUtil.toJson(resultModel.getObj()),
				new TypeReference<List<OrderGoodsCompleteBO>>() {
				});
		// 获取税费
		taxFee = orderComponentUtil.getTaxFee(boList, amount, postFee);
		// 判断价格是否一致
		if (!orderComponentUtil.judgeAmount(amount, taxFee, postFee, orderInfo)) {
			return new ResultModel(false, ErrorCodeEnum.PAYMENT_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.PAYMENT_VALIDATE_ERROR.getErrorMsg());
		}

		// 拆单
		Map<Integer, List<OrderGoodsCompleteBO>> map = boList.stream()
				.collect(Collectors.groupingBy(OrderGoodsCompleteBO::getSupplierId));
		List<OrderInfo> infoList = orderComponentUtil.splitOrderInfo(orderInfo, map);
		// 封装该订单商品的每一级的返佣比例
		for (OrderInfo info : infoList) {
			orderComponentUtil.packGoodsRebateByGrade(info);
			if (info.getOrderFlag().equals(Constants.GENERAL_TRADE)) {// 一般贸易订单状态已付款
				info.setStatus(Constants.ORDER_PAY);
			} else if (info.getOrderFlag().equals(Constants.O2O_ORDER_TYPE)) {// 跨境订单状态为支付单报关
				info.setStatus(Constants.ORDER_PAY_CUSTOMS);
			}
		}
		// 保存订单
		orderComponentUtil.saveOrder(infoList);

		shareProfitComponent.calShareProfitStayToAccount(orderInfo.getOrderId());// 计算资金池和返佣
		return new ResultModel(true, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel getOrderStatus(String json) {

		Map<String, String> param = null;
		try {
			param = JSONUtil.parse(json, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
					ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
		}
		String orderId = param.get("orderId");
		if (orderId == null) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		OrderStatus orderStatus = orderOpenInterfaceMapper.getOrderStatus(orderId);
		if (orderStatus == null) {
			return new ResultModel(false, ErrorCodeEnum.NO_DATA_ERROR.getErrorCode(),
					ErrorCodeEnum.NO_DATA_ERROR.getErrorMsg());
		}
		if (!Constants.ORDER_EXCEPTION.equals(orderStatus.getStatus())) {
			orderStatus.setAbnormalMsg(null);
		}
		return new ResultModel(true, orderStatus);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel payCustom(String data) {
		Map<String, Object> param = null;
		try {
			param = JSONUtil.parse(data, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
					ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
		}
		String orderId = param.get("orderId") == null ? null : param.get("orderId").toString();
		if (orderId == null) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		List<String> orderIds = new ArrayList<String>();
		orderIds.add(orderId);
		param.put("list", orderIds);
		param.put("status", 2);
		orderOpenInterfaceMapper.updateOrderStatus(param);
		return new ResultModel(true, null);
	}

}
