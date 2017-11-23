package com.zm.supplier.supplierinf;

import java.util.HashMap;
import java.util.Map;

import com.zm.supplier.supplierinf.model.AccessToken;

public class AccessTokenCacheMap {

	private Map<String, AccessToken> cache;

	private static volatile AccessTokenCacheMap instance;

	public static AccessTokenCacheMap getInstance() {
		if (instance == null) {
			synchronized (AccessTokenCacheMap.class) {
				if (instance == null) {
					instance = new AccessTokenCacheMap();
				}
			}
		}
		
		return instance;
	}
	
	private AccessTokenCacheMap(){
		cache = new HashMap<String, AccessToken>();
	}
	
	public void set(String key, AccessToken token){
		cache.put(key, token);
	}
	
	public AccessToken get(String key){
		return cache.get(key);
	}
}
