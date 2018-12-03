package com.zm.goods.activity.front.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.activity.component.BargainEntityConverter;
import com.zm.goods.activity.front.dao.BargainMapper;
import com.zm.goods.activity.front.service.BargainActivityService;
import com.zm.goods.activity.model.bargain.po.UserBargainPO;
import com.zm.goods.activity.model.bargain.vo.MyBargain;

@Service
public class BargainActivityServiceImpl implements BargainActivityService{

	@Resource
	BargainMapper bargainMapper;
	
	@Override
	public List<MyBargain> listMyBargain(Integer userId) {
		List<UserBargainPO> bargainList = bargainMapper.listBargainByUserId(userId);
		//创建砍价活动转换器
		BargainEntityConverter convert = new BargainEntityConverter();
		//获取前端展示需要的对象
		List<MyBargain> myBargainList = convert.userBargainPO2MyBargain(bargainList);
		//获取商品信息
		
		return null;
	}

	@Override
	public MyBargain getMyBargainDetail(Integer userId, int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
