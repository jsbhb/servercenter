package com.zm.goods.utils.lucene;

import java.util.HashMap;
import java.util.Map;

import com.zm.goods.utils.lucene.impl.GoodsLucene;

public class LuceneFactory {

	private static Map<Integer, AbstractLucene> data = new HashMap<Integer, AbstractLucene>();
	
	public static synchronized AbstractLucene get(Integer centerId){
		if(data.get(centerId)== null){
			AbstractLucene al = new GoodsLucene(centerId);
			data.put(centerId, al);
			return al;
		} else {
			return data.get(centerId);
		}
	}
}
