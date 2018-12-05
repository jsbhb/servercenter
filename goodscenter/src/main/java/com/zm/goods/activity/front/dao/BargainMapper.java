package com.zm.goods.activity.front.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.zm.goods.activity.model.ActiveGoods;
import com.zm.goods.activity.model.bargain.bo.BargainCountBO;
import com.zm.goods.activity.model.bargain.po.BargainRecordPO;
import com.zm.goods.activity.model.bargain.po.BargainRulePO;
import com.zm.goods.activity.model.bargain.po.UserBargainPO;

public interface BargainMapper {

	List<UserBargainPO> listBargainNormalByUserId(Integer userId);
	
	List<UserBargainPO> listBargainChainsByUserId(Integer userId);
	
	UserBargainPO getBargainDetailByParam(Map<String,Object> param);

	List<ActiveGoods> listActiceGoods(List<String> itemIdList);
	
	int getRuleTypeByUserBargainId(Integer id);

	Page<BargainRulePO> listBargainGoodsForPage();

	List<BargainCountBO> listBargainCount(List<Integer> idList);

	void updateUserBargainOver(Map<String, Object> param);

	BargainRulePO getBargainRuleById(Integer goodsRoleId);

	void saveUserBargain(UserBargainPO userBargainPO);

	void saveBargainRecord(BargainRecordPO po);

	UserBargainPO getUserBargainById(Integer id);

	void updateBargainGoodsBuy(Integer id);
}
