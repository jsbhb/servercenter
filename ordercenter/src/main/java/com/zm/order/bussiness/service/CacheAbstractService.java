package com.zm.order.bussiness.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.zm.order.component.CacheComponent;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.bo.GradeBO;
import com.zm.order.utils.DateUtils;


public abstract class CacheAbstractService {
	
	protected static final String HEAD = "head";//头部数据
	protected static final String CHART = "chart";//图表数据
	protected static final String WEEK = "week";//每周
	protected static final String MONTH = "month";//每月
	protected static final String ORDER = "order";//订单
	protected static final String FINANCE = "finance";//财务
	
	protected static final int LAST_WEEK = 7;
	
	@Resource
	private RedisTemplate<String, String> template;
	
	/**
	 * @fun 初始化缓存
	 */
	public abstract void initCache();
	
	/**
	 * @fun 每天凌晨放入周统计
	 */
	public abstract void saveDayCacheToWeek();
	
	/**
	 * @fun 获取缓存
	 * @param gradeId 分级ID
	 * @param dataType 数据类型
	 * @param time 时间按月，按周
	 * @param modelType 模块类型
	 * @return
	 */
	public abstract String getCache(Integer gradeId, String dataType, String time, String modelType);
	
	/**
	 * @fun 每月第一天初始化缓存
	 */
	public void initMonth(){
		Set<GradeBO> set = CacheComponent.getInstance().getSet();
		for (GradeBO grade : set) {
				String time = DateUtils.getTimeString("yyyyMM");
				put(Constants.SALES_STATISTICS_MONTH + grade.getId(), time, "0");
				put(Constants.ORDER_STATISTICS_MONTH + grade.getId(), time, "0");
		}
	}
	
	/**
	 * @fun 新增等级时初始化缓存
	 * @param gradeId
	 */
	public void initNewGradeCache(Integer gradeId){
		//初始化月统计
		String earliest = "201801";
		int monthCount = DateUtils.compareWithNow(earliest);
		for (int i = 0; i <= monthCount; i++) {
			String time = DateUtils.getTime(earliest, "yyyyMM", Calendar.MONTH, i);
			put(Constants.SALES_STATISTICS_MONTH + gradeId, time, "0");
			put(Constants.ORDER_STATISTICS_MONTH + gradeId, time, "0");
		}
		//初始化上周统计
		for(int i=0;i<LAST_WEEK;i++){
			addList(Constants.ORDER_STATISTICS_WEEK + gradeId, "0", LAST_WEEK);
			addList(Constants.SALES_STATISTICS_WEEK + gradeId, "0", LAST_WEEK);
		}
		//初始化当天订单
		Map<String, String> temp = new HashMap<String, String>();
		temp.put("produce", "0");
		temp.put("deliver", "0");
		temp.put("cancel", "0");
		putAll(Constants.ORDER_STATISTICS_DAY + gradeId, temp);
		//初始化当天销售额
		temp.clear();
		temp.put("sales", "0");
		putAll(Constants.SALES_STATISTICS_DAY + gradeId, temp);
	}
	
	/**
	 * @fun 增加订单数量
	 * @param gradeId
	 * @param incr
	 */
	public void addOrderCountCache(Integer gradeId, String perkey, String filed){
		increment(perkey + gradeId, filed, 1);
//		if (!Constants.CNCOOPBUY.equals(gradeId)) {
//			increment(perkey + Constants.CNCOOPBUY, filed, 1);
//		}
	}
	
	/**
	 * @fun 增加销售额
	 * @param gradeId
	 * @param incr
	 */
	public void addSalesCache(Integer gradeId, String perkey, String filed, double amount){
		increment(perkey + gradeId, filed, amount);
//		if (!Constants.CNCOOPBUY.equals(gradeId)) {
//			increment(perkey + Constants.CNCOOPBUY, filed, amount);
//		}
	}
	
	
	/**
	 * @fun 将value 放入指定的redis列表，列表长度超过length时移除第一个， 从尾部插入，length为0时，不限制长度
	 * @param key
	 * @param value
	 * @param lengh
	 */
	public void addList(String key, String value, int lengh) {
		ListOperations<String, String> listOperations = template.opsForList();
		if (lengh == 0) {
			listOperations.rightPush(key, value);
		} else {
			Long size = listOperations.size(key);
			if (size >= lengh) {
				listOperations.leftPop(key);
				listOperations.rightPush(key, value);
			} else {
				listOperations.rightPush(key, value);
			}
		}
	}
	
	
	/**
	 * @fun 将map设置到对应的key
	 * @param key
	 * @param value
	 */
	protected void putAll(String key, Map<String, String> value) {
		template.opsForHash().putAll(key, value);
	}

	/**
	 * @fun 将指定的hashKey和hashValue 放入指定的key
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 */
	protected void put(String key, String hashKey, String hashValue) {
		template.opsForHash().put(key, hashKey, hashValue);
	}

	/**
	 * @fun key的hashKey对应的value 增加指定的值
	 * @param key
	 * @param hashKey
	 * @param incr
	 */
	protected void increment(String key, String hashKey, long incr) {
		template.opsForHash().increment(key, hashKey, incr);
	}

	/**
	 * @fun key的hashKey对应的value 增加指定的值
	 * @param key
	 * @param hashKey
	 * @param incr
	 */
	protected void increment(String key, String hashKey, double incr) {
		template.opsForHash().increment(key, hashKey, incr);
	}

	/**
	 * @fun 获取指定key的hashMap
	 * @param key
	 * @return
	 */
	protected Map<String, String> entries(String key) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		return hashOperations.entries(key);
	}
	
	/**
	 * @fun 获取指定key,指定field的value
	 * @param key
	 * @return
	 */
	protected String entries(String key,String field) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		return hashOperations.get(key, field);
	}

	/**
	 * @fun 获取指定key的list
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	protected List<String> list(String key, int start, int end) {
		ListOperations<String, String> listOperations = template.opsForList();
		return listOperations.range(key, start, end);
	}
	
	/**
	 * @fun 根据all判断清空单个还是清空所有
	 * @param key
	 * @param all
	 */
	protected void empty(String key, boolean all){
		if(all){
			template.delete(getAllKey(key));
		} else {
			template.delete(key);
		}
	}
	
	/**
	 * @fun 模糊获取传入的key的所有keys
	 * @param key
	 * @return
	 */
	protected Set<String> getAllKey(String key){
		return template.keys(key + "*");
	}
}
