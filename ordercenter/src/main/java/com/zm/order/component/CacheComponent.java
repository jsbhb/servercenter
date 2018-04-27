package com.zm.order.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import com.zm.order.constants.Constants;
import com.zm.order.pojo.bo.GradeBO;
import com.zm.order.pojo.bo.IntradayOrderBO;
import com.zm.order.utils.CalculationUtils;

public class CacheComponent {

	private ConcurrentSkipListSet<GradeBO> gradeSet = new ConcurrentSkipListSet<GradeBO>();

//	private Map<String, IntradayOrderBO> statisticsOrderCacheMap = new ConcurrentHashMap<String, IntradayOrderBO>();
//	private Map<String, Double> statisticsSalesCacheMap = new ConcurrentHashMap<String, Double>();

	private static volatile CacheComponent instance;

	private CacheComponent() {
	}

	public static CacheComponent getInstance() {
		if (instance == null) {
			synchronized (CacheComponent.class) {
				if (instance == null) {
					instance = new CacheComponent();
				}
			}
		}
		return instance;
	}
	
	//初始化缓存
//	public void initCache(){
//		statisticsOrderCacheMap.clear();
//		statisticsSalesCacheMap.clear();
//		if (gradeSet != null) {
//			for (GradeBO model : gradeSet) {
//				statisticsOrderCacheMap.put(Constants.ORDER_STATISTICS_WEEK + model.getId(), new IntradayOrderBO());
//				statisticsSalesCacheMap.put(Constants.SALES_STATISTICS_WEEK + model.getId(), 0.0);
//			}
//		}
//	}
//
//	// 销售额缓存
//	public void putAllStatisticsSalesCache(Map<String, Double> map) {
//		statisticsSalesCacheMap.clear();
//		statisticsSalesCacheMap.putAll(map);
//	}
//
//	public Map<String, Double> getstatisticsSalesCache() {
//		return statisticsSalesCacheMap;
//	}
//
//	public void addSalesAmount(Integer gradeId, double amount) {
//		synchronized (statisticsSalesCacheMap) {
//			double tempAmount = statisticsSalesCacheMap.get(Constants.SALES_STATISTICS_WEEK + gradeId);
//			statisticsSalesCacheMap.put(Constants.SALES_STATISTICS_WEEK + gradeId,
//					CalculationUtils.add(tempAmount, amount));
//			if(!Constants.CNCOOPBUY.equals(gradeId)){
//				tempAmount = statisticsSalesCacheMap.get(Constants.SALES_STATISTICS_WEEK + Constants.CNCOOPBUY);
//				statisticsSalesCacheMap.put(Constants.SALES_STATISTICS_WEEK + Constants.CNCOOPBUY,
//						CalculationUtils.add(tempAmount, amount));
//			}
//		}
//	}
//	// end
//
//	// 订单缓存
//	public void putAllStatisticsOrderCache(Map<String, IntradayOrderBO> map) {
//		statisticsOrderCacheMap.clear();
//		statisticsOrderCacheMap.putAll(map);
//	}
//
//	public Map<String, IntradayOrderBO> getstatisticsOrderCache() {
//		return statisticsOrderCacheMap;
//	}
//
//	/**
//	 * @fun 增加销售订单
//	 * @param gradeId
//	 */
//	public void incrProduceOrder(Integer gradeId) {
//		statisticsOrderCacheMap.get(Constants.ORDER_STATISTICS_WEEK + gradeId).incrProduce();
//		if (!Constants.CNCOOPBUY.equals(gradeId)) {
//			statisticsOrderCacheMap.get(Constants.ORDER_STATISTICS_WEEK + Constants.CNCOOPBUY).incrProduce();
//		}
//	}
//	
//	/**
//	 * @fun 增加发货订单
//	 * @param gradeId
//	 */
//	public void incrDeliverOrder(Integer gradeId) {
//		statisticsOrderCacheMap.get(Constants.ORDER_STATISTICS_WEEK + gradeId).incrDeliver();
//		if (!Constants.CNCOOPBUY.equals(gradeId)) {
//			statisticsOrderCacheMap.get(Constants.ORDER_STATISTICS_WEEK + Constants.CNCOOPBUY).incrDeliver();
//		}
//	}
//	
//	/**
//	 * @fun 增加取消订单数量
//	 * @param gradeId
//	 */
//	public void incrCancelOrder(Integer gradeId) {
//		statisticsOrderCacheMap.get(Constants.ORDER_STATISTICS_WEEK + gradeId).incrCancel();
//		if (!Constants.CNCOOPBUY.equals(gradeId)) {
//			statisticsOrderCacheMap.get(Constants.ORDER_STATISTICS_WEEK + Constants.CNCOOPBUY).incrCancel();
//		}
//	}
	// end

	public void addGrade(GradeBO grade) {
		gradeSet.add(grade);
	}

	public ConcurrentSkipListSet<GradeBO> getSet() {
		return gradeSet;
	}
}
