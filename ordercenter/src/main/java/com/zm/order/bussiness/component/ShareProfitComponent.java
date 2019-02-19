package com.zm.order.bussiness.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.dao.OrderOpenInterfaceMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.FinanceFeignClient;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.DateUtils;
import com.zm.order.utils.JSONUtil;

@Component
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
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
			// 增加缓存订单数量
			cacheAbstractService.addOrderCountCache(info.getShopId(), Constants.ORDER_STATISTICS_DAY, "produce");
			// 增加月订单数
			cacheAbstractService.addOrderCountCache(info.getShopId(), Constants.ORDER_STATISTICS_MONTH, time);

			if(Constants.BARGAIN_ORDER == info.getCreateType()){//砍价订单不计算返佣
				return;
			}
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
					hashOperations.increment(Constants.GRADE_ORDER_REBATE + entry.getKey(), Constants.CAN_BE_PRESENTED,
							CalculationUtils.round(2, Double.valueOf(entry.getValue())));
					hashOperations.increment(Constants.GRADE_ORDER_REBATE + entry.getKey(), Constants.STAY_TO_ACCOUNT,
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
		result.put("orderFlag", orderInfo.getOrderFlag() + "");
		for (Map.Entry<Integer, Double> entry : rebateMap.entrySet()) {
			if(entry.getValue() > 0){
				result.put(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private void calRebate(OrderInfo orderInfo, Map<Integer, Double> rebateMap,
			HashOperations<String, String, String> hashOperations) {
		List<OrderGoods> goodsList = orderInfo.getOrderGoodsList();

		if (goodsList != null) {
			Map<String, String> goodsRebate = null;
			for (OrderGoods goods : goodsList) {
				try {
					String json = goods.getRebate();
					if (json == null || "".equals(json)) {
						continue;
					}
					goodsRebate = JSONUtil.parse(json, Map.class);
					double amount = CalculationUtils.mul(goods.getItemPrice(), goods.getItemQuantity());
					if (goodsRebate != null && goodsRebate.size() > 0) {
						for (Map.Entry<String, String> entry : goodsRebate.entrySet()) {
							// 获取已经计算的返佣的值
							double rebate = rebateMap.get(Integer.valueOf(entry.getKey())) == null ? 0
									: rebateMap.get(Integer.valueOf(entry.getKey()));
							// 返佣放入map，计算（上一级返佣比例减去下一级返佣比例 乘以福利网站返佣
							rebateMap.put(Integer.valueOf(entry.getKey()), CalculationUtils.add(rebate,
									CalculationUtils.mul(amount, Double.valueOf(entry.getValue()))));
						}
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
					if (!template.hasKey(Constants.CAPITAL_PERFIX + orderInfo.getShopId())) {
						orderIdListForCapitalNotEnough.add(orderInfo.getOrderId());
						it.remove();
						continue;
					} else {
						balance = hashOperations.increment(Constants.CAPITAL_PERFIX + orderInfo.getShopId(), Constants.CAPITAL_MONEY,
								CalculationUtils.sub(0, orderInfo.getOrderDetail().getPayment()));// 扣除资金池
						if (balance < 0) {// 如果扣除后小于0，则不发送订单给仓库，并把扣除的资金加回去
							orderIdListForCapitalNotEnough.add(orderInfo.getOrderId());
							it.remove();
							hashOperations.increment(Constants.CAPITAL_PERFIX + orderInfo.getShopId(), Constants.CAPITAL_MONEY,
									orderInfo.getOrderDetail().getPayment());
						} else {// 如果余额足够，把资金放到冻结资金处
							orderIdListForCapitalEnough.add(orderInfo.getOrderId());
							hashOperations.increment(Constants.CAPITAL_PERFIX + orderInfo.getShopId(), Constants.CAPITAL_FROZEN_MONEY,
									orderInfo.getOrderDetail().getPayment());// 冻结资金增加
							hashOperations.increment(Constants.CAPITAL_PERFIX + orderInfo.getShopId(), Constants.CAPITAL_USE_MONEY,
									orderInfo.getOrderDetail().getPayment());// 总共使用的资金增加
							Map<String, Object> capitalPoolDetailMap = getCapitalDetail(orderInfo);
							listOperations.leftPush(Constants.CAPITAL_DETAIL, JSONUtil.toJson(capitalPoolDetailMap));
						}
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
				if (Constants.OPEN_INTERFACE_TYPE != orderInfo.getCreateType()) {
					orderMapper.updateOrderCapitalEnough(orderIdListForCapitalEnough);
				} else {// 对接订单单独处理
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("list", orderIdListForCapitalEnough);
					if (orderInfo.getOrderFlag().equals(Constants.GENERAL_TRADE)) {// 一般贸易订单状态为12
						param.put("status", Constants.CAPITAL_POOL_ENOUGH);
					} else if (orderInfo.getOrderFlag().equals(Constants.O2O_ORDER_TYPE)) {// 跨境订单状态为支付单报关
						param.put("status", Constants.ORDER_PAY_CUSTOMS);
					}
					orderOpenInterfaceMapper.updateOrderStatus(param);
				}
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("【更新订单状态为资金池扣减出错】订单号：" + orderIdListForCapitalEnough.toString(), e);
			for (OrderInfo order : list) {
				try {
					hashOperations.increment(Constants.CAPITAL_PERFIX + order.getCenterId(), Constants.CAPITAL_MONEY,
							order.getOrderDetail().getPayment());
					hashOperations.increment(Constants.CAPITAL_PERFIX + order.getCenterId(), Constants.CAPITAL_FROZEN_MONEY,
							CalculationUtils.sub(0, order.getOrderDetail().getPayment()));
					hashOperations.increment(Constants.CAPITAL_PERFIX + order.getCenterId(), Constants.CAPITAL_USE_MONEY,
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
