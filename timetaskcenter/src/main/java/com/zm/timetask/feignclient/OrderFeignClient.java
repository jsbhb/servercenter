package com.zm.timetask.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.timetask.pojo.ResultModel;

@FeignClient("ordercenter")
public interface OrderFeignClient {

	/**
	 * @fun 超时关闭订单
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/order/close", method = RequestMethod.GET)
	public ResultModel timeTaskcloseOrder(@PathVariable("version") Double version);

	/**
	 * @fun 获取需要支付报关的订单
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/order/paycustom", method = RequestMethod.GET)
	public ResultModel payCustom(@PathVariable("version") Double version);

	/**
	 * @fun 修改订单状态为支付报关
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/order/paycustom/{orderId}", method = RequestMethod.POST)
	public ResultModel updatePayCustom(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId);

	/**
	 * @fun 获取可以发送仓库的订单
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/order/sendToWarehouse", method = RequestMethod.GET)
	public ResultModel sendToWarehouse(@PathVariable("version") Double version);

	/**
	 * @fun 定时确认收货
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/order/confirmByTimeTask", method = RequestMethod.GET)
	public void confirmByTimeTask(@PathVariable("version") Double version);

	/**
	 * @fun 获取需要同步状态的订单
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/order/listUnDeliverOrder", method = RequestMethod.GET)
	public ResultModel listUnDeliverOrder(@PathVariable("version") Double version);

	/**
	 * @fun 轮询查询资金池不足的订单并重新计算
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/order/capitalpool/notenough", method = RequestMethod.POST)
	public boolean capitalPoolRecount(@PathVariable("version") Double version);

	@RequestMapping(value = "{version}/cache/day", method = RequestMethod.GET)
	public void saveDayCacheToWeek(@PathVariable("version") Double version);

	@RequestMapping(value = "{version}/cache/month", method = RequestMethod.GET)
	public void initMonth(@PathVariable("version") Double version);
	
	@RequestMapping(value = "{version}/rebateorder/finance/schedule", method = RequestMethod.GET)
	public void saveRebateOrderToFinancecenter(@PathVariable("version") Double version);
}
