package com.zm.timetask.bussiness.job;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.OrderFeignClient;
import com.zm.timetask.feignclient.PayFeignClient;
import com.zm.timetask.pojo.CustomModel;
import com.zm.timetask.pojo.ResultModel;
import com.zm.timetask.util.JSONUtil;

@Component
public class PayCustomTimeTaskJob implements Job{

	@Resource
	OrderFeignClient orderFeignClient;
	
	@Resource
	PayFeignClient payFeignClient;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		ResultModel result = orderFeignClient.payCustom(Constants.FIRST_VERSION);
		if(result.isSuccess()){
			List<Map<String, Object>> list =  (List<Map<String, Object>>) result.getObj();
			if(list != null && list.size() > 0){
				CustomModel model = null;
				for(Map<String, Object> map : list){
					model = JSONUtil.parse(JSONUtil.toJson(map), CustomModel.class);
					try {
						boolean flag = payFeignClient.payCustom(model);
						if(flag){
							orderFeignClient.updatePayCustom(Constants.FIRST_VERSION, model.getOutRequestNo());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
