package com.zm.supplier.supplierinf.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderDetail;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.pojo.UserDetail;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.HttpClientUtil;

@Component
public class XinYunButtJoint extends AbstractSupplierButtJoint {

	private final String url = "http://120.76.191.121/api/service/business";// 测试
	// private final String url =
	// "http://apiserv.xyb2b.com/api/service/business";//正式

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user) {
		String msg = ButtJointMessageUtils.getXinYunOrderMsg(info, user, appKey, appSecret);
		return sendXinYunWarehouse(url, msg, SendOrderResult.class, info.getOrderId());
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		String msg = ButtJointMessageUtils.getXinYunOrderStatusMsg(orderIds.get(0), appKey, appSecret);
		return sendXinYunWarehouse(url, msg, OrderStatus.class, orderIds.get(0));
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		String msg = ButtJointMessageUtils.getXinYunStock(list, appKey, appSecret);
		Set<CheckStockModel> set = sendXinYunWarehouse(url, msg, CheckStockModel.class, list.get(0).getItemCode());
		CheckStockModel model = null;
		Set<CheckStockModel> result = new HashSet<CheckStockModel>();
		if (set == null || set.size() == 0) {// 如果空的，则都为0
			for (OrderBussinessModel temp : list) {
				model = new CheckStockModel();
				model.setItemCode(temp.getItemCode());
				model.setQuantity("0");
				result.add(model);
			}
			return result;
		}
		if (set.size() != list.size()) {// 如果数量不一致，找出没有的，设为0
			Map<String, CheckStockModel> tempMap = new HashMap<String, CheckStockModel>();
			for (CheckStockModel temp : set) {
				tempMap.put(temp.getItemCode(), temp);
			}
			for (OrderBussinessModel obmodel : list) {
				CheckStockModel csm = tempMap.get(obmodel.getItemCode());
				if (csm == null) {
					csm = new CheckStockModel();
					csm.setItemCode(obmodel.getItemCode());
					csm.setQuantity("0");
					set.add(csm);
				}
			}
			return set;
		}

		return set;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		String msg = ButtJointMessageUtils.getXinYunGoods(itemCode, appKey, appSecret);
		Set<ThirdWarehouseGoods> set = sendXinYunWarehouse(url, msg, ThirdWarehouseGoods.class, itemCode);
		for (ThirdWarehouseGoods model : set) {
			if (model.getRoughWeight() != null) {
				model.setWeight((Double.valueOf(model.getRoughWeight()).intValue()));
			}
		}
		return set;
	}

	private <T> Set<T> sendXinYunWarehouse(String url, String msg, Class<T> clazz, String param) {

		logger.info("发送报文：" + msg);
		String result = HttpClientUtil.post(url, msg, null);
		logger.info("返回：" + param + "=====" + result);

		if (msg.contains("get_goods_stock")) {
			result = result.replace("\"[", "[").replace("]\"", "]").replace("\\", "");
		}
		if (msg.contains("add_order")) {
			result = result.replace("order_no", "xyorder_no");
		}

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
		OrderInfo info = new OrderInfo();
		info.setOrderId("GX1001001212110411");
		info.setRemark("测试");
		OrderDetail detail = new OrderDetail();
		detail.setReceiveProvince("山东");
		detail.setReceiveCity("济宁市");
		detail.setReceiveArea("南山区");
		detail.setReceiveAddress("南山科技园海天一路4栋");
		detail.setReceivePhone("18949518599");
		detail.setReceiveZipCode("273100");
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		OrderGoods goods = new OrderGoods();
		goods.setItemCode("HJH04752-J");
		goods.setItemQuantity(2);
		list.add(goods);
		goods = new OrderGoods();
		goods.setItemCode("HJH00635-J");
		goods.setItemQuantity(5);
		list.add(goods);
		info.setOrderDetail(detail);
		info.setOrderGoodsList(list);
		UserInfo user = new UserInfo();
		UserDetail d = new UserDetail();
		d.setIdNum("530121197008214197");
		d.setName("李政");
		user.setUserDetail(d);
		System.out.println(joint.sendOrder(info, user));
		// 查询订单状态
		// List<String> list = new ArrayList<String>();
		// list.add("OA35125413030078985");
		// System.out.println(joint.checkOrderStatus(list));
		// 查询库存
		// OrderBussinessModel model = new OrderBussinessModel();
		// model.setItemCode("MBS07866-B");
		// model.setItemId("c00083");
		// List<OrderBussinessModel> list = new
		// ArrayList<OrderBussinessModel>();
		// list.add(model);
		// model = new OrderBussinessModel();
		// model.setItemCode("MBS04487-B");
		// model.setItemId("c00084");
		// list.add(model);
		// System.out.println(joint.checkStock(list));
		// System.out.println(joint.getGoods("MBS07866-B"));
	}

}
