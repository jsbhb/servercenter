package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.bo.ItemStockBO;
import com.zm.goods.pojo.po.PagePO;
import com.zm.goods.seo.model.SEOModel;

public interface SEOMapper {
	
	GoodsItem getGoods(Map<String, Object> param);

	String queryGoodsCategoryPath(String thirdCategory);
	
	List<String> listItemIdsByGoodsId(String goodsId);

	List<ItemStockBO> listStockByItemIds(Map<String,Object> param);
	
	void updateGoodsAccessPath(Map<String,Object> param);
	
	SEOModel getGoodsSEO(String goodsId);
	
	SEOModel getPageSEO(Integer id);
	
	String getGoodsAccessPath(String goodsId);
	
	List<PagePO> getGoodsDetailPageByPublish();
	
	PagePO getPageById(Integer id);

	void updatePagePublishToSave(PagePO page);
	
	void updatePageSaveToPublish(Integer id);
	
	String getGoodsIdByItemId(String itemId);

}
