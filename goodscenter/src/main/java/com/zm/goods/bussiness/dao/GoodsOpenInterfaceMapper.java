package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.goods.pojo.GoodsDetail;
import com.zm.goods.pojo.GoodsStock;

public interface GoodsOpenInterfaceMapper {

	List<GoodsStock> listGoodsStock(String[] itemIdArr);
	
	List<GoodsDetail> listGoodsDetail(String[] itemIdArr);
	
	List<GoodsDetail> listGoodsDetailByPage(Map<String,Object> param);
}
