package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.order.bussiness.dao.StatisticsMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.component.CacheComponent;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.bo.DiagramBO;
import com.zm.order.pojo.bo.GradeBO;
import com.zm.order.pojo.bo.IntradayOrderBO;
import com.zm.order.pojo.bo.StatisticBO;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.DateUtils;
import com.zm.order.utils.JSONUtil;
import com.zm.order.utils.TreeNodeUtil;

@Service
public class CacheServiceImpl extends CacheAbstractService {

	@Resource
	StatisticsMapper statisticsMapper;

	private static final int LAST_WEEK = 7;

	@Override
	public void initCache() {
		// 清空缓存
		empty(Constants.ORDER_STATISTICS_MONTH, true);
		empty(Constants.SALES_STATISTICS_MONTH, true);
		empty(Constants.ORDER_STATISTICS_WEEK, true);
		empty(Constants.SALES_STATISTICS_WEEK, true);
		empty(Constants.ORDER_STATISTICS_DAY, true);
		empty(Constants.SALES_STATISTICS_DAY, true);
		// end
		List<OrderInfo> weekList = statisticsMapper
				.queryLastWeek(DateUtils.getTime(Calendar.DATE, -LAST_WEEK, "yyyy-MM-dd"));
		List<OrderInfo> allList = statisticsMapper.queryAll();
		if (weekList != null && weekList.size() > 0) {
			// 根据时间yyyyMMdd分装成map
			Map<String, List<OrderInfo>> result = packOrderInfoByCreateTime(weekList);
			// 封装数据并放入缓存
			packLastWeekCache(result, weekList);
		}
		// 分装月数据
		if (allList != null && allList.size() > 0) {
			packMonthCache(allList);
		}

	}

	@Override
	public String getCache(Integer gradeId, String dataType, String time, String modelType) {
		List<Integer> list = new ArrayList<Integer>();
		// 获取所有下级包括自己的gradeId
		TreeNodeUtil.getchildNode(TreeNodeUtil.getchildNode(CacheComponent.getInstance().getSet(), gradeId), list);
		if (list.size() == 0) {
			return null;
		}
		if (HEAD.equals(dataType)) {
			if (ORDER.equals(modelType)) {
				// 获取当天订单统计
				return getTodayOrderStatistics(list);
			}
			if (FINANCE.equals(modelType)) {
				// 获取当天销售额统计和返佣数据
				return getTodaySalesStatistics(gradeId, list);
			}
		}
		if (CHART.equals(dataType)) {
			if (ORDER.equals(modelType)) {
				if (WEEK.equals(time)) {
					// 图表按周统计
					return chartStatisticsByWeek(gradeId, list, Constants.ORDER_STATISTICS_WEEK);
				}
				if (MONTH.equals(time)) {
					// 按月统计图表
					return chartStatisticsByMonth(gradeId, list, Constants.ORDER_STATISTICS_MONTH);
				}
			}
			if (FINANCE.equals(modelType)) {
				if (WEEK.equals(time)) {
					// 图表按周统计
					return chartStatisticsByWeek(gradeId, list, Constants.SALES_STATISTICS_WEEK);
				}
				if (MONTH.equals(time)) {
					// 按月统计图表
					return chartStatisticsByMonth(gradeId, list, Constants.SALES_STATISTICS_MONTH);
				}
			}
		}
		return null;
	}

	@Override
	public void saveDayCacheToWeek() {
		Set<GradeBO> set = CacheComponent.getInstance().getSet();
		for (GradeBO grade : set) {
			// 放入周统计
			String str = entries(Constants.ORDER_STATISTICS_DAY + grade.getId(), "produce");
			addList(Constants.ORDER_STATISTICS_WEEK + grade.getId(), str, LAST_WEEK);
			// 缓存置0
			put(Constants.ORDER_STATISTICS_DAY + grade.getId(), "produce", "0");
			put(Constants.ORDER_STATISTICS_DAY + grade.getId(), "deliver", "0");
			put(Constants.ORDER_STATISTICS_DAY + grade.getId(), "cancel", "0");
			// 放入周统计
			str = entries(Constants.SALES_STATISTICS_DAY + grade.getId(), "sales");
			addList(Constants.SALES_STATISTICS_DAY + grade.getId(), str, LAST_WEEK);
			// 缓存置0
			put(Constants.SALES_STATISTICS_DAY + grade.getId(), "sales", "0");
		}
	}

	private String chartStatisticsByMonth(Integer gradeId, List<Integer> list, String key) {
		Map<String, Double> tempMap = new HashMap<String, Double>();
		Map<Object, Object> tempMapByGrade = new HashMap<Object, Object>();
		Map<String, String> temp = null;
		List<DiagramBO> diagramList = null;
		StatisticBO statisticBO = new StatisticBO();
		diagramList = new ArrayList<DiagramBO>();
		// if (list.contains(Constants.CNCOOPBUY)) {// 如果是海外购。不需要分装
		// temp = entries(key + Constants.CNCOOPBUY);
		// renderData(key, diagramList, temp);
		// } else {
		for (Integer temGradeId : list) {
			temp = entries(key + temGradeId);
			mergeMap(tempMap, temp);
		}
		renderData(key, diagramList, tempMap);
		// }
		statisticBO.setDiagramData(diagramList);

		// 按区域中心封装
		List<Integer> childList = TreeNodeUtil.getChild(CacheComponent.getInstance().getSet(), gradeId);
		if (childList != null && childList.size() > 0) {
			for (Integer tempGradeId : childList) {
				Double total = 0.0;
				list.clear();
				// 获取所有下级包括自己的gradeId
				TreeNodeUtil.getchildNode(TreeNodeUtil.getchildNode(CacheComponent.getInstance().getSet(), tempGradeId),
						list);
				for (Integer temId : list) {
					temp = entries(key + temId);
					for (Map.Entry<String, String> entry : temp.entrySet()) {
						total = CalculationUtils.add(total, Double.valueOf(entry.getValue()));
					}
				}
				tempMapByGrade.put(tempGradeId, total);
			}
			diagramList = new ArrayList<DiagramBO>();
			renderData(key, diagramList, tempMapByGrade);
			statisticBO.setChartData(diagramList);
		}
		return JSONUtil.toJson(statisticBO);
	}

	private void mergeMap(Map<String, Double> tempMap, Map<String, String> temp) {
		if (tempMap.size() == 0) {
			for (Map.Entry<String, String> entry : temp.entrySet()) {
				tempMap.put(entry.getKey(), Double.valueOf(entry.getValue()));
			}
		} else {
			for (Map.Entry<String, String> entry : temp.entrySet()) {
				tempMap.put(entry.getKey(),
						CalculationUtils.add(tempMap.get(entry.getKey()), Double.valueOf(entry.getValue())));
			}
		}

	}

	private String chartStatisticsByWeek(Integer gradeId, List<Integer> list, String key) {
		List<String> tempList = null;
		List<Double> result = new ArrayList<Double>();
		List<DiagramBO> diagramList = null;
		StatisticBO statisticBO = new StatisticBO();
		diagramList = new ArrayList<DiagramBO>();
		// if (list.contains(Constants.CNCOOPBUY)) {// 如果是海外购。不需要分装
		// tempList = list(key + Constants.CNCOOPBUY, 0, -1);
		// renderData(key, tempList, diagramList);
		// } else {
		for (Integer temGradeId : list) {
			tempList = list(key + temGradeId, 0, -1);
			mergeList(result, tempList);
		}
		renderData(key, result, diagramList);
		// }
		statisticBO.setDiagramData(diagramList);
		List<Integer> childList = TreeNodeUtil.getChild(CacheComponent.getInstance().getSet(), gradeId);
		Map<Object, Object> tempMap = new HashMap<Object, Object>();

		// 按区域中心分装
		if (childList != null && childList.size() > 0) {
			for (Integer tempGradeId : childList) {
				Double total = 0.0;
				list.clear();
				// 获取所有下级包括自己的gradeId
				TreeNodeUtil.getchildNode(TreeNodeUtil.getchildNode(CacheComponent.getInstance().getSet(), tempGradeId),
						list);
				for (Integer temId : list) {
					tempList = list(key + temId, 0, -1);
					for (String str : tempList) {
						total = CalculationUtils.add(total, Double.valueOf(str));
					}
				}
				tempMap.put(tempGradeId, total);
			}
			diagramList = new ArrayList<DiagramBO>();
			renderData(key, diagramList, tempMap);
			statisticBO.setChartData(diagramList);
		}
		return JSONUtil.toJson(statisticBO);
	}

	private void renderData(String key, List<DiagramBO> diagramList, Map<? extends Object, ? extends Object> tempMap) {
		DiagramBO diagram;
		for (Map.Entry<? extends Object, ? extends Object> entry : tempMap.entrySet()) {
			String value = entry.getValue().toString();
			if (key.contains("order")) {
				if (value.contains(".")) {
					value = value.substring(0, value.indexOf("."));
				}
			} else if (key.contains("sales")) {
				value = CalculationUtils.round(2, Double.valueOf(value)) + "";
			}
			diagram = new DiagramBO(entry.getKey().toString(), value);
			diagramList.add(diagram);
		}
	}

	private void renderData(String key, List<? extends Object> result, List<DiagramBO> diagramList) {
		DiagramBO diagram;
		for (int i = LAST_WEEK; i > 0; i--) {
			String time = DateUtils.getTime(Calendar.DATE, -i, "yyyy-MM-dd");
			String value = result.get(LAST_WEEK - i).toString();
			if (key.contains("order")) {
				if (value.contains(".")) {
					value = value.substring(0, value.indexOf("."));
				}
			} else if (key.contains("sales")) {
				value = CalculationUtils.round(2, Double.valueOf(value)) + "";
			}
			diagram = new DiagramBO(time, value);
			diagramList.add(diagram);
		}
	}

	private void mergeList(List<Double> list, List<String> tempList) {
		if (list.size() == 0) {
			for (String str : tempList) {
				list.add(Double.valueOf(str));
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				list.set(i, CalculationUtils.add(list.get(i), Double.valueOf(tempList.get(i))));
			}
		}
	}

	private String getTodaySalesStatistics(Integer gradeId, List<Integer> list) {
		Double amount = 0.0;
		Map<String, String> result = new HashMap<String, String>();
		List<DiagramBO> diagramBOList = new ArrayList<DiagramBO>();
		DiagramBO diagramBO = null;
		if (list.contains(Constants.CNCOOPBUY)) {
			// result.putAll(entries(Constants.SALES_STATISTICS_DAY +
			// Constants.CNCOOPBUY));
			double canBePresented = 0.0;
			double alreadyPresented = 0.0;
			for (Integer temGradeId : list) {
				String tempCanBePresented = entries(Constants.GRADE_ORDER_REBATE + temGradeId, "canBePresented");
				canBePresented = CalculationUtils.add(canBePresented,
						Double.valueOf(tempCanBePresented == null ? "0" : tempCanBePresented));
				String tempAlreadyPresented = entries(Constants.GRADE_ORDER_REBATE + temGradeId, "alreadyPresented");
				alreadyPresented = CalculationUtils.add(alreadyPresented,
						Double.valueOf(tempAlreadyPresented == null ? "0" : tempAlreadyPresented));
			}
			result.put("可提现", canBePresented + "");
			result.put("已提现", alreadyPresented + "");
			// for (Map.Entry<String, String> entry : result.entrySet()) {
			// String key = "";
			// if(entry.getKey().equals("sales")){
			// key = "销售额";
			// } else {
			// key = entry.getKey();
			// }
			// diagramBO = new DiagramBO(key, entry.getValue());
			// diagramBOList.add(diagramBO);
			// }
			// return JSONUtil.toJson(diagramBOList);
		} else {
			String tempCanBePresented = entries(Constants.GRADE_ORDER_REBATE + gradeId, "canBePresented");
			String tempAlreadyPresented = entries(Constants.GRADE_ORDER_REBATE + gradeId, "alreadyPresented");
			result.put("可提现", tempCanBePresented == null ? "0" : tempCanBePresented);
			result.put("已提现", tempAlreadyPresented == null ? "0" : tempAlreadyPresented);
		}
		for (Integer temGradeId : list) {
			String temp = entries(Constants.SALES_STATISTICS_DAY + temGradeId, "sales");
			amount = CalculationUtils.add(Double.valueOf(temp), amount);
		}
		result.put("销售额", amount + "");

		for (Map.Entry<String, String> entry : result.entrySet()) {
			diagramBO = new DiagramBO(entry.getKey(), CalculationUtils.round(2, Double.valueOf(entry.getValue())));
			diagramBOList.add(diagramBO);
		}
		return JSONUtil.toJson(diagramBOList);
	}

	@SuppressWarnings("unchecked")
	private String getTodayOrderStatistics(List<Integer> list) {
		IntradayOrderBO temp = null;
		IntradayOrderBO intradayOrderBO = new IntradayOrderBO();
		Map<String, String> tempMap = null;
		List<DiagramBO> diagramBOList = new ArrayList<DiagramBO>();
		DiagramBO diagramBO = null;
		// if (list.contains(Constants.CNCOOPBUY)) {
		// tempMap = entries(Constants.ORDER_STATISTICS_DAY +
		// Constants.CNCOOPBUY);
		// for (Map.Entry<String, String> entry : tempMap.entrySet()) {
		// String key = entry.getKey().equals("produce") ? "产生订单"
		// : entry.getKey().equals("deliver") ? "发货订单" :
		// entry.getKey().equals("cancel") ? "取消订单" : "";
		// diagramBO = new DiagramBO(key, entry.getValue());
		// diagramBOList.add(diagramBO);
		// }
		// return JSONUtil.toJson(diagramBOList);
		// }
		for (Integer temGradeId : list) {
			temp = JSONUtil.parse(JSONUtil.toJson(entries(Constants.ORDER_STATISTICS_DAY + temGradeId)),
					IntradayOrderBO.class);
			intradayOrderBO.add(temp);
		}
		tempMap = JSONUtil.parse(JSONUtil.toJson(intradayOrderBO), Map.class);
		for (Map.Entry<String, String> entry : tempMap.entrySet()) {
			String key = entry.getKey().equals("produce") ? "产生订单"
					: entry.getKey().equals("deliver") ? "发货订单" : entry.getKey().equals("cancel") ? "取消订单" : "";
			diagramBO = new DiagramBO(key, entry.getValue());
			diagramBOList.add(diagramBO);
		}
		return JSONUtil.toJson(diagramBOList);
	}

	private void packMonthCache(List<OrderInfo> allList) {
		initMonthRedisCache();
		for (OrderInfo info : allList) {
			String time = DateUtils.getTimeString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMM");
			increment(Constants.ORDER_STATISTICS_MONTH + info.getShopId(), time, 1);
			if (!Constants.ORDER_CLOSE.equals(info.getStatus())) {
				increment(Constants.SALES_STATISTICS_MONTH + info.getShopId(), time,
						info.getOrderDetail().getPayment());
			}
//			if (info.getShopId() != Constants.CNCOOPBUY) {
//				increment(Constants.ORDER_STATISTICS_MONTH + Constants.CNCOOPBUY, time, 1);
//				if (!Constants.ORDER_CLOSE.equals(info.getStatus())) {
//					increment(Constants.SALES_STATISTICS_MONTH + Constants.CNCOOPBUY, time,
//							info.getOrderDetail().getPayment());
//				}
//			}
		}
	}

	/**
	 * @fun 初始化从最早月份开始的redis数据为0
	 */
	private void initMonthRedisCache() {
		Set<GradeBO> set = CacheComponent.getInstance().getSet();
		// 最早订单的月份
		String earliest = "201801";
		int monthCount = DateUtils.compareWithNow(earliest);
		if (set != null) {
			for (GradeBO grade : set) {
				for (int i = 0; i <= monthCount; i++) {
					String time = DateUtils.getTime(earliest, "yyyyMM", Calendar.MONTH, i);
					put(Constants.SALES_STATISTICS_MONTH + grade.getId(), time, "0");
					put(Constants.ORDER_STATISTICS_MONTH + grade.getId(), time, "0");
				}
			}
		}
	}

	/**
	 * @fun 以天 分装订单
	 * @param weekList
	 * @return
	 */
	private Map<String, List<OrderInfo>> packOrderInfoByCreateTime(List<OrderInfo> weekList) {
		Map<String, List<OrderInfo>> map = new HashMap<String, List<OrderInfo>>();
		List<OrderInfo> temp;
		for (OrderInfo info : weekList) {
			if (map.get(DateUtils.getTimeString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMdd")) == null) {
				temp = new ArrayList<OrderInfo>();
				temp.add(info);
				map.put(DateUtils.getTimeString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMdd"), temp);
			} else {
				map.get(DateUtils.getTimeString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMdd")).add(info);
			}
		}
		return map;
	}

	private static final int PRODUCE = 0;
	private static final int DELIVER = 1;
	private static final int CANCEL = 2;

	/**
	 * @fun 封装上星期数据,并统计当天数据(包括订单量和销售额)
	 * @param result
	 */
	private void packLastWeekCache(Map<String, List<OrderInfo>> result, List<OrderInfo> weekList) {
		List<OrderInfo> temp = null;
		// 订单数量统计map
		Map<String, IntradayOrderBO> orderCacheMap = new HashMap<String, IntradayOrderBO>();
		// 销售额统计map
		Map<String, Double> salesAmountCacheMap = new HashMap<String, Double>();
		for (int i = LAST_WEEK; i >= 0; i--) {
			String time = DateUtils.getTime(Calendar.DATE, -i, "yyyyMMdd");
			temp = result.get(time);

			// 初始化缓存map
			initCacheMap(orderCacheMap, salesAmountCacheMap);
			if (temp != null) {
				if (i != 0) {
					for (OrderInfo info : temp) {
						packOrderCacheMap(orderCacheMap, Constants.ORDER_STATISTICS_WEEK, info.getShopId(), PRODUCE);
						calSalesAmount(salesAmountCacheMap, info, Constants.SALES_STATISTICS_WEEK);
					}
				} else {
					for (OrderInfo info : temp) {
						packOrderCacheMap(orderCacheMap, Constants.ORDER_STATISTICS_DAY, info.getShopId(), PRODUCE);
						calSalesAmount(salesAmountCacheMap, info, Constants.SALES_STATISTICS_DAY);
					}
				}
				// 如果是当天，需要循环上一周所有的订单，拿到在当天取消和发货的订单（这个数据会漏掉7天前的订单在当天取消或发货的订单）
				if (i == 0) {
					for (OrderInfo tempInfo : weekList) {
						if (!"".equals(tempInfo.getUpdateTime()) && tempInfo.getUpdateTime() != null) {
							if (DateUtils.judgeTime(tempInfo.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")) {
								if (Constants.ORDER_DELIVER.equals(tempInfo.getStatus())) {
									packOrderCacheMap(orderCacheMap, Constants.ORDER_STATISTICS_DAY,
											tempInfo.getShopId(), DELIVER);
								}
								if (Constants.ORDER_CANCEL.equals(tempInfo.getStatus())
										|| Constants.ORDER_CLOSE.equals(tempInfo.getStatus())) {
									packOrderCacheMap(orderCacheMap, Constants.ORDER_STATISTICS_DAY,
											tempInfo.getShopId(), CANCEL);
								}
							}
						}
					}
				}
				// 放入redis，当天的放在本地缓存
				// 清空cachceMap，并初始化海外购
				if (i != 0) {
					putToRedis(orderCacheMap, salesAmountCacheMap);
				} else {
					putToRedisIntraday(orderCacheMap, salesAmountCacheMap);
				}
			} else {
				if (i != 0) {
					putToRedis(orderCacheMap, salesAmountCacheMap);
				} else {
					putToRedisIntraday(orderCacheMap, salesAmountCacheMap);
				}
			}
		}
	}

	@SuppressWarnings("serial")
	private void putToRedisIntraday(Map<String, IntradayOrderBO> orderCacheMap,
			Map<String, Double> salesAmountCacheMap) {
		IntradayOrderBO intradayOrderBO = null;
		Map<String, String> temp = null;
		for (Map.Entry<String, IntradayOrderBO> entry : orderCacheMap.entrySet()) {
			if (entry.getKey().contains("day")) {
				intradayOrderBO = entry.getValue();
				temp = new HashMap<String, String>();
				temp.put("produce", intradayOrderBO.getProduce().toString());
				temp.put("deliver", intradayOrderBO.getDeliver().toString());
				temp.put("cancel", intradayOrderBO.getCancel().toString());
				putAll(entry.getKey(), temp);
			}
		}

		for (final Map.Entry<String, Double> entry : salesAmountCacheMap.entrySet()) {
			if (entry.getKey().contains("day")) {
				putAll(entry.getKey(), new HashMap<String, String>() {
					{
						put("sales", entry.getValue().toString());
					}
				});
			}
		}

	}

	/**
	 * @fun 初始化缓存map
	 * @param orderCacheMap
	 * @param salesAmountCacheMap
	 */
	private void initCacheMap(Map<String, IntradayOrderBO> orderCacheMap, Map<String, Double> salesAmountCacheMap) {
		Set<GradeBO> set = CacheComponent.getInstance().getSet();
		orderCacheMap.clear();
		salesAmountCacheMap.clear();
		if (set != null) {
			for (GradeBO model : set) {
				orderCacheMap.put(Constants.ORDER_STATISTICS_WEEK + model.getId(), new IntradayOrderBO());
				salesAmountCacheMap.put(Constants.SALES_STATISTICS_WEEK + model.getId(), 0.0);
				orderCacheMap.put(Constants.ORDER_STATISTICS_DAY + model.getId(), new IntradayOrderBO());
				salesAmountCacheMap.put(Constants.SALES_STATISTICS_DAY + model.getId(), 0.0);
			}
		}
	}

	/**
	 * @fun 把数据放入redis
	 * @param orderCacheMap
	 * @param salesAmountCacheMap
	 */
	private void putToRedis(Map<String, IntradayOrderBO> orderCacheMap, Map<String, Double> salesAmountCacheMap) {
		for (Map.Entry<String, IntradayOrderBO> entry : orderCacheMap.entrySet()) {
			if (entry.getKey().contains("week")) {
				addList(entry.getKey(), entry.getValue().getProduce().toString(), LAST_WEEK);
			}
		}

		for (Map.Entry<String, Double> entry : salesAmountCacheMap.entrySet()) {
			if (entry.getKey().contains("week")) {
				addList(entry.getKey(), entry.getValue().toString(), LAST_WEEK);
			}
		}
	}

	/**
	 * @fun 封装销售额
	 * @param salesAmountCacheMap
	 * @param info
	 */
	private void calSalesAmount(Map<String, Double> salesAmountCacheMap, OrderInfo info, String perkey) {
		// 没付款的不计入销售额
		if (!Constants.ORDER_CLOSE.equals(info.getStatus()) && !Constants.ORDER_INIT.equals(info.getStatus())) {
			if (salesAmountCacheMap.get(perkey + info.getShopId()) == null) {
				salesAmountCacheMap.put(perkey, info.getOrderDetail().getPayment());
			} else {
				double tempAmount = salesAmountCacheMap.get(perkey + info.getShopId());
				salesAmountCacheMap.put(perkey + info.getShopId(),
						CalculationUtils.add(tempAmount, info.getOrderDetail().getPayment()));
			}
			// if (info.getShopId() != Constants.CNCOOPBUY) {
			// double tempAmount = salesAmountCacheMap.get(perkey +
			// Constants.CNCOOPBUY);
			// salesAmountCacheMap.put(perkey + Constants.CNCOOPBUY,
			// CalculationUtils.add(tempAmount,
			// info.getOrderDetail().getPayment()));
			// }
		}

	}

	/**
	 * @fun 封装订单数量
	 * @param cacheMap
	 * @param key
	 * @param type
	 */
	private void packOrderCacheMap(Map<String, IntradayOrderBO> cacheMap, String perkey, Integer gradeId,
			Integer type) {
		IntradayOrderBO intradayOrder = null;
		if (cacheMap.get(perkey + gradeId) == null) {
			intradayOrder = new IntradayOrderBO();
			calQuantity(type, intradayOrder);
			cacheMap.put(perkey + gradeId, intradayOrder);
		} else {
			intradayOrder = cacheMap.get(perkey + gradeId);
			calQuantity(type, intradayOrder);
		}
		// 有可能shopId就是2
		// if (!gradeId.equals(Constants.CNCOOPBUY)) {
		// intradayOrder = cacheMap.get(perkey + Constants.CNCOOPBUY);
		// calQuantity(type, intradayOrder);
		// }
	}

	private void calQuantity(Integer type, IntradayOrderBO intradayOrder) {
		switch (type) {
		case PRODUCE:
			intradayOrder.incrProduce();
			break;
		case DELIVER:
			intradayOrder.incrDeliver();
			break;
		case CANCEL:
			intradayOrder.incrCancel();
			break;
		}
	}
}
