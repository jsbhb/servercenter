package com.zm.order.bussiness.component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.order.bussiness.component.expressrule.ExpressRuleStrategy;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.component.CacheComponent;
import com.zm.order.constants.Constants;
import com.zm.order.exception.ParameterException;
import com.zm.order.exception.RuleCheckException;
import com.zm.order.feignclient.PayFeignClient;
import com.zm.order.feignclient.model.PayModel;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.PostFeeDTO;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.Tax;
import com.zm.order.pojo.bo.ExpressRule;
import com.zm.order.pojo.bo.GradeBO;
import com.zm.order.pojo.bo.TaxFeeBO;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.JSONUtil;
import com.zm.order.utils.TreeNodeUtil;

@Component
public class OrderComponentUtil {

	@Resource
	OrderService orderService;

	@Resource
	OrderMapper orderMapper;

	@Resource
	PayFeignClient payFeignClient;

	@Resource
	RedisTemplate<String, Object> template;

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
				totalExciseTax += CalculationUtils.mul(exciseTax, Constants.TAX_DISCOUNT);
				Double incremTax = CalculationUtils.mul(CalculationUtils.add(fee, subPostFee, exciseTax),
						tax.getIncrementTax());
				totalIncremTax += CalculationUtils.mul(incremTax, Constants.TAX_DISCOUNT);
			} else {
				totalIncremTax += CalculationUtils.mul(
						CalculationUtils.mul(CalculationUtils.add(fee, subPostFee), tax.getIncrementTax()),
						Constants.TAX_DISCOUNT);
			}
			LogUtil.writeLog("totalIncremTax=====" + totalIncremTax + ",fee====" + fee + ",postFee======" + postFee
					+ ",unDiscountAmount ======" + unDiscountAmount);
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
	public boolean judgeAmount(Double amount, TaxFeeBO taxFee, Double postFee, OrderInfo info) {
		amount = CalculationUtils.add(amount, taxFee.getTaxFee(), postFee);
		amount = CalculationUtils.round(2, amount);
		int totalAmount = (int) CalculationUtils.mul(amount, 100);
		int localAmount = (int) (info.getOrderDetail().getPayment() * 100);
		int rebateFee = (int) ((info.getOrderDetail().getRebateFee() == null ? 0 : info.getOrderDetail().getRebateFee())
				* 100);
		localAmount += rebateFee;
		LogUtil.writeLog("amount:"+amount+",totalAmount:"+totalAmount+",localAmount:"+localAmount+",rebateFee:"+rebateFee);
		if (totalAmount - localAmount > Constants.DEVIATION || totalAmount - localAmount < -Constants.DEVIATION) {// 价格区间定义在正负5分
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

		// 判断对应仓库的模板规则
		List<ExpressRule> ruleList = orderMapper.listExpressRule(info.getSupplierId());
		if (ruleList != null && ruleList.size() > 0) {
			try {
				ExpressRuleStrategy strategy = new ExpressRuleStrategy(ruleList);
				strategy.judgeExpressRule(info);
			} catch (ParameterException e) {
				result.setSuccess(false);
				result.setErrorMsg(e.getMessage());
				return;
			} catch (RuleCheckException e) {
				result.setSuccess(false);
				result.setErrorMsg(e.getMessage());
				return;
			}
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
		if (Constants.BACK_MANAGER_WEBSITE != (info.getOrderSource())) {// 如果不是后台订单
			if (info.getOrderDetail().getRebateFee() != null && info.getOrderDetail().getRebateFee() > 0) {
				result.setSuccess(false);
				result.setErrorMsg("权限错误，不能使用返佣支付");
				return;
			}
			if(Constants.REBATE_PAY.equals(payType)){
				result.setSuccess(false);
				result.setErrorMsg("权限错误，不能使用返佣支付");
				return;
			}
		} else {
			if(Constants.REBATE_PAY.equals(payType)){
				if(info.getOrderDetail().getPayment() > 0){
					result.setSuccess(false);
					result.setErrorMsg("还有余额需要支付，请选择其他支付方式");
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
	public void renderOrderInfo(OrderInfo info, Double postFee, Integer weight, TaxFeeBO taxFee, Double disAmount,
			boolean fromMall) {
		if (fromMall) {
			info.setWeight(weight);
			info.setStatus(0);
			info.getOrderDetail().setPostFee(postFee);
			info.getOrderDetail().setTaxFee(taxFee.getTaxFee());
			info.getOrderDetail().setIncrementTax(taxFee.getIncremTax());
			info.getOrderDetail().setExciseTax(taxFee.getExciseTax());
			info.getOrderDetail().setTariffTax(0.0);
			info.getOrderDetail().setDisAmount(disAmount);
		}
		// 封装该订单商品的每一级的返佣比例，返佣计算时可直接从这里拿
		packGoodsRebateByGrade(info);
	}

	private void packGoodsRebateByGrade(OrderInfo info) {
		List<OrderGoods> goodsList = info.getOrderGoodsList();
		Map<String, String> goodsRebate = null;
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		GradeBO grade = null;
		StringBuilder sb = null;// 记录每个商品的返佣，用json串
		for (OrderGoods goods : goodsList) {
			sb = new StringBuilder("{");
			// 获取该订单所有的上级,包括推手
			LinkedList<GradeBO> superNodeList = TreeNodeUtil.getSuperNode(CacheComponent.getInstance().getSet(),
					info.getShopId());
			goodsRebate = hashOperations.entries(Constants.GOODS_REBATE + goods.getItemId());
			if (goodsRebate == null || superNodeList == null || goodsRebate.size() == 0) {
				continue;
			}
			boolean isWelfareWebsite = Constants.WELFARE_WEBSITE == info.getOrderSource() ? true : false;
			double nextProportion = 0;// 下一级的返佣比例
			double welfareWebsiteRebate = 1;// 福利网站返佣比例，默认是拿全部返佣
			// 获取父级的时候已经按照先进先出排完续
			while (!superNodeList.isEmpty()) {
				grade = superNodeList.poll();
				// 如果是海外购，则不进行返佣
				if (grade.getId().equals(Constants.CNCOOPBUY)) {
					continue;
				}
				if (isWelfareWebsite) {// 如果是福利网站
					if (info.getShopId() == grade.getId()) {
						welfareWebsiteRebate = grade.getWelfareRebate() == null ? 0 : grade.getWelfareRebate();
					} else {
						welfareWebsiteRebate = 1;
					}
				}
				// 获取该类型的返佣的比例
				double tempProportion = Double.valueOf(goodsRebate.get(grade.getGradeType() + "") == null ? "0"
						: goodsRebate.get(grade.getGradeType() + ""));
				// 真实的返佣
				double proportion = CalculationUtils.mul(welfareWebsiteRebate,
						CalculationUtils.sub(tempProportion, nextProportion));
				nextProportion = proportion;// 记录下一级的返佣比例
				sb.append("\"" + grade.getId() + "\":" + "\"" + proportion + "\",");
			}
			if (sb.lastIndexOf(",") > 0) {
				goods.setRebate(sb.substring(0, sb.length() - 1) + "}");// 设置每个商品每个分级的返佣
			}
		}
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
			OrderInfo info, StringBuilder detail, int totalAmount) throws Exception {
		Double rebateFee = info.getOrderDetail().getRebateFee();
		if (rebateFee != null && rebateFee > 0) {// 如果有返佣抵扣，先进行扣减
			HashOperations<String, String, String> hashOperations = template.opsForHash();
			double balance = hashOperations.increment(Constants.GRADE_ORDER_REBATE + info.getShopId(), "alreadyCheck",
					CalculationUtils.sub(0, info.getOrderDetail().getRebateFee()));// 扣除返佣
			if (balance < 0) {
				hashOperations.increment(Constants.GRADE_ORDER_REBATE + info.getShopId(), "alreadyCheck",
						info.getOrderDetail().getRebateFee());// 增加返佣
				result.setSuccess(false);
				result.setErrorMsg("返佣使用金额超过可以使用金额");
				return;
			}
		}
		PayModel payModel = new PayModel();
		payModel.setBody("购物订单");
		payModel.setOrderId(info.getOrderId());
		payModel.setTotalAmount(totalAmount + "");
		String detailStr = detail.toString().substring(0, detail.toString().length() - 1);
		if (detailStr.length() > 60) {// 支付宝描述过长会报错
			detailStr = detailStr.substring(0, 60) + "...";
		}
		payModel.setDetail(detailStr);

		if (Constants.WX_PAY.equals(payType)) {
			payModel.setOpenId(openId);
			payModel.setIP(req.getRemoteAddr());
			try {
				Map<String, String> paymap = payFeignClient.wxPay(info.getCenterId(), type, payModel);
				result.setObj(paymap);
			} catch (Exception e) {
				exceptionHandle(result, info, rebateFee);
			}
			
		} else if (Constants.ALI_PAY.equals(payType)) {
			try {
				result.setObj(payFeignClient.aliPay(info.getCenterId(), type, payModel));
			} catch (Exception e) {
				exceptionHandle(result, info, rebateFee);
			}
		} else if (Constants.UNION_PAY.equals(payType)) {
			try {
				result.setObj(payFeignClient.unionpay(info.getCenterId(), type, payModel));
			} catch (Exception e) {
				exceptionHandle(result, info, rebateFee);
			}
		} else if (Constants.REBATE_PAY.equals(payType)) {
		} else {
			result.setSuccess(false);
			result.setErrorMsg("请指定正确的支付方式");
		}
	}

	private void exceptionHandle(ResultModel result, OrderInfo info, Double rebateFee) {
		if (rebateFee != null && rebateFee > 0) {
			template.opsForHash().increment(Constants.GRADE_ORDER_REBATE + info.getShopId(), "alreadyCheck", rebateFee);// 增加返佣
		}
		result.setSuccess(false);
		result.setErrorMsg("调用支付信息失败");
	}

}
