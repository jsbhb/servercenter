package com.zm.timetask.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.timetask.feignclient.model.OrderBussinessModel;
import com.zm.timetask.feignclient.model.OrderIdAndSupplierId;
import com.zm.timetask.feignclient.model.OrderInfo;
import com.zm.timetask.pojo.ResultModel;

@FeignClient("suppliercenter")
public interface SupplierFeignClient {

	@RequestMapping(value = "{version}/supplier/sendOrder", method = RequestMethod.POST)
	public ResultModel sendOrder(@PathVariable("version") Double version, @RequestBody List<OrderInfo> infoList);
	
	@RequestMapping(value = "{version}/supplier/checkOrderStatus", method = RequestMethod.POST)
	public ResultModel checkOrderStatus(@PathVariable("version") Double version,
			@RequestBody List<OrderIdAndSupplierId> list);
	
	@RequestMapping(value = "{version}/supplier/checkStock/{supplierId}", method = RequestMethod.POST)
	public ResultModel checkStock(@PathVariable("version") Double version, @PathVariable("supplierId") Integer supplierId,
			@RequestBody List<OrderBussinessModel> list);
}
