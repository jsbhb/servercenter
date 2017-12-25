package com.zm.timetask.bussiness.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.OrderFeignClient;
import com.zm.timetask.feignclient.SupplierFeignClient;
import com.zm.timetask.feignclient.model.OrderInfo;
import com.zm.timetask.pojo.ResultModel;
import com.zm.timetask.util.JSONUtil;

@Component
public class SendOrderTimeTaskJob implements Job{

	@Resource
	OrderFeignClient orderFeignClient;
	
	@Resource
	SupplierFeignClient supplierFeignClient;
	
	@Resource
	RedisTemplate<String, Object> redisTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		ResultModel result = orderFeignClient.alreadyPay(Constants.FIRST_VERSION);
		if(result.isSuccess()){
			List<Map<String, Object>> list = (List<Map<String, Object>>) result.getObj();
			if(list != null){
				List<OrderInfo> infoList = new ArrayList<OrderInfo>();
				OrderInfo info = null;
				for(Map<String, Object> map : list){
					redisTemplate.opsForValue().set(map.get("orderId").toString(), true);
					info = JSONUtil.parse(JSONUtil.toJson(map), OrderInfo.class);
					infoList.add(info);
				}
				supplierFeignClient.sendOrder(Constants.FIRST_VERSION, infoList);
			}
		} 
	}
}
