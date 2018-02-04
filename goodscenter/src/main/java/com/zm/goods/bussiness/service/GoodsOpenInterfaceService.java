package com.zm.goods.bussiness.service;

import java.util.Map;

import com.zm.goods.pojo.ResultModel;

public interface GoodsOpenInterfaceService {

	ResultModel getGoodsStock(String itemId);

	ResultModel getGoodsDetail(Map<String, String> param);

}
