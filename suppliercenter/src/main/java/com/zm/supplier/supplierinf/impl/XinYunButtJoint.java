package com.zm.supplier.supplierinf.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.HttpClientUtil;

@Component
public class XinYunButtJoint extends AbstractSupplierButtJoint {

	private final String url = "http://120.76.191.121/api/service/business";

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user) {
		String msg = ButtJointMessageUtils.getXinYunOrderMsg(info, user, appKey, appSecret);
		return sendXinYunWarehouse(url, msg, SendOrderResult.class);
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		String msg = ButtJointMessageUtils.getXinYunOrderStatusMsg(orderIds.get(0), appKey, appSecret);
		return sendXinYunWarehouse(url, msg, OrderStatus.class);
	}

	@Override
	public Set<CheckStockModel> checkStock(List<CheckStockModel> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private <T> Set<T> sendXinYunWarehouse(String url, String msg, Class<T> clazz){
		
		logger.info("发送报文：" + msg);
		String result = HttpClientUtil.post(url, msg);
		logger.info("返回：" + result);
		
		try {
			return renderResult(result, "JSON", clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		XinYunButtJoint joint = new XinYunButtJoint();
		joint.setAppKey("75041628");
		joint.setAppSecret("1BG847AC10007300A8C000000F634ED6");
//		OrderInfo info = new OrderInfo();
//		info.setOrderId("GX10010012121102");
//		info.setRemark("测试");
//		OrderDetail detail = new OrderDetail();
//		detail.setReceiveProvince("山东");
//		detail.setReceiveCity("济宁市");
//		detail.setReceiveArea("南山区");
//		detail.setReceiveAddress("南山科技园海天一路4栋");
//		detail.setReceivePhone("18949518599");
//		detail.setReceiveZipCode("273100");
//		List<OrderGoods> list = new ArrayList<OrderGoods>();
//		OrderGoods goods = new OrderGoods();
//		goods.setItemCode("MBS07866-B");
//		goods.setItemQuantity(2);
//		list.add(goods);
//		goods = new OrderGoods();
//		goods.setItemCode("MBS04487-B");
//		goods.setItemQuantity(5);
//		list.add(goods);
//		info.setOrderDetail(detail);
//		info.setOrderGoodsList(list);
//		UserInfo user = new UserInfo();
//		UserDetail d = new UserDetail();
//		d.setIdNum("530121197008214197");
//		d.setName("李政");
//		user.setUserDetail(d);
//		System.out.println(joint.sendOrder(info, user));
		List<String> list = new ArrayList<String>();
		list.add("OA35114226249246197");
		System.out.println(joint.checkOrderStatus(list));
	}

}
