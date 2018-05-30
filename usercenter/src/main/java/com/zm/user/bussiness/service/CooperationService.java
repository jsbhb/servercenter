package com.zm.user.bussiness.service;

import com.zm.user.common.Pagination;
import com.zm.user.common.ResultModel;
import com.zm.user.pojo.po.PartnerPO;
import com.zm.user.pojo.po.ShopKeeperPO;

public interface CooperationService {

	ResultModel listShopkeeper(Pagination pagination);

	ResultModel listPartner(Pagination pagination);

	ResultModel savePartner(PartnerPO po);

	ResultModel saveShopkeeper(ShopKeeperPO po);

	ResultModel updateShopkeeper(ShopKeeperPO po);

	ResultModel updatePartner(PartnerPO po);

	ResultModel deleteShopkeeper(Integer id);

	ResultModel deletePartner(Integer id);

	ResultModel getShopkeeperBack(Integer id);

	ResultModel getPartnerBack(Integer id);

	ResultModel listShopkeeperBack(ShopKeeperPO po);

	ResultModel listPartnerBack(PartnerPO po);

}
