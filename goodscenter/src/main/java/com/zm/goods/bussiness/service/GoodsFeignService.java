package com.zm.goods.bussiness.service;

import java.util.List;

import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.CustomCompletion;
import com.zm.goods.pojo.bo.GoodsItemBO;

public interface GoodsFeignService {

	ResultModel manualOrderGoodsCheck(List<GoodsItemBO> list);

	List<CustomCompletion> customCompletion(List<String> itemIdList);

	String getGoodsItemProxyPrice(List<String> itemIdList);

}
