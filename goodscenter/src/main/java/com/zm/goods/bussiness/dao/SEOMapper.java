package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.goods.pojo.po.BigSalesGoodsRecord;
import com.zm.goods.pojo.po.ComponentDataPO;
import com.zm.goods.pojo.po.PagePO;
import com.zm.goods.seo.model.CategoryPath;
import com.zm.goods.seo.model.SEOModel;

public interface SEOMapper {
	
	List<CategoryPath> queryGoodsCategoryPath(List<String> thirdCategoryList);
	
	void updateGoodsAccessPath(@Param("map") Map<String,Object> param);
	
	List<SEOModel> listGoodsSEO(List<String> goodsIdList);
	
	SEOModel getPageSEO(Integer id);
	
	String getGoodsAccessPath(String goodsId);
	
	List<PagePO> getGoodsDetailPageByPublish();
	
	PagePO getPageById(Integer id);

	void updatePagePublishToSave(PagePO page);
	
	void updatePageSaveToPublish(Integer id);
	
	String getGoodsIdByItemId(String itemId);

	List<PagePO> listPublishedIndexPageDetail();

	List<BigSalesGoodsRecord> listRecord(Map<String, Integer> param);

	void deleteByIdList(List<Integer> idList);

	void insertComponentDataBatch(List<ComponentDataPO> dataList);

}
