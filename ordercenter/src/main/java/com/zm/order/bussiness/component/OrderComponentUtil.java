package com.zm.order.bussiness.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.PayFeignClient;
import com.zm.order.feignclient.model.PayModel;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.PostFeeDTO;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.Tax;
import com.zm.order.pojo.bo.TaxFeeBO;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.JSONUtil;

@Component
public class OrderComponentUtil {

	@Resource
	OrderService orderService;

	@Resource
	OrderMapper orderMapper;

	@Resource
	PayFeignClient payFeignClient;

	/**
	 * @fun 获取运费
	 * @param orderInfo
	 * @param amount
	 * @param weight
	 * @return
	 */
	public Double getPostFee(OrderInfo orderInfo, Double amount, Integer weight) {

		String province = orderInfo.getOrderDetail().getReceiveProvince();
		PostFeeDTO post = new PostFeeDTO(amount, province, weight, 2, orderInfo.getSupplierId());
		List<PostFeeDTO> postFeeList = new ArrayList<PostFeeDTO>();
		postFeeList.add(post);
		return orderService.getPostFee(postFeeList).get(0).getPostFee();

	}

	/**
	 * @fun 获取税费
	 * @param map
	 * @param unDiscountAmount
	 * @param postFee
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public TaxFeeBO getTaxFee(Map<String, Double> map, Double unDiscountAmount, Double postFee, ResultModel result)
			throws Exception {
		Double taxFee = 0.0;// 总税费
		Double totalExciseTax = 0.0;// 消费税
		Double totalIncremTax = 0.0;// 增值税
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			Tax tax = JSONUtil.parse(entry.getKey(), Tax.class);
			Double fee = entry.getValue();
			Double subPostFee = CalculationUtils.mul(CalculationUtils.div(fee, unDiscountAmount, 2), postFee);
			if (tax.getExciseTax() != null) {
				if (tax.getExciseTax() >= 1 || tax.getIncrementTax() >= 1) {
					result.setSuccess(false);
					result.setErrorCode(ErrorCodeEnum.TAX_SET_ERROR.getErrorCode());
					result.setErrorMsg(ErrorCodeEnum.TAX_SET_ERROR.getErrorMsg());
					return null;
				}
				Double temp = CalculationUtils.div(CalculationUtils.add(fee, subPostFee),
						CalculationUtils.sub(1.0, tax.getExciseTax()), 2);
				Double exciseTax = CalculationUtils.mul(temp, tax.getExciseTax());
				totalExciseTax += CalculationUtils.mul(exciseTax, 0.7);
				Double incremTax = CalculationUtils.mul(CalculationUtils.add(fee, subPostFee, exciseTax),
						tax.getIncrementTax());
				totalIncremTax += CalculationUtils.mul(incremTax, 0.7);
			} else {
				totalIncremTax += CalculationUtils
						.mul(CalculationUtils.mul(CalculationUtils.add(fee, subPostFee), tax.getIncrementTax()), 0.7);
			}
		}
		taxFee = CalculationUtils.add(totalExciseTax, totalIncremTax);

		return new TaxFeeBO(totalExciseTax, totalIncremTax, taxFee);
	}

	/**
	 * @fun 保存订单信息
	 * @param orderInfo
	 */
	public void saveOrder(OrderInfo orderInfo) {
		orderMapper.saveOrder(orderInfo);
		orderInfo.getOrderDetail().setOrderId(orderInfo.getOrderId());
		orderMapper.saveOrderDetail(orderInfo.getOrderDetail());
		for (OrderGoods goods : orderInfo.getOrderGoodsList()) {
			goods.setOrderId(orderInfo.getOrderId());
		}
		orderMapper.saveOrderGoods(orderInfo.getOrderGoodsList());
	}

	/**
	 * @fun 判断费用前后是否一致
	 * @param amount
	 * @param taxFee
	 * @param postFee
	 * @param payMent
	 * @return
	 */
	public boolean judgeAmount(Double amount, TaxFeeBO taxFee, Double postFee, Double payMent) {
		amount = CalculationUtils.add(amount, taxFee.getTaxFee(), postFee);
		amount = CalculationUtils.round(2, amount);
		int totalAmount = (int) CalculationUtils.mul(amount, 100);
		int localAmount = (int) (payMent * 100);
		if (totalAmount - localAmount > 5 || totalAmount - localAmount < -5) {// 价格区间定义在正负5分
			return false;
		}
		return true;
	}

	/**
	 * @验证参数有效性
	 * @param info
	 * @param payType
	 * @param type
	 * @param result
	 * @param openId
	 */
	public void paramValidate(OrderInfo info, String payType, String type, ResultModel result, String openId) {
		if (info == null) {
			result.setErrorMsg("订单不能为空");
			result.setSuccess(false);
			return;
		}
		if (!info.check()) {
			result.setErrorMsg("订单参数不全");
			result.setSuccess(false);
			return;
		}
		// TODO 天天仓规则
		if (info.getSupplierId() == 1) {
			int totalQuantity = 0;
			for (OrderGoods goods : info.getOrderGoodsList()) {
				totalQuantity += goods.getItemQuantity();
			}
			if (totalQuantity > 10) {
				result.setSuccess(false);
				result.setErrorMsg("保税TT仓的商品订单数量不能超过10个");
				return;
			}
		}

		if (info.getOrderFlag().equals(Constants.GENERAL_TRADE)
				&& info.getOrderDetail().getPayment() < Constants.GENERAL_TRADE_FEE) {
			result.setSuccess(false);
			result.setErrorMsg("一般贸易订单起订额需要大于" + Constants.GENERAL_TRADE_FEE + "元");
			return;
		}

		if (Constants.WX_PAY.equals(payType)) {

			if (Constants.JSAPI.equals(type)) {
				if (openId == null || "".equals(openId)) {
					result.setSuccess(false);
					result.setErrorMsg("请使用微信授权登录");
					return;
				}
			}
		}
	}

	/**
	 * @fun 补全订单信息
	 * @param info
	 * @param postFee
	 * @param weight
	 * @param amount
	 * @param taxFee
	 * @param disAmount
	 */
	public void renderOrderInfo(OrderInfo info, Double postFee, Integer weight, Double amount, TaxFeeBO taxFee,
			Double disAmount) {
		info.setWeight(weight);
		info.setStatus(0);
		info.getOrderDetail().setPostFee(postFee);
		info.getOrderDetail().setPayment(amount);
		info.getOrderDetail().setTaxFee(taxFee.getTaxFee());
		info.getOrderDetail().setIncrementTax(taxFee.getIncremTax());
		info.getOrderDetail().setExciseTax(taxFee.getExciseTax());
		info.getOrderDetail().setTariffTax(0.0);
		info.getOrderDetail().setDisAmount(disAmount);
	}

	/**
	 * @fun 调用第三方支付
	 * @param payType
	 * @param type
	 * @param req
	 * @param result
	 * @param openId
	 * @param orderId
	 * @param centerId
	 * @param detail
	 * @param totalAmount
	 * @throws Exception
	 */
	public void getPayInfo(String payType, String type, HttpServletRequest req, ResultModel result, String openId,
			String orderId, Integer centerId, StringBuilder detail, int totalAmount) throws Exception {
		PayModel payModel = new PayModel();
		payModel.setBody("购物订单");
		payModel.setOrderId(orderId);
		payModel.setTotalAmount(totalAmount + "");
		String detailStr = detail.toString().substring(0, detail.toString().length() - 1);
		if (detailStr.length() > 100) {// 支付宝描述过长会报错
			detailStr = detailStr.substring(0, 100) + "...";
		}
		payModel.setDetail(detailStr);

		if (Constants.WX_PAY.equals(payType)) {
			payModel.setOpenId(openId);
			payModel.setIP(req.getRemoteAddr());
			Map<String, String> paymap = payFeignClient.wxPay(centerId, type, payModel);
			result.setObj(paymap);
		} else if (Constants.ALI_PAY.equals(payType)) {
			result.setObj(payFeignClient.aliPay(centerId, type, payModel));
		} else if (Constants.UNION_PAY.equals(payType)) {
			result.setObj(payFeignClient.unionpay(centerId, type, payModel));
		} else {
			result.setSuccess(false);
			result.setErrorMsg("请指定正确的支付方式");
		}
	}

}
