package com.zm.goods.seo.service;

import java.util.List;

import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.ItemStockBO;

public interface SEOService {

	List<ItemStockBO> getGoodsStock(String goodsId, Integer centerId);

	ResultModel publish(List<String> itemIdList, Integer centerId);

	ResultModel navPublish();

	ResultModel delPublish(List<String> itemIdList);

	ResultModel indexPublish(Integer id);

	ResultModel getGoodsAccessPath(String goodsId, String itemId);
}
