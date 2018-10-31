package com.zm.order.bussiness.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.OrderScheduleService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.FinanceFeignClient;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.bo.Rebate4Order;

@Service
public class OrderScheduleServiceImpl implements OrderScheduleService {

	@Resource
	FinanceFeignClient financeFeignClient;
	
	@Resource
	OrderMapper orderMapper;
	
	@Override
	public void saveRebateOrderToFinancecenter() {
		List<Rebate4Order> list = orderMapper.listRebate4Order();
		if(list != null && list.size() > 0){
			try {
				financeFeignClient.rebate4orderBatch(Constants.FIRST_VERSION, list);
			} catch (Exception e) {
				LogUtil.writeErrorLog("同步返佣订单到财务中心出错", e);
				return;
			}
			orderMapper.deleteRebate4Order(list);
		}
	}

}
