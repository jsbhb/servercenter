package com.zm.supplier.bussiness.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.custominf.AbstractCustom;
import com.zm.supplier.custominf.model.CustomConfig;
import com.zm.supplier.feignclient.GoodsFeignClient;
import com.zm.supplier.feignclient.OrderFeignClient;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.ThirdOrderInfo;
import com.zm.supplier.pojo.bo.CustomCompletion;
import com.zm.supplier.pojo.callback.ReceiveLogisticsCompany;
import com.zm.supplier.util.ConvertUtil;
import com.zm.supplier.util.SpringContextUtil;

@Component
public class CustomThreadPool {

	@Resource
	SupplierMapper supplierMapper;

	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Async("myAsync")
	public void customOrder(ReceiveLogisticsCompany receiveLogisticsCompany) {
		OrderInfo order = orderFeignClient.handleSupplierCenterExceptionOrder(Constants.FIRST_VERSION,
				receiveLogisticsCompany.getOrderNo());
		//TODO 如何动态确定海关属地
		AbstractCustom buttJoint = getTargetInterface(1);// 目前写死1、代表杭州海关
		if (buttJoint == null || order == null) {
			return;
		}
		// 补全hscode和原产国
		completeGoodsDetailInfo(order);
		//属地订单申报
		Set<OrderStatus> set = buttJoint.orderCustom(order, receiveLogisticsCompany);
		if (set == null || set.size() == 0) {
			return;
		}
		List<ThirdOrderInfo> list = new ArrayList<ThirdOrderInfo>();
		ThirdOrderInfo thirdOrder = null;
		for (OrderStatus orderStatus : set) {
			thirdOrder = ConvertUtil.toThirdOrder(orderStatus);
			list.add(thirdOrder);
		}
		orderFeignClient.changeOrderStatusByThirdWarehouse(Constants.FIRST_VERSION, list);
	}
	/**
	 * @fun 加签并发送总署
	 * @param order
	 * @param receiveLogisticsCompany
	 */
	public void addSignatureAndZsCustoms(OrderInfo order, ReceiveLogisticsCompany receiveLogisticsCompany){
		//TODO 如何动态确定海关属地
		AbstractCustom buttJoint = getTargetInterface(1);// 目前写死1、代表杭州海关
		if (buttJoint == null) {
			return;
		}
		// 补全hscode和原产国
		completeGoodsDetailInfo(order);
		//加签并发送总署
		Set<OrderStatus> set = buttJoint.addSignatureAndZsCustoms(order, receiveLogisticsCompany);
		if (set == null || set.size() == 0) {
			return;
		}
		List<ThirdOrderInfo> list = new ArrayList<ThirdOrderInfo>();
		ThirdOrderInfo thirdOrder = null;
		for (OrderStatus orderStatus : set) {
			thirdOrder = ConvertUtil.toThirdOrder(orderStatus);
			list.add(thirdOrder);
		}
		orderFeignClient.changeOrderStatusByThirdWarehouse(Constants.FIRST_VERSION, list);
	}

	private void completeGoodsDetailInfo(OrderInfo order) {
		List<String> itemIdList = order.getOrderGoodsList().stream().map(g -> g.getItemId())
				.collect(Collectors.toList());
		List<CustomCompletion> ccList = goodsFeignClient.customCompletion(Constants.FIRST_VERSION, itemIdList);
		Map<String, List<CustomCompletion>> map = ccList.stream()
				.collect(Collectors.groupingBy(CustomCompletion::getItemId));
		order.getOrderGoodsList().stream().forEach(g->{
			g.setHsCode(map.get(g.getItemId()).get(0).getHscode());
			g.setOrigin(map.get(g.getItemId()).get(0).getOrigin());
			g.setBrand(map.get(g.getItemId()).get(0).getBrand());
		});
	}

	private AbstractCustom getTargetInterface(Integer id) {
		CustomConfig config = supplierMapper.getCustomConfig(id);
		if (config == null) {
			return null;
		}
		AbstractCustom buttJoint = (AbstractCustom) SpringContextUtil.getBean(config.getTargetObject());
		if (buttJoint == null) {
			return null;
		}
		buttJoint.init(config);
		return buttJoint;
	}
}
