package com.zm.supplier.bussiness.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zm.supplier.common.ResultModel;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.feignclient.OrderFeignClient;
import com.zm.supplier.feignclient.UserFeignClient;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.SupplierInterface;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.SpringContextUtil;

@Component
public class WarehouseThreadPool {

	@Resource
	RedisTemplate<String, Object> redisTemplate;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	OrderFeignClient orderFeignClient;

	Logger logger = LoggerFactory.getLogger(WarehouseThreadPool.class);

	@Async("myAsync")
	public void sendOrder(OrderInfo info) {
		AbstractSupplierButtJoint buttJoint = getTargetInterface(info.getSupplierId());
		UserInfo user = userFeignClient.getUser(Constants.FIRST_VERSION, info.getUserId());
		List<SendOrderResult> list = buttJoint.sendOrder(info, user);
		if(list == null){
			return;
		}
		for(SendOrderResult model : list){
			model.setSupplierId(info.getSupplierId());
		}
		ResultModel model = orderFeignClient.saveThirdOrder(Constants.FIRST_VERSION, list);
		if (model.isSuccess()) {
			redisTemplate.delete(info.getOrderId());
		} else {
			logger.info("订单号：" + info.getOrderId() + "===============发送成功，更新出错==============");
		}
	}

	@Async("myAsync")
	public List<OrderStatus> checkOrderStatus(List<OrderIdAndSupplierId> orderList, Integer supplierId,
			CountDownLatch countDownLatch) {
		try {
			AbstractSupplierButtJoint buttJoint = getTargetInterface(supplierId);
			List<String> orderIds = new ArrayList<String>();
			for(OrderIdAndSupplierId model : orderList){
				orderIds.add(model.getThirdOrderId());
			}
			List<OrderStatus> list = buttJoint.checkOrderStatus(orderIds);
			for(OrderStatus orderStatus : list){
				for(OrderIdAndSupplierId model : orderList){
					if(orderStatus.getThirdOrderId().equals(model.getThirdOrderId())){
						orderStatus.setOrderId(model.getOrderId());
					}
				}
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			countDownLatch.countDown();
		}
		
	}

	private AbstractSupplierButtJoint getTargetInterface(Integer supplierId) {
		SupplierInterface inf = (SupplierInterface) redisTemplate.opsForValue()
				.get(Constants.SUPPLIER_INTERFACE + supplierId);
		AbstractSupplierButtJoint buttJoint = (AbstractSupplierButtJoint) SpringContextUtil
				.getBean(inf.getTargetObject());
		buttJoint.setAppKey(inf.getAppKey());
		buttJoint.setAppSecret(inf.getAppSecret());
		return buttJoint;
	}
}
