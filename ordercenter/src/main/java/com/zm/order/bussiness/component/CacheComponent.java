package com.zm.order.bussiness.component;

import java.util.HashMap;
import java.util.Map;

import com.zm.order.pojo.OrderGoodsCacheModel;

public class CacheComponent {

	private static Map<String, OrderGoodsCacheModel> orderGoodsCache = new HashMap<String, OrderGoodsCacheModel>();
	
	public static void putOrderGoodsCache(String key,OrderGoodsCacheModel ogc){
		synchronized (orderGoodsCache) {
			if(orderGoodsCache.containsKey(key)){
				orderGoodsCache.get(key).getOrderNum().incrementAndGet();
			} else {
				orderGoodsCache.put(key, ogc);
			}
		}
	}
	
	public static void clearOrderGoodsCache(){
		orderGoodsCache.clear();
	}
	
	public static Map<String, OrderGoodsCacheModel> getOrderGoodsCache(){
		return orderGoodsCache;
	}
	
}
