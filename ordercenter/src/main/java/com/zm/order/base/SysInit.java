package com.zm.order.base;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.component.CacheComponent;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderGoodsCacheModel;
import com.zm.order.pojo.bo.GradeBO;
import com.zm.order.utils.DateUtils;

@Component
public class SysInit {

	@Resource
	UserFeignClient userFeignClient;
	
	@Resource
	CacheAbstractService cacheAbstractService;
	
	@Resource
	OrderMapper orderMapper;
	
	@PostConstruct
	public void init(){
		loadGradeBO();
		
		initGoodsOrderNum();
		
//		cacheAbstractService.initCache();
	}
	
	private void initGoodsOrderNum() {
		List<OrderGoods> goodsList = orderMapper.listOrderGoodsByCreateTime(DateUtils.getTimeString("yyyy-MM-dd"));
		OrderGoodsCacheModel ogc = null;
		for (OrderGoods og : goodsList) {
			ogc = new OrderGoodsCacheModel();
			ogc.setGoodsName(og.getItemName());
			ogc.setOrderNum(new AtomicInteger(1));
			com.zm.order.bussiness.component.CacheComponent.putOrderGoodsCache(og.getGoodsId(), ogc);
		}
	}

	private void loadGradeBO(){
		List<GradeBO> list = userFeignClient.listGradeBO(Constants.FIRST_VERSION);
		if(list != null && list.size() > 0){
			for(GradeBO grade : list){
				CacheComponent.getInstance().addGrade(grade);
			}
		}
	} 
}
