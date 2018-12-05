package com.zm.goods.activity.backmanger.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.goods.activity.backmanger.model.BargainActivityGoodsModel;
import com.zm.goods.activity.backmanger.model.BargainActivityModel;
import com.zm.goods.activity.backmanger.model.BaseActivityModel;

public interface BackBargainMapper {

	Page<BaseActivityModel> selectActivityForPage(BaseActivityModel model);
	
	void insertBargainActivityInfo(BargainActivityModel model);
	
	void insertBargainActivityGoodsInfo(BargainActivityModel model);
	
	BargainActivityModel selectBargainActivityByParam(BargainActivityModel model);
	
	void updateBargainActivityInfo(BargainActivityModel model);
	
	void updateBargainActivityGoodsInfo(List<BargainActivityGoodsModel> list);
	
	void deleteBargainActivityGoodsInfo(List<BargainActivityGoodsModel> list);
}
