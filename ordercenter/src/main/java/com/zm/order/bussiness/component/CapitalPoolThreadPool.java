package com.zm.order.bussiness.component;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.pojo.OrderInfo;

@Component
public class CapitalPoolThreadPool {

	@Resource
	OrderMapper orderMapper;
	
	@Resource
	ShareProfitComponent shareProfitComponent;
	
	@Async("myAsync")
	public void capitalPoolRecount(List<OrderInfo> list){
		if(list != null && list.size() > 0){
			for(OrderInfo info : list){
				shareProfitComponent.calcapitalpool(info.getOrderId());
			}
		}
	}
}
