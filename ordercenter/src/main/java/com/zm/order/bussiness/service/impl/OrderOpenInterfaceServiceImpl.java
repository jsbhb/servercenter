package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.order.bussiness.component.OpenInterfaceUtil;
import com.zm.order.bussiness.component.ShareProfitComponent;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.dao.OrderOpenInterfaceMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.bussiness.service.OrderOpenInterfaceService;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.ButtJointOrder;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderStatus;
import com.zm.order.pojo.PostFeeDTO;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.Tax;
import com.zm.order.pojo.UserInfo;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.DateUtils;
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
	OrderService orderService;
	
	@Resource
	CacheAbstractService cacheAbstractService;

	@SuppressWarnings("unchecked")
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

		Integer status = orderMapper.getOrderStatusByOrderId(orderInfo.getOrderId());
		if (status != null) {
			return new ResultModel(false, ErrorCodeEnum.REPEAT_ERROR.getErrorCode(),
					ErrorCodeEnum.REPEAT_ERROR.getErrorMsg());
		}

		// 验证参数有效性
		ResultModel resultModel = OpenInterfaceUtil.paramValidate(orderInfo);
		if (resultModel != null) {
			return resultModel;
		}
		List<OrderBussinessModel> list = new ArrayList<OrderBussinessModel>();
		OrderBussinessModel model = null;
		for (OrderGoods goods : orderInfo.getOrderGoodsList()) {
			model = new OrderBussinessModel();
			model.setOrderId(orderInfo.getOrderId());
			model.setItemCode(goods.getItemCode());
			model.setItemId(goods.getItemId());
			model.setQuantity(goods.getItemQuantity());
			model.setSku(goods.getSku());
			list.add(model);
		}

		UserInfo user = OpenInterfaceUtil.packUser(orderInfo);
		resultModel = userFeignClient.registerUser(Constants.FIRST_VERSION, user, "erp");
		if (!resultModel.isSuccess()) {
			return resultModel;
		}
		orderInfo.setUserId(Integer.valueOf(resultModel.getObj().toString()));
		// 判断库存和购买数量
		resultModel = goodsFeignClient.getPriceAndDelStock(Constants.FIRST_VERSION, list, orderInfo.getSupplierId(),
				false, 2, orderInfo.getOrderFlag(), orderInfo.getCouponIds(), orderInfo.getUserId());
		if (!resultModel.isSuccess()) {
			return resultModel;
		}
		Map<String, Object> priceAndWeightMap = null;
		Double amount = 0.0;
		priceAndWeightMap = (Map<String, Object>) resultModel.getObj();
		amount = (Double) priceAndWeightMap.get("totalAmount");

		// 邮费和税费初始值
		Double postFee = 0.0;
		Double taxFee = 0.0;
		Double totalExciseTax = 0.0;
		Double totalIncremTax = 0.0;
		Double unDiscountAmount = 0.0;
		Integer weight = (Integer) priceAndWeightMap.get("weight");

		// 获取包邮包税
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> tempMap = hashOperations.entries(Constants.POST_TAX + orderInfo.getSupplierId());
		boolean freePost = false;
		boolean freeTax = false;
		if (tempMap != null) {
			freePost = Constants.FREE_POST.equals(tempMap.get("post"))
					|| Constants.ARRIVE_POST.equals(tempMap.get("post")) ? true : false;
			freeTax = Constants.FREE_TAX.equals(tempMap.get("tax")) ? true : false;
		}
		if (!freePost) {
			// 计算邮费(自提不算邮费)
			String province = orderInfo.getOrderDetail().getReceiveProvince();
			PostFeeDTO post = new PostFeeDTO(amount, province, weight, 2, orderInfo.getSupplierId());
			List<PostFeeDTO> postFeeList = new ArrayList<PostFeeDTO>();
			postFeeList.add(post);
			postFee = orderService.getPostFee(postFeeList).get(0).getPostFee();
		}
		if (!freeTax) {
			// 计算税费
			if (Constants.O2O_ORDER_TYPE.equals(orderInfo.getOrderFlag())) {
				Map<String, Double> map = (Map<String, Double>) priceAndWeightMap.get("tax");

				for (Map.Entry<String, Double> entry : map.entrySet()) {
					unDiscountAmount += entry.getValue();
				}
				for (Map.Entry<String, Double> entry : map.entrySet()) {
					Tax tax = JSONUtil.parse(entry.getKey(), Tax.class);
					Double fee = entry.getValue();
					Double subPostFee = CalculationUtils.mul(CalculationUtils.div(fee, unDiscountAmount, 2), postFee);
					if (tax.getExciseTax() != null) {
						if (tax.getExciseTax() >= 1 || tax.getIncrementTax() >= 1) {
							return new ResultModel(false, ErrorCodeEnum.TAX_SET_ERROR.getErrorCode(),
									ErrorCodeEnum.TAX_SET_ERROR.getErrorMsg());
						}
						Double temp = CalculationUtils.div(CalculationUtils.add(fee, subPostFee),
								CalculationUtils.sub(1.0, tax.getExciseTax()), 2);
						Double exciseTax = CalculationUtils.mul(temp, tax.getExciseTax());
						totalExciseTax += CalculationUtils.mul(exciseTax, 0.7);
						Double incremTax = CalculationUtils.mul(CalculationUtils.add(fee, subPostFee, exciseTax),
								tax.getIncrementTax());
						totalIncremTax += CalculationUtils.mul(incremTax, 0.7);
					} else {
						totalIncremTax += CalculationUtils.mul(
								CalculationUtils.mul(CalculationUtils.add(fee, subPostFee), tax.getIncrementTax()),
								0.7);
					}
				}
				taxFee = CalculationUtils.add(totalExciseTax, totalIncremTax);
			}
		}

		amount = CalculationUtils.add(amount, taxFee, postFee);
		amount = CalculationUtils.round(2, amount);
		int totalAmount = (int) CalculationUtils.mul(amount, 100);
		int localAmount = (int) (orderInfo.getOrderDetail().getPayment() * 100);
		if (totalAmount - localAmount > 5 || totalAmount - localAmount < -5) {// 价格区间定义在正负5分
			return new ResultModel(false, ErrorCodeEnum.PAYMENT_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.PAYMENT_VALIDATE_ERROR.getErrorMsg());
		}

		if(orderInfo.getOrderFlag().equals(Constants.GENERAL_TRADE)){//一般贸易订单状态已付款
			orderInfo.setStatus(Constants.ORDER_PAY);
		} else if(orderInfo.getOrderFlag().equals(Constants.O2O_ORDER_TYPE)){//跨境订单状态为支付单报关
			orderInfo.setStatus(Constants.ORDER_PAY_CUSTOMS);
		}

		resultModel = goodsFeignClient.calStock(Constants.FIRST_VERSION, list, orderInfo.getSupplierId(),
				orderInfo.getOrderFlag());
		if (!resultModel.isSuccess()) {
			return new ResultModel(false, ErrorCodeEnum.OUT_OF_STOCK.getErrorCode(),
					resultModel.getErrorMsg() + ErrorCodeEnum.OUT_OF_STOCK.getErrorMsg());
		}

		orderMapper.saveOrder(orderInfo);
		orderInfo.getOrderDetail().setOrderId(orderInfo.getOrderId());

		orderMapper.saveOrderDetail(orderInfo.getOrderDetail());

		for (OrderGoods goods : orderInfo.getOrderGoodsList()) {
			goods.setOrderId(orderInfo.getOrderId());
		}
		orderMapper.saveOrderGoods(orderInfo.getOrderGoodsList());

		// 增加缓存订单数量
		cacheAbstractService.addOrderCountCache(orderInfo.getShopId(), Constants.ORDER_STATISTICS_DAY, "produce");
		// 增加月订单数
		String time = DateUtils.getTimeString("yyyyMM");
		cacheAbstractService.addOrderCountCache(orderInfo.getShopId(), Constants.ORDER_STATISTICS_MONTH, time);

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
		param.put("status",2);
		orderOpenInterfaceMapper.updateOrderStatus(param);
		return new ResultModel(true, null);
	}

}
