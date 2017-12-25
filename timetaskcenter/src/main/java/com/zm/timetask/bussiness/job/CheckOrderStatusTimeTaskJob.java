package com.zm.timetask.bussiness.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.OrderFeignClient;
import com.zm.timetask.feignclient.SupplierFeignClient;
import com.zm.timetask.feignclient.model.OrderIdAndSupplierId;
import com.zm.timetask.pojo.ResultModel;
import com.zm.timetask.util.JSONUtil;

@Component
public class CheckOrderStatusTimeTaskJob implements Job{

	@Resource
	OrderFeignClient orderFeignClient;
	
	@Resource
	SupplierFeignClient supplierFeignClient;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		ResultModel result = orderFeignClient.listUnDeliverOrder(Constants.FIRST_VERSION);
		if(result.isSuccess()){
			List<Map<String, Object>> list = (List<Map<String, Object>>) result.getObj();
			if(list != null){
				List<OrderIdAndSupplierId> infoList = new ArrayList<OrderIdAndSupplierId>();
				OrderIdAndSupplierId info = null;
				for(Map<String, Object> map : list){
					info = JSONUtil.parse(JSONUtil.toJson(map), OrderIdAndSupplierId.class);
					infoList.add(info);
				}
				supplierFeignClient.checkOrderStatus(Constants.FIRST_VERSION, infoList);
			}
		} 
		
	}

}
