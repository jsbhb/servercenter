package com.zm.goods.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.ComponentData;
import com.zm.goods.pojo.ComponentPage;
import com.zm.goods.pojo.po.BigSalesGoodsRecord;

public interface MallMapper {


	Page<ComponentPage> selectComponentForPage(ComponentPage entity);
	
	List<ComponentData> selectComponentDataByPageId(String pageId);
	
	void updateComponentData(ComponentData data);
	
	void insertBigSaleDataa(List<BigSalesGoodsRecord> list);
	
	void updateBigSaleData(List<BigSalesGoodsRecord> list);
	
	List<BigSalesGoodsRecord> selectBigSaleData();
}
