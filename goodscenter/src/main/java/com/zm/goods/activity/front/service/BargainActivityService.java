package com.zm.goods.activity.front.service;

import java.util.List;
import java.util.Map;

import com.zm.goods.activity.model.bargain.dto.BargainInfoDTO;
import com.zm.goods.activity.model.bargain.vo.MyBargain;
import com.zm.goods.exception.ActiviteyException;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.DealOrderDataBO;

public interface BargainActivityService {

	List<MyBargain> listMyBargain(Integer userId, Integer start);

	MyBargain getMyBargainDetail(int id) throws ActiviteyException;

	Map<String, Object> listBargainGoods(Integer userId);

	boolean userBargainOver(Integer userId, Integer id);

	Integer startBargain(BargainInfoDTO dto) throws ActiviteyException;

	double bargain(BargainInfoDTO dto) throws ActiviteyException;

	ResultModel getBargainGoodsInfo(DealOrderDataBO bo, Integer id);

	boolean updateBargainGoodsBuy(Integer userId, Integer id);

	ResultModel bargainRetry(BargainInfoDTO dto) throws ActiviteyException;

}
