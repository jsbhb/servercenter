package com.zm.user.bussiness.dao;

import com.github.pagehelper.Page;
import com.zm.user.pojo.po.PartnerPO;
import com.zm.user.pojo.po.ShopKeeperPO;

public interface CooperationMapper {

	Page<ShopKeeperPO> listShopKeeper();
	
	ShopKeeperPO getShopkeeperById(Integer id);
	
	Page<PartnerPO> listPartner();
	
	PartnerPO getPartnerById(Integer id);
	
	void saveShopKeeper(ShopKeeperPO shopKeeper);
	
	void savePartner(PartnerPO partner);
	
	void updatePartner(PartnerPO partner);
	
	void updateShopKeeper(ShopKeeperPO shopKeeper);
	
	void deleteShopkeeperById(Integer id);
	
	void deletePartnerById(Integer id);
	
	Page<ShopKeeperPO> selectShopKeeperForPage(ShopKeeperPO shopKeeper);
	
	Page<PartnerPO> selectPartnerForPage(PartnerPO partner);
}
