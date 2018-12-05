package com.zm.goods.activity.backmanger.service;

import com.github.pagehelper.Page;
import com.zm.goods.activity.backmanger.model.BargainActivityModel;
import com.zm.goods.activity.backmanger.model.BaseActivityModel;

public interface BackBargainActivityService {

	Page<BaseActivityModel> queryActivityForPage(BaseActivityModel model);
	
	void insertBargainActivityInfo(BargainActivityModel model);
	
	BargainActivityModel queryBargainActivityByParam(BargainActivityModel model);
	
	void modifyBargainActivityInfo(BargainActivityModel model);
}
