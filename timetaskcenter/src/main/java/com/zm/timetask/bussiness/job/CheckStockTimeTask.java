package com.zm.timetask.bussiness.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.GoodsFeignClient;
import com.zm.timetask.feignclient.SupplierFeignClient;
import com.zm.timetask.feignclient.model.OrderBussinessModel;

@Component
public class CheckStockTimeTask implements Job {

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	SupplierFeignClient supplierFeignClient;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<OrderBussinessModel> list = goodsFeignClient.checkStock(Constants.FIRST_VERSION);
		if (list != null && list.size() > 0) {
			Map<Integer, List<OrderBussinessModel>> temMap = new HashMap<Integer, List<OrderBussinessModel>>();
			List<OrderBussinessModel> temList = null;
			for (OrderBussinessModel model : list) {
				if (temMap.get(model.getSupplierId()) == null) {
					temList = new ArrayList<OrderBussinessModel>();
					temList.add(model);
					temMap.put(model.getSupplierId(), temList);
				} else {
					temMap.get(model.getSupplierId()).add(model);
				}
			}
			for (Map.Entry<Integer, List<OrderBussinessModel>> entry : temMap.entrySet()) {
				supplierFeignClient.checkStock(Constants.FIRST_VERSION, entry.getKey(), entry.getValue(), true);
			}
		}

	}

}
