package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.bussiness.service.OrderFeignService;
import com.zm.order.component.CacheComponent;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.bo.CustomOrderReturn;
import com.zm.order.pojo.bo.GoodsInfo;
import com.zm.order.pojo.bo.GradeBO;
import com.zm.order.utils.JSONUtil;

@Service
public class OrderFeignServiceImpl implements OrderFeignService {

	@Resource
	CacheAbstractService cacheAbstractService;

	@Resource
	OrderMapper orderMapper;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	UserFeignClient userFeignClient;

	@Override
	public void syncGrade(GradeBO grade) {
		LogUtil.writeLog("开始同步grade====" + CacheComponent.getInstance().getSet().toString());
		int startSize = CacheComponent.getInstance().getSet().size();
		// 加入缓存
		CacheComponent.getInstance().addGrade(grade);
		int endSize = CacheComponent.getInstance().getSet().size();
		if (endSize > startSize) {
			// 初始化统计缓存
			cacheAbstractService.initNewGradeCache(grade.getId());
		}
		LogUtil.writeLog("同步grade结束====" + CacheComponent.getInstance().getSet().toString());
	}

	@Override
	public void saveCustomOrderExpress(CustomOrderReturn orderExpress) {
		orderMapper.saveCustomOrderExpress(orderExpress);
	}

	@Override
	public String listOrderGoodsInfo(String orderId) {
		List<OrderGoods> list = orderMapper.listOrderGoodsByOrderId(orderId);
		List<GoodsInfo> infoList = new ArrayList<>();
		GoodsInfo info = null;
		String url = userFeignClient.getClientUrl(2, Constants.FIRST_VERSION);
		for (OrderGoods goods : list) {
			info = new GoodsInfo();
			Object obj = template.opsForValue().get("href:" + goods.getGoodsId());
			String href = obj == null ? "" : obj.toString();
			info.setItemLink(url + href);
			info.setGname(goods.getItemName());
			infoList.add(info);
		}
		return JSONUtil.toJson(infoList);
	}

	@Override
	public String getOrderDetail(String orderId) {
		Map<String, String> result = orderMapper.getOrderDetailByOrderId(orderId);
		return JSONUtil.toJson(result);
	}

	@Override
	public String getOrderExpressDetail(String orderId) {
		return orderMapper.getOrderExpressDetail(orderId);
	}

}
