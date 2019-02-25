package com.zm.order.bussiness.component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.PayFeignClient;
import com.zm.order.feignclient.model.PayModel;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.PostFeeDTO;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.UserInfo;
import com.zm.order.pojo.bo.DealOrderDataBO;
import com.zm.order.pojo.bo.ExpressRule;
import com.zm.order.pojo.bo.GradeBO;
import com.zm.order.pojo.bo.OrderGoodsCompleteBO;
import com.zm.order.pojo.bo.TaxFeeBO;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.CommonUtils;
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

	@Resource
	GoodsFeignClient goodsFeignClient;

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
	public TaxFeeBO getTaxFee(List<OrderGoodsCompleteBO> boList, Double unDiscountAmount, Double postFee) {
		Double taxFee = 0.0;// 总税费
		Double totalExciseTax = 0.0;// 消费税
		Double totalIncremTax = 0.0;// 增值税
		for (OrderGoodsCompleteBO bo : boList) {
			Double subPostFee = CalculationUtils.mul(CalculationUtils.div(bo.getItemPrice(), unDiscountAmount, 2),
					postFee);
			if (bo.getExciseTax() != 0) {
				Double temp = CalculationUtils.div(CalculationUtils.add(bo.getItemPrice(), subPostFee),
						CalculationUtils.sub(1.0, bo.getExciseTax()), 2);
				Double exciseTax = CalculationUtils.mul(temp, bo.getExciseTax());
				totalExciseTax += CalculationUtils.mul(exciseTax, Constants.TAX_DISCOUNT);
				Double incremTax = CalculationUtils.mul(CalculationUtils.add(bo.getItemPrice(), subPostFee, exciseTax),
						bo.getIncrementTax());
				totalIncremTax += CalculationUtils.mul(incremTax, Constants.TAX_DISCOUNT);
			} else {
				totalIncremTax += CalculationUtils.mul(
						CalculationUtils.mul(CalculationUtils.add(bo.getItemPrice(), subPostFee), bo.getIncrementTax()),
						Constants.TAX_DISCOUNT);
			}
			LogUtil.writeLog("totalIncremTax=====" + totalIncremTax + ",fee====" + bo.getItemPrice() + ",postFee======"
					+ postFee + ",unDiscountAmount ======" + unDiscountAmount);
		}
		taxFee = CalculationUtils.add(totalExciseTax, totalIncremTax);

		return new TaxFeeBO(totalExciseTax, totalIncremTax, taxFee);
	}

	/**
	 * @fun 保存订单信息
	 * @param orderInfo
	 */
	public void saveOrder(List<OrderInfo> infoList) {
		orderMapper.saveOrder(infoList);
		List<OrderDetail> detailList = new ArrayList<>();
		List<OrderGoods> goodsList = new ArrayList<>();
		for(OrderInfo info : infoList){
			detailList.add(info.getOrderDetail());
			goodsList.addAll(info.getOrderGoodsList());
		}
		orderMapper.saveOrderDetail(detailList);
		orderMapper.saveOrderGoods(goodsList);
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
		LogUtil.writeLog("amount:" + amount + ",totalAmount:" + totalAmount + ",localAmount:" + localAmount);
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
		if (info.getCreateType() == Constants.BARGAIN_ORDER) {
			if (info.getOrderGoodsList().size() != 1) {
				result.setErrorMsg("砍价订单商品数量只能为1个");
				result.setSuccess(false);
				return;
			}
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
		if (Constants.YOP_PAY.equals(payType)) {
			if (Constants.O2O_ORDER_TYPE.equals(info.getOrderFlag())) {
				result.setSuccess(false);
				result.setErrorMsg("跨境订单暂不支持易宝支付");
				return;
			}
		}
		if (Constants.BACK_MANAGER_WEBSITE != (info.getOrderSource())) {// 如果不是后台订单
			if (info.getOrderDetail().getRebateFee() != null && info.getOrderDetail().getRebateFee() > 0) {
				result.setSuccess(false);
				result.setErrorMsg("权限错误，不能使用返佣支付");
				return;
			}
			if (Constants.REBATE_PAY.equals(payType)) {
				result.setSuccess(false);
				result.setErrorMsg("权限错误，不能使用返佣支付");
				return;
			}
		}
	}

	/**
	 * @fun 封装该订单商品的每一级的返佣比例，返佣计算时可直接从这里拿
	 * @param info
	 */
	public void packGoodsRebateByGrade(OrderInfo info) {
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
			goodsRebate = hashOperations.entries(Constants.GOODS_REBATE + goods.getSpecsTpId());
			if (goodsRebate == null || superNodeList == null || goodsRebate.size() == 0) {
				continue;
			}
			boolean isWelfareWebsite = Constants.WELFARE_WEBSITE == info.getOrderSource() ? true : false;
			double nextProportion = 0;// 下一级的返佣比例
			double welfareWebsiteRebate = 1;// 福利网站返佣比例，默认是拿全部返佣
			// 获取父级的时候已经按照先进先出排完续
			while (!superNodeList.isEmpty()) {
				grade = superNodeList.poll();
				// 判断是否需要返佣
				if (notNeedToRebate(grade, info)) {
					if (isBackManagerOrder(grade, info)) {// 如果是后台订单不计算本级返佣，但要记录本级的返佣比例
						nextProportion = Double.valueOf(goodsRebate.get(grade.getGradeType() + "") == null ? "0"
								: goodsRebate.get(grade.getGradeType() + ""));
					}
					continue;
				}
				if (isWelfareWebsite) {// 如果是福利网站
					if (info.getShopId().equals(grade.getId())) {
						welfareWebsiteRebate = grade.getWelfareRebate() == null ? 0 : grade.getWelfareRebate();
					} else {
						welfareWebsiteRebate = 1;
					}
				}
				// 获取该类型的返佣的比例
				double tempProportion = Double.valueOf(goodsRebate.get(grade.getGradeType() + "") == null ? "0"
						: goodsRebate.get(grade.getGradeType() + ""));
				double currentProprtion = CalculationUtils.sub(tempProportion, nextProportion);
				// 真实的返佣,记录的下一级返佣应该是原来的
				double proportion = CalculationUtils.mul(welfareWebsiteRebate, currentProprtion);
				nextProportion = currentProprtion;// 记录下一级的返佣比例
				sb.append("\"" + grade.getId() + "\":" + "\"" + proportion + "\",");
			}
			if (sb.lastIndexOf(",") > 0) {
				goods.setRebate(sb.substring(0, sb.length() - 1) + "}");// 设置每个商品每个分级的返佣
			}
		}
	}

	/**
	 * @fun 如果是后台订单
	 * @param grade
	 * @param info
	 * @return
	 */
	private boolean isBackManagerOrder(GradeBO grade, OrderInfo info) {
		if (grade.getId().equals(info.getShopId()) && Constants.BACK_MANAGER_WEBSITE.equals(info.getOrderSource())) {
			return true;
		}
		return false;
	}

	/**
	 * @fun 不需要返佣的返回ture
	 * @param grade
	 * @param info
	 * @return
	 */
	private boolean notNeedToRebate(GradeBO grade, OrderInfo info) {
		if (grade.getId().equals(Constants.CNCOOPBUY)) {// 如果是海外购，不需要返佣
			return true;
		}
		// 如果是后台订单，并且是本级的，不进行返佣
		if (grade.getId().equals(info.getShopId()) && Constants.BACK_MANAGER_WEBSITE.equals(info.getOrderSource())) {
			return true;
		}
		return false;
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
			OrderInfo info, StringBuilder detail, UserInfo user) throws Exception {
		Double rebateFee = info.getOrderDetail().getRebateFee();
		if (rebateFee != null && rebateFee > 0) {// 如果有返佣抵扣，先进行扣减
			HashOperations<String, String, String> hashOperations = template.opsForHash();
			double balance = hashOperations.increment(Constants.GRADE_ORDER_REBATE + info.getShopId(),
					Constants.ALREADY_CHECK, CalculationUtils.sub(0, info.getOrderDetail().getRebateFee()));// 扣除返佣
			if (balance < 0) {
				hashOperations.increment(Constants.GRADE_ORDER_REBATE + info.getShopId(), Constants.ALREADY_CHECK,
						info.getOrderDetail().getRebateFee());// 增加返佣
				result.setSuccess(false);
				result.setErrorMsg("返佣使用金额超过可以使用金额");
				return;
			}
			hashOperations.increment(Constants.GRADE_ORDER_REBATE + info.getShopId(), Constants.FROZEN_REBATE,
					rebateFee);// 增加冻结金额
		}
		Double needToPayAmount = CalculationUtils.sub(info.getOrderDetail().getPayment(),
				rebateFee == null ? 0 : rebateFee);

		int needToPayTotalAmount = (int) CalculationUtils.mul(needToPayAmount, 100);

		PayModel payModel = new PayModel();
		payModel.setBody("购物订单");
		payModel.setOrderId(info.getOrderId());
		payModel.setTotalAmount(needToPayTotalAmount + "");
		payModel.setPhone(user.getPhone());
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
		} else if (Constants.YOP_PAY.equals(payType)) {
			try {
				result.setObj(payFeignClient.yopPay(info.getCenterId(), payModel));
			} catch (Exception e) {
				exceptionHandle(result, info, rebateFee);
			}
		} else {
			exceptionHandle(result, info, rebateFee);
		}
	}

	private void exceptionHandle(ResultModel result, OrderInfo info, Double rebateFee) {
		if (rebateFee != null && rebateFee > 0) {
			template.opsForHash().increment(Constants.GRADE_ORDER_REBATE + info.getShopId(), Constants.ALREADY_CHECK,
					rebateFee);// 增加返佣
			template.opsForHash().increment(Constants.GRADE_ORDER_REBATE + info.getShopId(), Constants.FROZEN_REBATE,
					CalculationUtils.sub(0, rebateFee));// 减少冻结金额
		}
		result.setSuccess(false);
		result.setErrorMsg("调用支付信息失败");
	}

	/**
	 * @fun 根据不同的创建类型获取订单商品的金额税费等信息
	 * @param info
	 * @param list
	 * @param vip
	 * @param fx
	 * @return
	 */
	public ResultModel doOrderGoodsDeal(DealOrderDataBO bo, int type) {
		switch (type) {
		case Constants.TO_B_ORDER:
			return getNormal(bo);
		case Constants.NORMAL_ORDER:
			return getNormal(bo);
		case Constants.OPEN_INTERFACE_TYPE:
			return getNormal(bo);
		case Constants.BARGAIN_ORDER:
			return getBargain(bo);
		default:
			return new ResultModel(false, "创建类型有误");
		}
	}

	private ResultModel getNormal(DealOrderDataBO bo) {
		return goodsFeignClient.getPriceAndDelStock(Constants.FIRST_VERSION, bo);
	}

	private ResultModel getBargain(DealOrderDataBO bo) {
		if (bo.getModelList().size() > 1) {
			return new ResultModel(false, "砍价类订单，每个订单只能有一件商品");
		}
		int id;
		try {
			id = Integer.valueOf(bo.getCouponIds().split(",")[0]);
		} catch (Exception e) {
			return new ResultModel(false, "优惠列表参数有误");
		}
		return goodsFeignClient.getBargainGoodsInfo(Constants.FIRST_VERSION, bo, id);
	}

	/**
	 * @fun 进行拆单
	 * @param info
	 * @param map
	 * @return
	 */
	public List<OrderInfo> splitOrderInfo(OrderInfo info, Map<Integer, List<OrderGoodsCompleteBO>> map) {
		List<OrderInfo> infoList = new ArrayList<>();
		List<OrderGoods> goodsList = info.getOrderGoodsList();
		Map<String, OrderGoods> tmp = goodsList.stream()
				.collect(Collectors.toMap(OrderGoods::getSpecsTpId, o -> o));
		if (map.size() == 0) {// 说明需要手动处理
			info.setHandle(1);// 手动处理
			infoList.add(info);
		}
		if (map.size() == 1) {// 不需要拆单,补全信息
			for (Map.Entry<Integer, List<OrderGoodsCompleteBO>> entry : map.entrySet()) {
				buildOrderInfo(info, null, tmp, entry, info.getOrderId());
			}
			infoList.add(info);
		}
		if (map.size() > 1) {// 需要拆单，并补全信息
			String combinOrderId = CommonUtils.getOrderId(info.getOrderFlag() + "") + "_CB";
			int i = 1;
			for (Map.Entry<Integer, List<OrderGoodsCompleteBO>> entry : map.entrySet()) {
				if (i == 1) {
					buildOrderInfo(info, combinOrderId, tmp, entry, info.getOrderId());
					infoList.add(info);
				} else {
					OrderInfo infoTmp = info.clone();
					String orderId = CommonUtils.getOrderId(info.getOrderFlag() + "");
					buildOrderInfo(infoTmp, combinOrderId, tmp, entry, orderId);
					infoList.add(infoTmp);
				}
				i++;
			}
		}
		return infoList;
	}

	private void buildOrderInfo(OrderInfo info, String combinOrderId, Map<String, OrderGoods> tmp,
			Map.Entry<Integer, List<OrderGoodsCompleteBO>> entry, String orderId) {
		List<OrderGoods> goodsListTmp = new ArrayList<>();
		info.setSupplierId(entry.getKey());
		info.setOrderId(orderId);
		info.setWeight(entry.getValue().stream().mapToInt(bo -> bo.getWeight()).sum());
		info.setTdq(entry.getValue().size());
		info.setStatus(0);
		info.setCombinationId(combinOrderId);
		double amount = entry.getValue().stream().mapToDouble(bo -> bo.getItemPrice()).sum();
		TaxFeeBO taxFee = getTaxFee(entry.getValue(), amount, 0.0);
		info.getOrderDetail().setExciseTax(taxFee.getExciseTax());
		info.getOrderDetail().setIncrementTax(taxFee.getIncremTax());
		info.getOrderDetail().setPostFee(0.0);
		info.getOrderDetail().setTariffTax(0.0);
		info.getOrderDetail().setPayment(CalculationUtils.add(taxFee.getTaxFee(), amount));
		info.getOrderDetail().setOrderId(orderId);
		for (OrderGoodsCompleteBO b : entry.getValue()) {
			OrderGoods goods = tmp.get(b.getSpecsTpId());
			goods.setOrderId(orderId);
			goods.setCarton(b.getCarton());
			goods.setConversion(b.getConversion());
			goods.setItemCode(b.getItemCode());
			goods.setItemId(b.getItemId());
			goods.setSku(b.getSku());
			goods.setUnit(b.getUnit());
			goodsListTmp.add(goods);
		}
		info.setOrderGoodsList(goodsListTmp);
	}

	// ****************************临时加的逻辑，砍价天天仓商品特殊处理***********************
	private final String SPECIAL_ITEM_ID = "100001207";
	private final String[] specialArr = { "1011", "1008", "1013", "1012", "1014", "1010", "1009" };

	/**
	 * @fun 判断是否砍价特殊订单，砍价七天面膜的订单特殊处理
	 * @param info
	 * @return
	 */
	public boolean judgeIsBargainOrder(OrderInfo info) {
		if (info.getCreateType() == Constants.BARGAIN_ORDER) {
			if (SPECIAL_ITEM_ID.equals(info.getOrderGoodsList().get(0).getItemId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @fun 判断是否特殊
	 * @param info
	 * @return
	 */
	public boolean judgeIsSpecial(OrderInfo info) {
		if (info.getOrderGoodsList().size() > 1 || !info.getOrderSource().equals(0)) {
			return false;
		}
		for (String s : specialArr) {
			if (s.equals(info.getOrderGoodsList().get(0).getItemId())) {
				return true;
			}
		}
		return false;
	}

	public void splitGoods(OrderInfo info, boolean isSpecial) {
		if (isSpecial) {
			info.setTdq(7);
			OrderGoods originalGoods = info.getOrderGoodsList().get(0);
			double goodsPrice = CalculationUtils.div(originalGoods.getActualPrice(), 1.112, 2);// 商品价格
			double goodsSinglePrice = CalculationUtils.div(goodsPrice, 7, 2);
			double taxFee = CalculationUtils.sub(originalGoods.getActualPrice(), goodsPrice);// 税费
			info.getOrderDetail().setTaxFee(taxFee);
			info.getOrderDetail().setIncrementTax(taxFee);
			info.getOrderDetail().setPostFee(0.0);
			info.getOrderDetail().setDisAmount(0.0);
			info.getOrderDetail().setTariffTax(0.0);
			info.getOrderDetail().setExciseTax(0.0);
			List<OrderGoods> list = buildGoodsList(info, goodsSinglePrice, originalGoods.getItemQuantity());
			info.setOrderGoodsList(list);
			info.setWeight(3500);
			info.setStatus(0);
		} else {
			OrderGoods goods = info.getOrderGoodsList().stream()
					.filter(orderGoods -> orderGoods.getItemId().equals(SPECIAL_ITEM_ID)).findAny().orElse(null);
			if (goods != null) {
				double goodsSinglePrice = CalculationUtils.div(goods.getActualPrice(), 7, 2);
				List<OrderGoods> list = buildGoodsList(info, goodsSinglePrice, goods.getItemQuantity());
				info.getOrderGoodsList().addAll(list);
				info.getOrderGoodsList().remove(goods);
				info.setTdq(info.getOrderGoodsList().size());
			}
		}
	}

	private List<OrderGoods> buildGoodsList(OrderInfo info, double goodsSinglePrice, int quantity) {
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		// ******写死商品七天面膜*********1
		OrderGoods goods = new OrderGoods();
		goods.setActualPrice(goodsSinglePrice);
		goods.setItemPrice(goodsSinglePrice);
		goods.setItemQuantity(quantity);
		goods.setOrderId(info.getOrderId());
		goods.setItemId("100001208");
		goods.setItemCode("310517622300000300");
		goods.setSku("310517622300000300");
		goods.setItemName("韩国Forencos芙恋可姿星期一 七天面膜10片装(效期:19年6月）");
		goods.setItemImg(
				"https://static.cncoopbuy.com:8080/goods/100000913/master/images/8f8f9297-2173-4104-92e2-a4b4e5d201a8.jpg");
		goods.setGoodsId("100000913");
		list.add(goods);
		// ******写死商品七天面膜*********2
		goods = new OrderGoods();
		goods.setActualPrice(goodsSinglePrice);
		goods.setItemPrice(goodsSinglePrice);
		goods.setItemQuantity(quantity);
		goods.setOrderId(info.getOrderId());
		goods.setItemId("100001209");
		goods.setItemCode("310517622300000301");
		goods.setSku("310517622300000301");
		goods.setItemName("韩国Forencos芙恋可姿星期二 七天面膜10片装（效期:19年6月）");
		goods.setItemImg(
				"https://static.cncoopbuy.com:8080/goods/100000914/master/images/35b484f2-9899-432d-9eef-5269b67314ef.jpg");
		goods.setGoodsId("100000914");
		list.add(goods);
		// ******写死商品七天面膜*********3
		goods = new OrderGoods();
		goods.setActualPrice(goodsSinglePrice);
		goods.setItemPrice(goodsSinglePrice);
		goods.setItemQuantity(quantity);
		goods.setOrderId(info.getOrderId());
		goods.setItemId("100001210");
		goods.setItemCode("310517622300000302");
		goods.setSku("310517622300000302");
		goods.setItemName("韩国Forencos芙恋可姿星期三 七天面膜10片装(效期:19年6月）");
		goods.setItemImg(
				"https://static.cncoopbuy.com:8080/goods/100000915/master/images/843ff607-e028-4692-9b6f-b2b6cee3fc20.jpg");
		goods.setGoodsId("100000915");
		list.add(goods);
		// ******写死商品七天面膜*********4
		goods = new OrderGoods();
		goods.setActualPrice(goodsSinglePrice);
		goods.setItemPrice(goodsSinglePrice);
		goods.setItemQuantity(quantity);
		goods.setOrderId(info.getOrderId());
		goods.setItemId("100001211");
		goods.setItemCode("310517622300000303");
		goods.setSku("310517622300000303");
		goods.setItemName("韩国Forencos芙恋可姿星期四 七天面膜10片装(效期:19年6月）");
		goods.setItemImg(
				"https://static.cncoopbuy.com:8080/goods/100000916/master/images/0384110b-9dbd-40db-82cb-4f057520f39a.jpg");
		goods.setGoodsId("100000916");
		list.add(goods);
		// ******写死商品七天面膜*********5
		goods = new OrderGoods();
		goods.setActualPrice(goodsSinglePrice);
		goods.setItemPrice(goodsSinglePrice);
		goods.setItemQuantity(quantity);
		goods.setOrderId(info.getOrderId());
		goods.setItemId("100001212");
		goods.setItemCode("310517622300000304");
		goods.setSku("310517622300000304");
		goods.setItemName("韩国Forencos芙恋可姿星期五 七天面膜10片装(效期:19年6月）");
		goods.setItemImg(
				"https://static.cncoopbuy.com:8080/goods/100000917/master/images/9df73ee5-271c-443e-aa40-98c170f61a4c.jpg");
		goods.setGoodsId("100000917");
		list.add(goods);
		// ******写死商品七天面膜*********6
		goods = new OrderGoods();
		goods.setActualPrice(goodsSinglePrice);
		goods.setItemPrice(goodsSinglePrice);
		goods.setItemQuantity(quantity);
		goods.setOrderId(info.getOrderId());
		goods.setItemId("100001213");
		goods.setItemCode("310517622300000305");
		goods.setSku("310517622300000305");
		goods.setItemName("韩国Forencos芙恋可姿星期六 七天面膜10片装(效期:19年6月）");
		goods.setItemImg(
				"https://static.cncoopbuy.com:8080/goods/100000918/master/images/b285c36c-5f8d-4ef7-acd3-01c02e1fc59d.jpg");
		goods.setGoodsId("100000918");
		list.add(goods);
		// ******写死商品七天面膜*********7
		goods = new OrderGoods();
		goods.setActualPrice(goodsSinglePrice);
		goods.setItemPrice(goodsSinglePrice);
		goods.setItemQuantity(quantity);
		goods.setOrderId(info.getOrderId());
		goods.setItemId("100001214");
		goods.setItemCode("310517622300000306");
		goods.setSku("310517622300000306");
		goods.setItemName("韩国Forencos芙恋可姿星期日 七天面膜10片装(效期:19年6月）");
		goods.setItemImg(
				"https://static.cncoopbuy.com:8080/goods/100000919/master/images/9ea72a8a-bfdd-4f9f-8af0-0377c0ee37b2.jpg");
		goods.setGoodsId("100000919");
		list.add(goods);
		return list;
	}

	public void splitTax(OrderInfo info) {
		try {
			OrderGoods originalGoods = info.getOrderGoodsList().get(0);
			double goodsPrice = CalculationUtils.div(originalGoods.getActualPrice(), 1.112, 2);// 商品价格
			double taxFee = CalculationUtils.sub(originalGoods.getActualPrice(), goodsPrice);// 税费
			originalGoods.setActualPrice(goodsPrice);
			originalGoods.setItemPrice(goodsPrice);
			info.getOrderDetail().setTaxFee(taxFee);
			info.getOrderDetail().setIncrementTax(taxFee);
			info.getOrderDetail().setPostFee(0.0);
			info.getOrderDetail().setDisAmount(0.0);
			info.getOrderDetail().setTariffTax(0.0);
			info.getOrderDetail().setExciseTax(0.0);
			info.setWeight(5000);
			info.setStatus(0);
		} catch (Exception e) {
		}
	}

	// 北京环卫福利商城
	public boolean judgeIsBJWelfare(OrderInfo info) {
		if (info.getShopId().equals(124) && info.getOrderSource() == Constants.WELFARE_WEBSITE) {
			return true;
		}
		return false;
	}
}
