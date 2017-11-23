package com.zm.supplier.bussiness.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zm.supplier.constants.Constants;
import com.zm.supplier.feignclient.OrderFeignClient;
import com.zm.supplier.feignclient.UserFeignClient;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.SupplierInterface;
import com.zm.supplier.pojo.ThirdOrderInfo;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ExpressContrast;
import com.zm.supplier.util.SpringContextUtil;
import com.zm.supplier.util.StatusContrast;
import com.zm.supplier.util.StatusTOChiness;

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
		if (buttJoint == null) {
			return;
		}
		UserInfo user = userFeignClient.getUser(Constants.FIRST_VERSION, info.getUserId());
		Set<SendOrderResult> set = buttJoint.sendOrder(info, user);
		if (set == null || set.size() == 0) {
			return;
		}
		for (SendOrderResult model : set) {
			model.setSupplierId(info.getSupplierId());
			model.setOrderId(info.getOrderId());
		}
		boolean flag = orderFeignClient.saveThirdOrder(Constants.FIRST_VERSION, set);
		if (flag) {
			redisTemplate.delete(info.getOrderId());
		} else {
			logger.info("订单号：" + info.getOrderId() + "===============发送成功，更新出错==============");
		}
	}

	@Async("myAsync")
	public void checkOrderStatus(List<OrderIdAndSupplierId> orderList, Integer supplierId) {
		try {
			AbstractSupplierButtJoint buttJoint = getTargetInterface(supplierId);
			if (buttJoint == null) {
				return;
			}
			List<String> orderIds = new ArrayList<String>();
			for (OrderIdAndSupplierId model : orderList) {
				orderIds.add(model.getThirdOrderId());
			}
			Set<OrderStatus> set = buttJoint.checkOrderStatus(orderIds);
			for (OrderStatus orderStatus : set) {
				for (OrderIdAndSupplierId model : orderList) {
					if (orderStatus.getThirdOrderId().equals(model.getThirdOrderId())) {
						orderStatus.setOrderId(model.getOrderId());
						orderStatus.setSupplierId(supplierId);
					}
				}
			}
			List<ThirdOrderInfo> list = new ArrayList<ThirdOrderInfo>();
			ThirdOrderInfo thirdOrder = null;
			for (OrderStatus orderStatus : set) {
				thirdOrder = toThirdOrder(orderStatus);
				list.add(thirdOrder);
			}
			orderFeignClient.changeOrderStatusByThirdWarehouse(Constants.FIRST_VERSION, list);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	private ThirdOrderInfo toThirdOrder(OrderStatus orderStatus) {
		ThirdOrderInfo thirdOrder = new ThirdOrderInfo();
		thirdOrder.setExpressId(orderStatus.getExpressId());
		thirdOrder.setExpressKey(orderStatus.getLogisticsCode());
		thirdOrder.setExpressName(ExpressContrast.get(orderStatus.getLogisticsCode()));
		String CNStatus = StatusTOChiness.get(orderStatus.getSupplierId() + orderStatus.getStatus());
		thirdOrder.setStatus(CNStatus == null ? orderStatus.getStatus() : CNStatus);
		thirdOrder.setOrderStatus(StatusContrast.get(orderStatus.getSupplierId() + orderStatus.getStatus()));
		thirdOrder.setThirdOrderId(orderStatus.getThirdOrderId());
		thirdOrder.setOrderId(orderStatus.getOrderId());
		return thirdOrder;
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
