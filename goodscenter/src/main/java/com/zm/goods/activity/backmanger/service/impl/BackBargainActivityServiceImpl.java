package com.zm.goods.activity.backmanger.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.activity.backmanger.dao.BackBargainMapper;
import com.zm.goods.activity.backmanger.model.BargainActivityGoodsModel;
import com.zm.goods.activity.backmanger.model.BargainActivityModel;
import com.zm.goods.activity.backmanger.model.BargainActivityShowPageModel;
import com.zm.goods.activity.backmanger.model.BaseActivityModel;
import com.zm.goods.activity.backmanger.service.BackBargainActivityService;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class BackBargainActivityServiceImpl implements BackBargainActivityService {

	@Resource
	BackBargainMapper backBargainMapper;
	
	@Override
	public Page<BaseActivityModel> queryActivityForPage(BaseActivityModel model) {
		PageHelper.startPage(model.getCurrentPage(), model.getNumPerPage(), true);
		return backBargainMapper.selectActivityForPage(model);
	}
	
	@Override
	public void insertBargainActivityInfo(BargainActivityModel model) {
		backBargainMapper.insertBargainActivityInfo(model);
		backBargainMapper.insertBargainActivityGoodsInfo(model);
	}
	
	@Override
	public BargainActivityModel queryBargainActivityByParam(BargainActivityModel model) {
		return backBargainMapper.selectBargainActivityByParam(model);
	}
	
	@Override
	public void modifyBargainActivityInfo(BargainActivityModel model) {
		BargainActivityModel bargainInfo = backBargainMapper.selectBargainActivityByParam(model);

		List<BargainActivityGoodsModel> insList = new ArrayList<BargainActivityGoodsModel>();
		List<BargainActivityGoodsModel> updList = new ArrayList<BargainActivityGoodsModel>();
		//遍历既存商品信息,组合增删改数据
		boolean isNew = true;
		for (BargainActivityGoodsModel newBag:model.getItemList()) {
			isNew = true;
			for(BargainActivityGoodsModel oldBAG:bargainInfo.getItemList()) {
				if (oldBAG.getActivityId().equals(newBag.getActivityId()) && oldBAG.getItemId().equals(newBag.getItemId())) {
					newBag.setId(oldBAG.getId());
					newBag.setActivityId(oldBAG.getActivityId());
					newBag.setOpt(model.getOpt());
					updList.add(newBag);
					bargainInfo.getItemList().remove(oldBAG);
					isNew = false;
					break;
				}
			}
			if (isNew) {
				insList.add(newBag);
			}
		}
		model.setItemList(insList);
		//更新活动信息
		backBargainMapper.updateBargainActivityInfo(model);
		if (insList.size() > 0) {
			//插入新增商品
			backBargainMapper.insertBargainActivityGoodsInfo(model);
		}
		if (updList.size() > 0) {
			//更新已有商品
			backBargainMapper.updateBargainActivityGoodsInfo(updList);
		}
		if (bargainInfo.getItemList().size() > 0) {
			//删除移除商品
			backBargainMapper.deleteBargainActivityGoodsInfo(bargainInfo.getItemList());
		}
	}
	
	@Override
	public List<BargainActivityShowPageModel> queryBargainActivityShowPageInfo(BargainActivityModel model) {
		return backBargainMapper.selectBargainActivityShowPageInfo(model);
	}
}
