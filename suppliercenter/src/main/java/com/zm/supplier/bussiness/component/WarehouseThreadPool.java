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

import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.feignclient.GoodsFeignClient;
import com.zm.supplier.feignclient.OrderFeignClient;
import com.zm.supplier.feignclient.UserFeignClient;
import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.SupplierInterface;
import com.zm.supplier.pojo.ThirdOrderInfo;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.pojo.WarehouseStock;
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

	@Resource
	GoodsFeignClient goodsFeignClient;
	
	@Resource
	SupplierMapper supplierMapper;

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
			if (model.getThirdOrderId() == null || "".equals(model.getThirdOrderId())) {
				model.setThirdOrderId(info.getOrderId());
			}
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
			if (set == null || set.size() == 0) {
				return;
			}
			// 设置本地订单号和供应商Id,如果没有第三方订单号则以本地订单号当第三方订单号
			for (OrderStatus orderStatus : set) {
				for (OrderIdAndSupplierId model : orderList) {
					if (orderStatus.getThirdOrderId().equals(model.getThirdOrderId())) {
						orderStatus.setOrderId(model.getOrderId());
						orderStatus.setSupplierId(supplierId);
						if (orderStatus.getThirdOrderId() == null) {
							orderStatus.setThirdOrderId(model.getOrderId());
						}
					}
				}
			}
			// end
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

	@Async("myAsync")
	public void getThirdGoods(String itemCode, Integer supplierId, String supplierName) {
		try {
			AbstractSupplierButtJoint buttJoint = getTargetInterface(supplierId);
			if (buttJoint == null) {
				return;
			}
			Set<ThirdWarehouseGoods> set = buttJoint.getGoods(itemCode);

			if (set != null && set.size() > 0) {
				for (ThirdWarehouseGoods model : set) {
					model.setSupplierId(supplierId);
					model.setSupplierName(supplierName);
					if (model.getQuantity() != null) {
						model.setStock(Integer.valueOf(model.getQuantity()));
					}
				}
				goodsFeignClient.saveThirdGoods(Constants.FIRST_VERSION, set);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void checkStock(List<OrderBussinessModel> list, Integer supplierId) {
		try {
			AbstractSupplierButtJoint buttJoint = getTargetInterface(supplierId);
			if (buttJoint == null) {
				return;
			}
			Set<CheckStockModel> set = buttJoint.checkStock(list);
			if (set == null || set.size() == 0) {
				return;
			}
			// 将ItemId赋值到返回的checkStockModel里面
			for (OrderBussinessModel model : list) {
				for (CheckStockModel tem : set) {
					if (model.getSku().equals(tem.getSku()) || model.getItemCode().equals(tem.getItemCode())) {
						tem.setItemId(model.getItemId());
					}
				}
			}
			List<WarehouseStock> stockList = new ArrayList<WarehouseStock>();
			WarehouseStock stock = null;
			for (CheckStockModel tem : set) {
				stock = toWarehouseStock(tem);
				stockList.add(stock);
			}
			goodsFeignClient.updateThirdWarehouseStock(Constants.FIRST_VERSION, stockList);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private WarehouseStock toWarehouseStock(CheckStockModel tem) {
		WarehouseStock stock = new WarehouseStock();
		stock.setItemId(tem.getItemId());
		stock.setQuantity(Integer.valueOf(tem.getQuantity()));
		return stock;
	}

	private ThirdOrderInfo toThirdOrder(OrderStatus orderStatus) {
		ThirdOrderInfo thirdOrder = new ThirdOrderInfo();
		thirdOrder.setExpressId(orderStatus.getExpressId());
		thirdOrder.setExpressKey(orderStatus.getLogisticsCode());
		if (orderStatus.getLogisticsCode() == null) {
			thirdOrder.setExpressName(ExpressContrast.get(orderStatus.getLogisticsName()));
		} else {
			thirdOrder.setExpressName(ExpressContrast.get(orderStatus.getLogisticsCode()));
		}
		thirdOrder.setRemark(orderStatus.getMsg());
		String CNStatus = StatusTOChiness.get(orderStatus.getSupplierId() + "-" + orderStatus.getStatus());
		thirdOrder.setStatus((CNStatus == null || "".equals(CNStatus)) ? orderStatus.getStatus() : CNStatus);
		thirdOrder.setOrderStatus(StatusContrast.get(orderStatus.getSupplierId() + "-" + orderStatus.getStatus()));
		thirdOrder.setThirdOrderId(orderStatus.getThirdOrderId());
		thirdOrder.setOrderId(orderStatus.getOrderId());
		return thirdOrder;
	}

	private AbstractSupplierButtJoint getTargetInterface(Integer supplierId) {
		SupplierInterface inf = (SupplierInterface) redisTemplate.opsForValue()
				.get(Constants.SUPPLIER_INTERFACE + supplierId);
		if(inf == null){
			inf = supplierMapper.getSupplierInterface(supplierId);
			if(inf == null){
				return null;
			}
			redisTemplate.opsForValue().set(Constants.SUPPLIER_INTERFACE + supplierId, inf);
		}
		AbstractSupplierButtJoint buttJoint = (AbstractSupplierButtJoint) SpringContextUtil
				.getBean(inf.getTargetObject());
		if(buttJoint == null){
			return null;
		}
		buttJoint.setAppKey(inf.getAppKey());
		buttJoint.setAppSecret(inf.getAppSecret());
		return buttJoint;
	}
}
