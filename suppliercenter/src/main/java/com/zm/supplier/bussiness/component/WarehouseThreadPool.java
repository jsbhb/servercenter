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
import com.zm.supplier.common.ResultModel;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.feignclient.GoodsFeignClient;
import com.zm.supplier.feignclient.OrderFeignClient;
import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.SupplierInterface;
import com.zm.supplier.pojo.ThirdOrderInfo;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.pojo.WarehouseStock;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ExpressContrast;
import com.zm.supplier.util.SpringContextUtil;
import com.zm.supplier.util.StatusContrast;
import com.zm.supplier.util.StatusTOChiness;

@Component
public class WarehouseThreadPool {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	SupplierMapper supplierMapper;

	Logger logger = LoggerFactory.getLogger(WarehouseThreadPool.class);
	
	@Async("myAsync")
	public void sendOrder(List<OrderInfo> infoList, Integer supplierId) {
		AbstractSupplierButtJoint buttJoint = getTargetInterface(supplierId);
		if (buttJoint == null) {
			return;
		}
		Set<SendOrderResult> set = buttJoint.sendOrder(infoList);
		if (set == null || set.size() == 0) {
			return;
		}
		orderFeignClient.saveThirdOrder(Constants.FIRST_VERSION, set);
	}
	
	public ResultModel sendOrderCancel(OrderInfo info) {
		ResultModel result = null;
		AbstractSupplierButtJoint buttJoint = getTargetInterface(info.getSupplierId());
		if (buttJoint == null) {
			result = new ResultModel(false, "为查询到供应商的配置信息");
		}
		Set<OrderCancelResult> set = buttJoint.orderCancel(info);
		if (set == null || set.size() == 0) {
			logger.info("订单号：" + info.getOrderId() + "===============调用退款接口失败==============");
			result = new ResultModel(false, "调用佳贝艾特退款接口失败");
		} else {
			logger.info("订单号：" + info.getOrderId() + "===============调用退款接口成功==============");
			result = new ResultModel(true, "调用佳贝艾特退款接口成功");
		}
		return result;
	}

	@Async("myAsync")
	public void checkOrderStatus(List<OrderIdAndSupplierId> orderList, Integer supplierId) {
		try {
			AbstractSupplierButtJoint buttJoint = getTargetInterface(supplierId);
			if (buttJoint == null) {
				return;
			}
			Set<OrderStatus> set = buttJoint.checkOrderStatus(orderList);
			if (set == null || set.size() == 0) {
				return;
			}
			// 设置本地订单号和供应商Id,如果没有第三方订单号则以本地订单号当第三方订单号
			for (OrderStatus orderStatus : set) {
				for (OrderIdAndSupplierId model : orderList) {
					if (model.getThirdOrderId().equals(orderStatus.getThirdOrderId())) {
						orderStatus.setOrderId(model.getOrderId());
						orderStatus.setSupplierId(supplierId);
					}
					if (orderStatus.getThirdOrderId() == null) {
						orderStatus.setThirdOrderId(model.getOrderId());
						orderStatus.setOrderId(model.getOrderId());
						orderStatus.setSupplierId(supplierId);
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
			logger.error("获取订单状态", e);
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

	@Async("myAsync")
	public void checkStockByAsync(List<OrderBussinessModel> list, Integer supplierId) {
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
		thirdOrder.setSupplierId(orderStatus.getSupplierId());
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
		thirdOrder.setItemCode(orderStatus.getItemCode());
		return thirdOrder;
	}

	private AbstractSupplierButtJoint getTargetInterface(Integer supplierId) {
		SupplierInterface inf = (SupplierInterface) template.opsForValue()
				.get(Constants.SUPPLIER_INTERFACE + supplierId);
		if (inf == null) {
			inf = supplierMapper.getSupplierInterface(supplierId);
			if (inf == null) {
				return null;
			}
			template.opsForValue().set(Constants.SUPPLIER_INTERFACE + supplierId, inf);
		}
		AbstractSupplierButtJoint buttJoint = (AbstractSupplierButtJoint) SpringContextUtil
				.getBean(inf.getTargetObject());
		if (buttJoint == null) {
			return null;
		}
		buttJoint.init(inf);
		return buttJoint;
	}
}
