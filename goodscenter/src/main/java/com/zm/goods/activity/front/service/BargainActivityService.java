package com.zm.goods.activity.front.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.goods.activity.model.bargain.vo.BargainGoods;
import com.zm.goods.activity.model.bargain.vo.MyBargain;
import com.zm.goods.exception.ActiviteyException;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.common.Pagination;

public interface BargainActivityService {

	List<MyBargain> listMyBargain(Integer userId);

	MyBargain getMyBargainDetail(Integer userId, int id) throws ActiviteyException;

	Page<BargainGoods> listBargainGoods(Pagination pagination,Integer userId);

	boolean userBargainOver(Integer userId, Integer id);

	Integer startBargain(Integer userId, Integer goodsRoleId) throws ActiviteyException;

	double bargain(Integer userId, Integer id) throws ActiviteyException;

	ResultModel getBargainGoodsInfo(List<OrderBussinessModel> list, Integer userId, Integer id);

	boolean updateBargainGoodsBuy(Integer userId, Integer id);

}
