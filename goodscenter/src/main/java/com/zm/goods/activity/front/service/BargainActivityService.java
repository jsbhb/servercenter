package com.zm.goods.activity.front.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.goods.activity.model.bargain.vo.BargainGoods;
import com.zm.goods.activity.model.bargain.vo.MyBargain;
import com.zm.goods.exception.ActiviteyException;
import com.zm.goods.common.Pagination;

public interface BargainActivityService {

	List<MyBargain> listMyBargain(Integer userId);

	MyBargain getMyBargainDetail(Integer userId, int id) throws ActiviteyException;
	
	Page<BargainGoods> listBargainGoods(Pagination pagination);

	boolean userBargainOver(Integer userId, Integer id);

	Integer startBargain(Integer userId, Integer goodsRoleId) throws ActiviteyException;

}
