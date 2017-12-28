package com.zm.timetask.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.timetask.feignclient.model.OrderBussinessModel;
import com.zm.timetask.pojo.ResultModel;


@FeignClient("goodscenter")
public interface GoodsFeignClient {

	/**
	 * @fun 开启活动
	 * @param version
	 * @param activeId
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/active/start/{centerId}/{activeId}", method = RequestMethod.POST)
	public ResultModel startActive(@PathVariable("version") Double version, @PathVariable("activeId") String activeId,
			@PathVariable("centerId") Integer centerId);
	
	/**
	 * @fun 结束活动
	 * @param version
	 * @param activeId
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/active/end/{centerId}/{activeId}", method = RequestMethod.POST)
	public ResultModel endActive(@PathVariable("version") Double version, @PathVariable("activeId") Integer activeId,
			@PathVariable("centerId") Integer centerId);
	
	/**
	 * @fun 获取已经结束的活动
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/endactive", method = RequestMethod.GET)
	public ResultModel getEndActive(@PathVariable("version") Double version);
	
	/**
	 * @fun 获取需要同步的商品
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/checkStock", method = RequestMethod.GET)
	public List<OrderBussinessModel> checkStock(@PathVariable("version") Double version);
}
