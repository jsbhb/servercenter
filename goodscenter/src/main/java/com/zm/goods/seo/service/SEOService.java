package com.zm.goods.seo.service;

import java.util.List;

import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.ItemStockBO;

public interface SEOService {

	List<ItemStockBO> getGoodsStock(String goodsId, Integer centerId);

	ResultModel publish(List<String> itemIdList, Integer centerId, boolean isNewPublish);

	ResultModel navPublish();

	ResultModel delPublish(List<String> itemIdList, Integer centerId);

	ResultModel indexPublish(Integer id);

	ResultModel getGoodsAccessPath(String goodsId, String itemId);

	ResultModel publishByGoodsId(List<String> goodsIdList, Integer centerId, boolean isNewPublish);

	ResultModel delPublishByGoodsId(List<String> goodsIdList, Integer centerId);
}
