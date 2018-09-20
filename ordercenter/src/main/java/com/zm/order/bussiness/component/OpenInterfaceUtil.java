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
		// 判断订单必要参数
		if (!orderInfo.validate() || !orderInfo.getOrderDetail().validate()) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		// 判断订单时间格式
		if (!DateUtils.judgeDateFormat(orderInfo.getOrderDetail().getPayTime(), "yyyy-MM-dd HH:mm:ss")) {
			return new ResultModel(false, ErrorCodeEnum.TIME_FORMATE_ERROR.getErrorCode(),
					ErrorCodeEnum.TIME_FORMATE_ERROR.getErrorMsg());
		}
		// 判断订单标志是否合法
		if (Constants.CNCOOPBUY != orderInfo.getCenterId() || Constants.OPEN_INTERFACE_TYPE != orderInfo.getCreateType()
				|| Constants.EXPRESS != orderInfo.getExpressType() || 0 != orderInfo.getOrderDetail().getDisAmount()
				|| !orderType.contains(orderInfo.getOrderFlag())
				|| !payType.contains(orderInfo.getOrderDetail().getPayType())
				|| !orderSource.contains(orderInfo.getOrderSource())) {
			return new ResultModel(false, ErrorCodeEnum.PARAM_ERROR.getErrorCode(),
					ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
		}

		// 判断订单是否存在orderDetail
		if (orderInfo.getOrderDetail() == null) {
			return new ResultModel(false, ErrorCodeEnum.ORDER_MISS_DETAIL.getErrorCode(),
					ErrorCodeEnum.ORDER_MISS_DETAIL.getErrorMsg());
		}
		// 判断增值税消费税关税和是否等于总税费
		Double taxFee = CalculationUtils.add(orderInfo.getOrderDetail().getExciseTax(),
				orderInfo.getOrderDetail().getIncrementTax(), orderInfo.getOrderDetail().getTariffTax());
		if (CalculationUtils.sub(taxFee, orderInfo.getOrderDetail().getTaxFee()) > CalculationUtils
				.div(Constants.DEVIATION, 100)
				|| CalculationUtils.sub(taxFee,
						orderInfo.getOrderDetail().getTaxFee()) < -CalculationUtils.div(Constants.DEVIATION, 100)) {
			return new ResultModel(false, ErrorCodeEnum.TAX_ERROR.getErrorCode(),
					ErrorCodeEnum.TAX_ERROR.getErrorMsg());
		}
		// 判断商品列表是否存在
		if (orderInfo.getOrderGoodsList() == null || orderInfo.getOrderGoodsList().size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.ORDER_MISS_GOODS.getErrorCode(),
					ErrorCodeEnum.ORDER_MISS_GOODS.getErrorMsg());
		}
		// 如果是跨境订单并且商品不只一个或者一个商品买了多个，则判断金额是否大于2000
		if (orderInfo.getOrderFlag() == Constants.O2O_ORDER_TYPE && (orderInfo.getOrderGoodsList().size() > 1
				|| orderInfo.getOrderGoodsList().get(0).getItemQuantity() > 1)) {
			if (orderInfo.getOrderDetail().getPayment() > 2000) {
				return new ResultModel(false, ErrorCodeEnum.OUT_OF_PRICE.getErrorCode(),
						ErrorCodeEnum.OUT_OF_PRICE.getErrorMsg());
			}
		}
		// 判断电话号码是否合法
		if (!RegularUtil.isPhone(orderInfo.getPhone())) {
			return new ResultModel(false, ErrorCodeEnum.BUYER_PHONE_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.BUYER_PHONE_VALIDATE_ERROR.getErrorMsg());
		}
		// 判断身份证号码是否合法
		if (!RegularUtil.isIdentify(orderInfo.getNumId())) {
			return new ResultModel(false, ErrorCodeEnum.IDENTIFY_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.IDENTIFY_VALIDATE_ERROR.getErrorMsg());
		}
		// 判断收货手机号是否合法
		if (!RegularUtil.isPhone(orderInfo.getOrderDetail().getReceivePhone())) {
			return new ResultModel(false, ErrorCodeEnum.RECEIVE_PHONE_ERROR.getErrorCode(),
					ErrorCodeEnum.RECEIVE_PHONE_ERROR.getErrorMsg());
		}
		// 判断订单商品参数是否齐全
		Double amount = 0.0;
		for (OrderGoods goods : orderInfo.getOrderGoodsList()) {
			if (!goods.validate()) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			amount = CalculationUtils.add(amount, CalculationUtils.mul(goods.getItemPrice(), goods.getItemQuantity()));
		}
		// 判断税费加商品金额是否等于支付金额
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
