package com.zm.thirdcenter.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheMap {

	private Map<String,Object> data = new HashMap<String,Object>();;
	
	private static volatile CacheMap cacheMap;
	
	private CacheMap(){}
	
	public static CacheMap getCache(){
		if(cacheMap == null){
			synchronized (CacheMap.class){
				if(cacheMap == null){
					cacheMap = new CacheMap();
				}
			}
		}
		return cacheMap;
	}
	
	public void put(String key,Object value){
		if(data == null){
			data = new HashMap<String,Object>();
			data.put(key, value);
		}else{
			data.put(key, value);
		}
	}

	public Map<String, Object> getData() {
		return data;
	}
	
}
