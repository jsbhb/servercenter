package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.order.bussiness.component.OpenInterfaceUtil;
import com.zm.order.bussiness.component.ShareProfitComponent;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.dao.OrderOpenInterfaceMapper;
import com.zm.order.bussiness.service.OrderOpenInterfaceService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.ButtJointOrder;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderStatus;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.UserInfo;
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

	@Override
	public ResultModel addOrder(String order) {

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
		// 判断库存和购买数量
		resultModel = goodsFeignClient.delButtjoinOrderStock(Constants.FIRST_VERSION, list, orderInfo.getSupplierId(),
				orderInfo.getOrderFlag());
		if (!resultModel.isSuccess()) {
			return resultModel;
		}
		orderInfo.setUserId(Integer.valueOf(resultModel.getObj().toString()));
		orderInfo.setStatus(Constants.ORDER_PAY);
		orderMapper.saveOrder(orderInfo);
		orderInfo.getOrderDetail().setOrderId(orderInfo.getOrderId());

		orderMapper.saveOrderDetail(orderInfo.getOrderDetail());

		for (OrderGoods goods : orderInfo.getOrderGoodsList()) {
			goods.setOrderId(orderInfo.getOrderId());
		}
		orderMapper.saveOrderGoods(orderInfo.getOrderGoodsList());

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
		Map<String, String> param = null;
		try {
			param = JSONUtil.parse(data, Map.class);
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

		orderOpenInterfaceMapper.updateOrderPayCustom(orderId);
		return new ResultModel(true, null);
	}

}
