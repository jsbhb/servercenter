package com.zm.order.bussiness.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.order.bussiness.component.model.ShareProfitModel;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.dao.OrderOpenInterfaceMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.component.CacheComponent;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.FinanceFeignClient;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ProfitProportion;
import com.zm.order.pojo.UserInfo;
import com.zm.order.pojo.bo.GradeBO;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.DateUtils;
import com.zm.order.utils.JSONUtil;
import com.zm.order.utils.TreeNodeUtil;

@Component
public class ShareProfitComponent {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	OrderMapper orderMapper;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	FinanceFeignClient financeFeignClient;

	@Resource
	CacheAbstractService cacheAbstractService;
	
	@Resource
	OrderOpenInterfaceMapper orderOpenInterfaceMapper;

	private static final Integer REBATE_DETAIL_FINISH = 1;
	private static final Integer REBATE_DETAIL_CANCEL = 2;

	private static Integer register_type = 0;// 注册地分润
	private static Integer consume_type = 1;// 消费地分润
	private static Integer register_consume_type = 3;// 注册地消费地相同

	public void calShareProfit(String orderId) {

		try {

			OrderInfo info = orderMapper.getOrderByOrderId(orderId);
			if (info == null) {
				return;
			}
			if (Constants.PREDETERMINE_ORDER.equals(info.getOrderSource())) {
				predetermineOrderProfit(info);
			} else {
				calCanBePresented(info);
			}

		} catch (Exception e) {
			LogUtil.writeErrorLog("【从待到账到已到账的返佣计算出错】订单号：" + orderId, e);
		}

	}

	/**
	 * @fun 计算待到账的金额并扣减资金池
	 * @param orderId
	 */
	public void calShareProfitStayToAccount(String orderId) {

		try {
			// 判断redis里是否有该订单，有的话说明正在计算
			synchronized (ShareProfitComponent.class) {
				boolean flag = template.opsForSet().isMember(Constants.ORDER_REBATE, orderId);
				if (flag) {
					return;
				} else {
					template.opsForSet().add(Constants.ORDER_REBATE, orderId);
				}
			}

			calcapitalpool(orderId);// 扣减资金池

			OrderInfo info = orderMapper.getOrderByOrderIdForRebate(orderId);
			if (info == null) {
				return;
			}
			String time = DateUtils.getTimeString("yyyyMM");
			// 增加当天销售额
			cacheAbstractService.addSalesCache(info.getShopId(), Constants.SALES_STATISTICS_DAY, "sales",
					info.getOrderDetail().getPayment());
			// 增加月销售额
			cacheAbstractService.addSalesCache(info.getShopId(), Constants.SALES_STATISTICS_MONTH, time,
					info.getOrderDetail().getPayment());

			if (Constants.PREDETERMINE_ORDER == info.getOrderSource()) {
				predetermineOrderProfitStayToAccount(info);
			} else {
				orderProfitStayToAccount(info);
			}
			orderMapper.updateOrderRebate(orderId);
		} catch (Exception e) {
			template.opsForList().leftPush(Constants.EXCEPTION_PROFIT, orderId);
			LogUtil.writeErrorLog("【待到账返佣计算出错】订单号：" + orderId, e);
		}

	}

	/**
	 * @fun 退款时扣除待到账金额
	 * @param orderId
	 */
	public void calRefundShareProfit(String orderId) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		template.opsForSet().remove(Constants.ORDER_REBATE, orderId);
		Map<String, String> result = hashOperations.entries(Constants.REBATE_DETAIL + orderId);
		for (Map.Entry<String, String> entry : result.entrySet()) {
			if (!"orderId".equals(entry.getKey())) {
				hashOperations.increment(Constants.GRADE_ORDER_REBATE + entry.getKey(), "stayToAccount",
						CalculationUtils.sub(0, CalculationUtils.round(2, Double.valueOf(entry.getValue()))));
				LogUtil.writeLog("退单返回返佣====GradeId:" + entry.getKey() + ",返佣=" + entry.getValue());
			}
		}
		template.delete(Constants.REBATE_DETAIL + orderId);
		financeFeignClient.updateRebateDetail(Constants.FIRST_VERSION, orderId, REBATE_DETAIL_CANCEL);
	}

	/**
	 * @fun 订货平台分润
	 * @param info
	 */
	private void predetermineOrderProfit(OrderInfo info) {
		// TODO Auto-generated method stub

	}

	/**
	 * @fun 订货平台待到账分润
	 * @param info
	 */
	private void predetermineOrderProfitStayToAccount(OrderInfo info) {
		// TODO Auto-generated method stub

	}

	/**
	 * @fun 消费订单分润计算(原先有消费地和注册地之分)
	 * @param info
	 * @deprecated
	 */
	private void consumOrderProfit(OrderInfo info) {
		if (info.getOrderGoodsList() != null) {
			List<OrderBussinessModel> list = new ArrayList<OrderBussinessModel>();
			OrderBussinessModel model = null;
			for (OrderGoods goods : info.getOrderGoodsList()) {
				model = new OrderBussinessModel();
				model.setItemId(goods.getItemId());
				model.setQuantity(goods.getItemQuantity());
				list.add(model);
			}
			Double cost = goodsFeignClient.getCostPrice(Constants.FIRST_VERSION, list);
			// TODO 结算邮费

			Double totalProfit = 0.0;
			if (cost != null && cost > 0) {
				totalProfit = CalculationUtils.sub(info.getOrderDetail().getPayment(),
						info.getOrderDetail().getTaxFee());
				totalProfit = CalculationUtils.sub(totalProfit, cost);
			}

			UserInfo user = userFeignClient.getUser(Constants.FIRST_VERSION, info.getUserId());
			ProfitProportion profitProportion = orderMapper.getProfitProportion(info.getCenterId());

			// 获取用于分润的钱
			Double totalShareProfit = CalculationUtils.mul(totalProfit, profitProportion.getTotal_profit_proportion());

			if (info.getShopId() == null && user.getShopId() == null) {
				return;
			}
			boolean crossArea = false;
			if (!info.getCenterId().equals(user.getCenterId())) {
				crossArea = true;
			}
			if (info.getShopId() == null && user.getShopId() != null) {
				setShareProfit(totalProfit, profitProportion, totalShareProfit, info, register_type, crossArea,
						user.getShopId());
			}
			if (info.getShopId() != null && user.getShopId() == null) {
				setShareProfit(totalProfit, profitProportion, totalShareProfit, info, consume_type, crossArea,
						info.getShopId());
			}
			if (info.getShopId() != null && user.getShopId() != null) {
				if (info.getShopId().equals(user.getShopId())) {
					setShareProfit(totalProfit, profitProportion, totalShareProfit, info, register_consume_type,
							crossArea, user.getShopId());
				} else {
					setShareProfit(totalProfit, profitProportion, totalShareProfit, info, crossArea, user.getShopId(),
							info.getShopId());
				}
			}

		}
	}

	private void setShareProfit(Double totalProfit, ProfitProportion profitProportion, Double totalShareProfit,
			OrderInfo info, boolean crossArea, Integer shopId, Integer shopId2) {

		ShareProfitModel shareProfitModel = null;
		shareProfitModel = new ShareProfitModel();
		shareProfitModel.setOrderAmount(info.getOrderDetail().getPayment());
		shareProfitModel.setOrderId(info.getOrderId());
		shareProfitModel.setOrderProfit(totalProfit);
		shareProfitModel.setRegisterProfit(
				CalculationUtils.mul(totalShareProfit, profitProportion.getRegister_profit_proportion()));
		shareProfitModel.setCrossArea(crossArea);
		template.opsForList().rightPush(Constants.PROFIT + shopId, shareProfitModel);

		shareProfitModel = new ShareProfitModel();
		shareProfitModel.setOrderAmount(info.getOrderDetail().getPayment());
		shareProfitModel.setOrderId(info.getOrderId());
		shareProfitModel.setOrderProfit(totalProfit);
		shareProfitModel.setConsumeProfit(
				CalculationUtils.mul(totalShareProfit, profitProportion.getConsume_profit_proportion()));
		shareProfitModel.setCrossArea(crossArea);
		template.opsForList().rightPush(Constants.PROFIT + shopId2, shareProfitModel);
	}

	/**
	 * 
	 * @param totalProfit
	 * @param profitProportion
	 * @param totalShareProfit
	 * @param info
	 * @param type
	 *            0注册地分润；1消费地分润
	 * @param crossArea
	 * @param shopId
	 */
	private void setShareProfit(Double totalProfit, ProfitProportion profitProportion, Double totalShareProfit,
			OrderInfo info, Integer type, boolean crossArea, Integer shopId) {

		ShareProfitModel shareProfitModel = new ShareProfitModel();
		shareProfitModel.setOrderAmount(info.getOrderDetail().getPayment());
		shareProfitModel.setOrderId(info.getOrderId());
		shareProfitModel.setOrderProfit(totalProfit);
		if (register_type.equals(type)) {
			shareProfitModel.setRegisterProfit(
					CalculationUtils.mul(totalShareProfit, profitProportion.getRegister_profit_proportion()));
		}
		if (consume_type.equals(type)) {
			shareProfitModel.setConsumeProfit(
					CalculationUtils.mul(totalShareProfit, profitProportion.getConsume_profit_proportion()));
		}
		if (register_consume_type.equals(type)) {
			shareProfitModel.setConsumeProfit(
					CalculationUtils.mul(totalShareProfit, profitProportion.getConsume_profit_proportion()));
			shareProfitModel.setRegisterProfit(
					CalculationUtils.mul(totalShareProfit, profitProportion.getRegister_profit_proportion()));
		}
		shareProfitModel.setCrossArea(crossArea);

		template.opsForList().rightPush(Constants.PROFIT + shopId, shareProfitModel);
	}

	/**
	 * @fun 订单返佣计算，放入待到账中
	 * @param orderInfo
	 */
	private void orderProfitStayToAccount(OrderInfo orderInfo) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<Integer, Double> rebateMap = new HashMap<Integer, Double>();
		calRebate(orderInfo, rebateMap, hashOperations);
		for (Map.Entry<Integer, Double> entry : rebateMap.entrySet()) {
			hashOperations.increment(Constants.GRADE_ORDER_REBATE + entry.getKey(), "stayToAccount",
					CalculationUtils.round(2, entry.getValue()));
		}
		Map<String, String> result = packageDetailMap(orderInfo, rebateMap);
		financeFeignClient.saveRebateDetail(Constants.FIRST_VERSION, result);
		template.opsForHash().putAll(Constants.REBATE_DETAIL + orderInfo.getOrderId(), result);

	}

	/**
	 * @fun 计算可提现金额（从待到账金额转到可提现金额）
	 */

	private void calCanBePresented(OrderInfo orderInfo) {
		try {
			Long count = template.opsForSet().remove(Constants.ORDER_REBATE, orderInfo.getOrderId());
			if (count > 0) {
				HashOperations<String, String, String> hashOperations = template.opsForHash();
				Map<String, String> result = hashOperations.entries(Constants.REBATE_DETAIL + orderInfo.getOrderId());
				result.remove("orderId", orderInfo.getOrderId());
				for (Map.Entry<String, String> entry : result.entrySet()) {
					hashOperations.increment(Constants.GRADE_ORDER_REBATE + entry.getKey(), "canBePresented",
							CalculationUtils.round(2, Double.valueOf(entry.getValue())));
					hashOperations.increment(Constants.GRADE_ORDER_REBATE + entry.getKey(), "stayToAccount",
							CalculationUtils.sub(0, CalculationUtils.round(2, Double.valueOf(entry.getValue()))));
				}
				template.delete(Constants.REBATE_DETAIL + orderInfo.getOrderId());
				financeFeignClient.updateRebateDetail(Constants.FIRST_VERSION, orderInfo.getOrderId(),
						REBATE_DETAIL_FINISH);
			}
		} catch (Exception e) {
			template.opsForSet().add(Constants.ORDER_REBATE, orderInfo.getOrderId());
			LogUtil.writeErrorLog("【从待到账金额转到可提现金额出错】订单号:" + orderInfo.getOrderId(), e);
		}

	}

	private Map<String, String> packageDetailMap(OrderInfo orderInfo, Map<Integer, Double> rebateMap) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("orderId", orderInfo.getOrderId());
		for (Map.Entry<Integer, Double> entry : rebateMap.entrySet()) {
			result.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return result;
	}

	private void calRebate(OrderInfo orderInfo, Map<Integer, Double> rebateMap,
			HashOperations<String, String, String> hashOperations) {
		List<OrderGoods> goodsList = orderInfo.getOrderGoodsList();

		if (goodsList != null) {
			Map<String, String> goodsRebate = null;
			GradeBO grade = null;
			for (OrderGoods goods : goodsList) {
				// 获取该订单所有的上级,包括推手
				LinkedList<GradeBO> superNodeList = TreeNodeUtil.getSuperNode(CacheComponent.getInstance().getSet(),
						orderInfo.getShopId());
				goodsRebate = hashOperations.entries(Constants.GOODS_REBATE + goods.getItemId());
				if (goodsRebate == null && superNodeList != null) {
					continue;
				}
				try {
					double amount = CalculationUtils.mul(goods.getItemPrice(), goods.getItemQuantity());
					double nextProportion = 0;// 下一级的返佣比例
					// 获取父级的时候已经按照先进先出排完续
					while (!superNodeList.isEmpty()) {
						grade = superNodeList.poll();
						// 如果是海外购，则不进行返佣
						if (grade.getId().equals(Constants.CNCOOPBUY)) {
							continue;
						}
						// 获取已经计算的返佣的值
						double rebate = rebateMap.get(grade.getId()) == null ? 0 : rebateMap.get(grade.getId());
						// 获取该类型的返佣的比例
						double proportion = Double.valueOf(goodsRebate.get(grade.getGradeType() + "") == null ? "0"
								: goodsRebate.get(grade.getGradeType() + ""));
						rebateMap.put(grade.getId(), CalculationUtils.add(rebate,
								CalculationUtils.mul(amount, CalculationUtils.sub(proportion, nextProportion))));
						nextProportion = proportion;// 记录下一级的返佣比例

					}
				} catch (Exception e) {
					LogUtil.writeErrorLog("【单个商品返佣计算出错】商品ID:" + goods.getGoodsId(), e);
				}
			}
		}
	}

	/**
	 * 资金池扣减
	 * 
	 * @param orderId
	 */
	public void calcapitalpool(String orderId) {
		List<OrderInfo> list = new ArrayList<OrderInfo>();
		list.addAll(orderMapper.listOrderForCalCapital(orderId));

		HashOperations<String, Object, Object> hashOperations = template.opsForHash();
		ListOperations<String, Object> listOperations = template.opsForList();
		Iterator<OrderInfo> it = list.iterator();
		List<String> orderIdListForCapitalNotEnough = new ArrayList<String>();
		List<String> orderIdListForCapitalEnough = new ArrayList<String>();
		OrderInfo orderInfo = null;
		Double balance = null;
		while (it.hasNext()) {
			orderInfo = it.next();
			if ((Constants.PREDETERMINE_PLAT_TYPE != orderInfo.getCenterId()
					&& Constants.CNCOOPBUY != orderInfo.getCenterId())
					|| Constants.OPEN_INTERFACE_TYPE == orderInfo.getCreateType()) {
				balance = null;
				try {
					balance = hashOperations.increment(Constants.CAPITAL_PERFIX + orderInfo.getShopId(), "money",
							CalculationUtils.sub(0, orderInfo.getOrderDetail().getPayment()));// 扣除资金池
					if (balance < 0) {// 如果扣除后小于0，则不发送订单给仓库，并把扣除的资金加回去
						orderIdListForCapitalNotEnough.add(orderInfo.getOrderId());
						it.remove();
						hashOperations.increment(Constants.CAPITAL_PERFIX + orderInfo.getShopId(), "money",
								orderInfo.getOrderDetail().getPayment());
					} else {// 如果余额足够，把资金放到冻结资金处
						orderIdListForCapitalEnough.add(orderInfo.getOrderId());
						hashOperations.increment(Constants.CAPITAL_PERFIX + orderInfo.getShopId(), "frozenMoney",
								orderInfo.getOrderDetail().getPayment());// 冻结资金增加
						hashOperations.increment(Constants.CAPITAL_PERFIX + orderInfo.getShopId(), "useMoney",
								orderInfo.getOrderDetail().getPayment());// 总共使用的资金增加
						Map<String, Object> capitalPoolDetailMap = getCapitalDetail(orderInfo);
						listOperations.leftPush(Constants.CAPITAL_DETAIL, JSONUtil.toJson(capitalPoolDetailMap));
					}
				} catch (Exception e) {
					if (balance == null) {
						LogUtil.writeErrorLog("【扣减资金池出错】订单号：" + orderInfo.getOrderId(), e);
						it.remove();// 不确定资金池够不够，先移除
					} else if (balance < 0) {// 扣减资金池成功，加回资金池时出错，资金池出现负数的时候可能这里出现问题
						LogUtil.writeErrorLog("【加回资金池出错】订单号：" + orderInfo.getOrderId(), e);
					} else {// 加冻结资金时出错或增加记录时出错，不影响整体流程
						LogUtil.writeErrorLog("【记录或加冻结资金出错】订单号：" + orderInfo.getOrderId(), e);
					}
				}
			} else {
				orderIdListForCapitalEnough.add(orderId);
			}
		}
		if (orderIdListForCapitalNotEnough.size() > 0) {
			orderMapper.updateOrderCapitalNotEnough(orderIdListForCapitalNotEnough);
		}
		try {// 资金够的更新出错需要回滚
			if (orderIdListForCapitalEnough.size() > 0) {
				if(Constants.OPEN_INTERFACE_TYPE != orderInfo.getCreateType()){
					orderMapper.updateOrderCapitalEnough(orderIdListForCapitalEnough);
				} else {//对接订单单独处理
					Map<String,Object> param = new HashMap<String, Object>();
					param.put("list", orderIdListForCapitalEnough);
					if(orderInfo.getOrderFlag().equals(Constants.GENERAL_TRADE)){//一般贸易订单状态为12
						param.put("status", Constants.CAPITAL_POOL_ENOUGH);
					} else if(orderInfo.getOrderFlag().equals(Constants.O2O_ORDER_TYPE)){//跨境订单状态为支付单报关
						param.put("status", Constants.ORDER_PAY_CUSTOMS);
					}
					orderOpenInterfaceMapper.updateOrderStatus(param);
				}
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("【更新订单状态为资金池扣减出错】订单号：" + orderIdListForCapitalEnough.toString(), e);
			for (OrderInfo order : list) {
				try {
					hashOperations.increment(Constants.CAPITAL_PERFIX + order.getCenterId(), "money",
							order.getOrderDetail().getPayment());
					hashOperations.increment(Constants.CAPITAL_PERFIX + order.getCenterId(), "frozenMoney",
							CalculationUtils.sub(0, order.getOrderDetail().getPayment()));
					hashOperations.increment(Constants.CAPITAL_PERFIX + order.getCenterId(), "useMoney",
							CalculationUtils.sub(0, order.getOrderDetail().getPayment()));
				} catch (Exception e2) {
					LogUtil.writeErrorLog("【资金回滚出错】订单号：" + order.getOrderId(), e2);
				}
			}
		}
	}

	private Map<String, Object> getCapitalDetail(OrderInfo orderInfo) {
		Map<String, Object> capitalPoolDetailMap = new HashMap<String, Object>();
		capitalPoolDetailMap.put("centerId", orderInfo.getShopId().toString());
		capitalPoolDetailMap.put("payType", "1");// 类型是支出
		capitalPoolDetailMap.put("businessType", "2");// 方式是现金
		capitalPoolDetailMap.put("money", orderInfo.getOrderDetail().getPayment().toString());
		capitalPoolDetailMap.put("orderId", orderInfo.getOrderId());
		capitalPoolDetailMap.put("remark", "订单产生，资金池扣减");
		capitalPoolDetailMap.put("createTime", orderInfo.getCreateTime());
		return capitalPoolDetailMap;
	}

}
