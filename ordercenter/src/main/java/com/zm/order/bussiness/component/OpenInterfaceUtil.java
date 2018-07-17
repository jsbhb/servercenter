package com.zm.order.bussiness.component;

import java.util.ArrayList;
import java.util.List;

import com.zm.order.constants.Constants;
import com.zm.order.pojo.ButtJointOrder;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.UserDetail;
import com.zm.order.pojo.UserInfo;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.DateUtils;
import com.zm.order.utils.RegularUtil;

public class OpenInterfaceUtil {

	public static UserInfo packUser(ButtJointOrder orderInfo) {
		UserInfo user = new UserInfo();
		user.setPhone(orderInfo.getPhone());
		user.setPhoneValidate(1);
		user.setCenterId(orderInfo.getCenterId());
		UserDetail detail = new UserDetail();
		detail.setIdNum(orderInfo.getNumId());
		detail.setName(orderInfo.getName());
		user.setUserDetail(detail);
		return user;
	}

	@SuppressWarnings("serial")
	private static final List<Integer> orderType = new ArrayList<Integer>() {
		{
			add(0);// 跨境
			add(2);// 一般贸易
		}
	};
	@SuppressWarnings("serial")
	private static final List<Integer> payType = new ArrayList<Integer>() {
		{
			add(1);// 微信
			add(2);// 支付宝
		}
	};
	@SuppressWarnings("serial")
	private static final List<Integer> orderSource = new ArrayList<Integer>() {
		{
			add(0);// PC
			add(1);// 手机
		}
	};

	public static ResultModel paramValidate(ButtJointOrder orderInfo) {
		if (!orderInfo.validate() || !orderInfo.getOrderDetail().validate()) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		if (!DateUtils.judgeDateFormat(orderInfo.getOrderDetail().getPayTime(), "yyyy-MM-dd HH:mm:ss")) {
			return new ResultModel(false, ErrorCodeEnum.TIME_FORMATE_ERROR.getErrorCode(),
					ErrorCodeEnum.TIME_FORMATE_ERROR.getErrorMsg());
		}
		if (Constants.CNCOOPBUY != orderInfo.getCenterId() || Constants.OPEN_INTERFACE_TYPE != orderInfo.getCreateType()
				|| Constants.EXPRESS != orderInfo.getExpressType() || 0 != orderInfo.getOrderDetail().getDisAmount()
				|| !orderType.contains(orderInfo.getOrderFlag())
				|| !payType.contains(orderInfo.getOrderDetail().getPayType())
				|| !orderSource.contains(orderInfo.getOrderSource())) {
			return new ResultModel(false, ErrorCodeEnum.PARAM_ERROR.getErrorCode(),
					ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
		}

		if (orderInfo.getOrderDetail() == null) {
			return new ResultModel(false, ErrorCodeEnum.ORDER_MISS_DETAIL.getErrorCode(),
					ErrorCodeEnum.ORDER_MISS_DETAIL.getErrorMsg());
		}
		Double taxFee = CalculationUtils.add(orderInfo.getOrderDetail().getExciseTax(),
				orderInfo.getOrderDetail().getIncrementTax(), orderInfo.getOrderDetail().getTariffTax());
		if (CalculationUtils.sub(taxFee, orderInfo.getOrderDetail().getTaxFee()) > 3
				|| CalculationUtils.sub(taxFee, orderInfo.getOrderDetail().getTaxFee()) < -3) {
			return new ResultModel(false, ErrorCodeEnum.TAX_ERROR.getErrorCode(),
					ErrorCodeEnum.TAX_ERROR.getErrorMsg());
		}
		if (orderInfo.getOrderGoodsList() == null) {
			return new ResultModel(false, ErrorCodeEnum.ORDER_MISS_GOODS.getErrorCode(),
					ErrorCodeEnum.ORDER_MISS_GOODS.getErrorMsg());
		}
		if (orderInfo.getOrderFlag() == 0 && orderInfo.getOrderDetail().getPayment() > 2000) {
			return new ResultModel(false, ErrorCodeEnum.OUT_OF_PRICE.getErrorCode(),
					ErrorCodeEnum.OUT_OF_PRICE.getErrorMsg());
		}
		if (orderInfo.getOrderFlag() == 2 && orderInfo.getOrderDetail().getPayment() < 800) {
			return new ResultModel(false, ErrorCodeEnum.OUT_OF_PRICE.getErrorCode(),
					ErrorCodeEnum.OUT_OF_PRICE.getErrorMsg());
		}
		if (!RegularUtil.isPhone(orderInfo.getPhone())) {
			return new ResultModel(false, ErrorCodeEnum.BUYER_PHONE_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.BUYER_PHONE_VALIDATE_ERROR.getErrorMsg());
		}
		if (!RegularUtil.isIdentify(orderInfo.getNumId())) {
			return new ResultModel(false, ErrorCodeEnum.IDENTIFY_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.IDENTIFY_VALIDATE_ERROR.getErrorMsg());
		}
		if (!RegularUtil.isPhone(orderInfo.getOrderDetail().getReceivePhone())) {
			return new ResultModel(false, ErrorCodeEnum.RECEIVE_PHONE_ERROR.getErrorCode(),
					ErrorCodeEnum.RECEIVE_PHONE_ERROR.getErrorMsg());
		}
		Double amount = 0.0;
		for (OrderGoods goods : orderInfo.getOrderGoodsList()) {
			if (!goods.validate()) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			amount = CalculationUtils.add(amount, CalculationUtils.mul(goods.getItemPrice(), goods.getItemQuantity()));
		}
		amount = CalculationUtils.add(amount, orderInfo.getOrderDetail().getTaxFee(),
				orderInfo.getOrderDetail().getPostFee());
		if (CalculationUtils.sub(orderInfo.getOrderDetail().getPayment(), amount) > 3
				|| CalculationUtils.sub(orderInfo.getOrderDetail().getPayment(), amount) < -3) {
			return new ResultModel(false, ErrorCodeEnum.PAYMENT_ERROR.getErrorCode(),
					ErrorCodeEnum.PAYMENT_ERROR.getErrorMsg());
		}

		return null;
	}
}
