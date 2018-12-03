package com.zm.goods.activity.front.service;

import java.util.List;

import com.zm.goods.activity.model.bargain.vo.MyBargain;

public interface BargainActivityService {

	List<MyBargain> listMyBargain(Integer userId);

	MyBargain getMyBargainDetail(Integer userId, int id);

}
