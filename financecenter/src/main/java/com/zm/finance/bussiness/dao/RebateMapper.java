package com.zm.finance.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.finance.pojo.RebateSearchModel;
import com.zm.finance.pojo.rebate.CenterRebate;
import com.zm.finance.pojo.rebate.PushUserRebate;
import com.zm.finance.pojo.rebate.Rebate;
import com.zm.finance.pojo.rebate.RebateDetail;
import com.zm.finance.pojo.rebate.ShopRebate;

public interface RebateMapper {

	void insertCenterRebate(List<CenterRebate> list);
	
	void insertShopRebate(List<ShopRebate> list);
	
	void insertPushUserRebate(List<PushUserRebate> list);
	
	void insertRebateDetail(List<RebateDetail> list);
	
	Page<RebateDetail> selectRebateDetailById(RebateDetail entity);

	Page<Rebate> listCenterRebate(RebateSearchModel search);
	
	Page<Rebate> listShopRebate(RebateSearchModel search);
	
	Page<Rebate> listPushRebate(RebateSearchModel search);
}
