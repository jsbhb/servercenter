package com.zm.pay.component;

import java.util.HashSet;
import java.util.Set;

public class CacheSet {

	private Set<String> cache;

	private static volatile CacheSet instance;

	public static CacheSet getInstance() {
		if (instance == null) {
			synchronized (CacheSet.class) {
				if (instance == null) {
					instance = new CacheSet();
				}
			}
		}
		
		return instance;
	}
	
	private CacheSet(){
		cache = new HashSet<>();
	}
	
	public void set(String oderId){
		cache.add(oderId);
	}
	
	public boolean contain(String orderId){
		return cache.contains(orderId);
	}
	
	public void remove(String orderId){
		cache.remove(orderId);
	}
}
