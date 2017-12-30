package com.zm.pay.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.pay.feignclient.model.OrderDetail;
import com.zm.pay.feignclient.model.OrderInfo;
import com.zm.pay.pojo.ResultModel;

@FeignClient("ordercenter")
public interface OrderFeignClient {

	/**
	 * @fun 根据订单号获取centerID
	 * @param orderId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/order/getClientId/{orderId}", method = RequestMethod.GET)
	Integer getClientIdByOrderId(@PathVariable("orderId") String orderId, @PathVariable("version") Double version);

	/**
	 * @fun 支付成功后更新订单
	 * @param version
	 * @param orderId
	 * @param payNo
	 * @return
	 */
	@RequestMapping(value = "{version}/order/alread-pay/{orderId}", method = RequestMethod.PUT)
	public ResultModel updateOrderPayStatusByOrderId(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId, @RequestParam("payNo") String payNo);

	/**
	 * @fun 支付时根据订单号获取订单信息（金额已经在第一次保存时进行判断）
	 * @param version
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "{version}/order/pay/{orderId}", method = RequestMethod.GET)
	public OrderInfo getOrderByOrderIdForPay(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId);

	/**
	 * @fun 支付方式如果不同需要更新支付方式
	 * @param version
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "{version}/order/payType", method = RequestMethod.POST)
	public boolean updateOrderPayType(@PathVariable("version") Double version, @RequestBody OrderDetail detail);

	/**
	 * @fun 超时关闭订单
	 * @param version
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "{version}/order/close/{userId}/{orderId}", method = RequestMethod.POST)
	public ResultModel closeOrder(@PathVariable("version") Double version, @PathVariable("orderId") String orderId,
			@PathVariable("userId") Integer userId);

}
