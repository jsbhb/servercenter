package com.zm.goods.bussiness.service;

import java.util.List;

import com.zm.goods.pojo.ResultModel;

public interface GoodsOpenInterfaceService {

	ResultModel getGoodsStock(String data);

	ResultModel getGoodsDetail(String data);
	
	void sendGoodsInfo(List<String> itemIdList);
	
	void sendGoodsDownShelves(List<String> itemIdList);

}
