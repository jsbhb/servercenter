package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.bo.ItemStockBO;
import com.zm.goods.pojo.po.PagePO;
import com.zm.goods.seo.model.CategoryPath;
import com.zm.goods.seo.model.GoodsTempModel;
import com.zm.goods.seo.model.SEOModel;

public interface SEOMapper {
	
	List<GoodsItem> listGoods(List<String> goodsIdList);

	List<CategoryPath> queryGoodsCategoryPath(List<String> thirdCategoryList);
	
	List<String> listItemIdsByGoodsId(String goodsId);

	List<ItemStockBO> listStockByItemIds(List<String> itemIds);
	
	void updateGoodsAccessPath(@Param("map") Map<String,Object> param);
	
	List<SEOModel> listGoodsSEO(List<String> goodsIdList);
	
	SEOModel getPageSEO(Integer id);
	
	String getGoodsAccessPath(String goodsId);
	
	List<PagePO> getGoodsDetailPageByPublish();
	
	PagePO getPageById(Integer id);

	void updatePagePublishToSave(PagePO page);
	
	void updatePageSaveToPublish(Integer id);
	
	String getGoodsIdByItemId(String itemId);

	void updateGoodsPublishByGoodsId(List<String> goodsIdList);

	void updateGoodsDelPublishByGoodsId(String goodsId);
	
	void updateGoodsRePublishByGoodsId(List<String> goodsIdList);
	
	List<GoodsTempModel> getDownShelvesGoodsIdByGoodsId(List<String> goodsIdList);

}
