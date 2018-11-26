package com.zm.order.bussiness.component;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.FinanceFeignClient;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.bo.Rebate4Order;

@Component
public class ThreadPoolComponent {

	@Resource
	ShareProfitComponent shareProfitComponent;
	
	@Resource
	FinanceFeignClient financeFeignClient;
	
	@Resource
	OrderMapper orderMapper;
	
	@Async("myAsync")
	public void calShareProfitStayToAccount(String orderId){
		shareProfitComponent.calShareProfitStayToAccount(orderId);
	}
	
	@Async("myAsync")
	public void capitalPoolRecount(List<OrderInfo> list){
		if(list != null && list.size() > 0){
			for(OrderInfo info : list){
				shareProfitComponent.calcapitalpool(info.getOrderId());
			}
		}
	}
	
	@Async("myAsync")
	public void rebate4Order(OrderInfo info){
		Double rebateFee = info.getOrderDetail().getRebateFee();
		if(rebateFee != null && rebateFee > 0){
			Rebate4Order rebate4Order = new Rebate4Order();
			rebate4Order.setGradeId(info.getShopId());
			rebate4Order.setMoney(rebateFee);
			rebate4Order.setOrderId(info.getOrderId());
			ResultModel model;
			try {
				model = financeFeignClient.rebate4order(Constants.FIRST_VERSION, rebate4Order);
			} catch (Exception e) {
				orderMapper.insertRebateConsume(rebate4Order);
				return;
			}
			if(!model.isSuccess()){
				orderMapper.insertRebateConsume(rebate4Order);
			}
		}
	}
}
