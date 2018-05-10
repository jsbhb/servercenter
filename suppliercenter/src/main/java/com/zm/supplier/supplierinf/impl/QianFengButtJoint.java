package com.zm.supplier.supplierinf.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.supplier.constants.Constants;
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
import com.zm.supplier.util.SignUtil;

@Component
public class QianFengButtJoint extends AbstractSupplierButtJoint {

	// private static final String CUSTOMER = "ZGGXHWG";// 正式
	private static final String CUSTOMER = "FQHT";// 测试

	// private static String base_url =
	// "http://114.55.149.118:8181/nredi/base/api/service?method={action}";// 正式
	private static String base_url = "http://180.168.135.198:59096/ms";// 测试
	private static String wms_url = "http://180.168.135.198:59092/ms";// 测试

	@Resource
	RedisTemplate<String, Object> template;

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user) {
		String unionPayMerId = "";
		// Object obj = template.opsForValue().get(Constants.PAY +
		// info.getCenterId() + Constants.UNION_PAY_MER_ID);
		// if (obj != null) {
		// unionPayMerId = obj.toString();
		// }
		String msg = ButtJointMessageUtils.getQianFengOrderMsg(info, user, CUSTOMER, unionPayMerId);// 报文
		return (Set<SendOrderResult>) sendQianFengWarehouse(base_url, msg, "cnec_order", SendOrderResult.class, true);
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		String msg = ButtJointMessageUtils.getQianFengCheckOrderMsg(orderIds, CUSTOMER);// 报文
		return (Set<OrderStatus>) sendQianFengWarehouse(wms_url, msg, "GetOrderInfo", OrderStatus.class, false);
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		return null;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		// TODO Auto-generated method stub
		return null;
	}

	private <T> Set<T> sendQianFengWarehouse(String url, String msg, String method, Class<T> clazz, boolean flag) {
		Map<String, String> param = new HashMap<String, String>();
		String sign = "";
		if (flag) {
			param.put("id", appKey);
			param.put("format", "xml");
			param.put("method", method);
			param.put("msg", msg);
			param.put("signmethod", "md5");
			sign = SignUtil.qianFengSign(appSecret, param);// 签名
			param.put("signature", sign);
		} else {
			param.put("userid", appKey);
			param.put("msgtype", method);
			param.put("msg", msg);
			sign = SignUtil.qianFengSign(appKey, appSecret);// 签名
			param.put("sign", sign);
		}

		logger.info("发送报文：" + msg + ",签名：" + sign);
		String result = HttpClientUtil.post(url, param);
		logger.info("返回：" + result);

		try {
			return renderResult(result, "XML", clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		QianFengButtJoint joint = new QianFengButtJoint();
		joint.setAppKey("FQHT");
		joint.setAppSecret("FQHT201804key");
		// OrderInfo info = new OrderInfo();
		// info.setOrderId("GX1001001212110411");
		// info.setRemark("测试");
		// info.setWeight(1000);
		// info.setCreateTime("2018-01-01 12:12:12");
		// OrderDetail detail = new OrderDetail();
		// detail.setPayType(2);
		// detail.setPostFee(0.0);
		// detail.setTaxFee(0.0);
		// detail.setTariffTax(0.0);
		// detail.setPayment(120.0);
		// detail.setIncrementTax(0.0);
		// detail.setExciseTax(0.0);
		// detail.setPayNo("123");
		// detail.setPayTime("2018-01-01 12:12:12");
		// detail.setDisAmount(0.0);
		// detail.setReceiveProvince("山东");
		// detail.setReceiveCity("济宁市");
		// detail.setReceiveArea("南山区");
		// detail.setReceiveAddress("南山科技园海天一路4栋");
		// detail.setReceivePhone("18949518599");
		// detail.setReceiveZipCode("273100");
		// detail.setReceiveName("李政");
		// List<OrderGoods> list = new ArrayList<OrderGoods>();
		// OrderGoods goods = new OrderGoods();
		// goods.setItemCode("310518789100000001");
		// goods.setSku("310518789100000001");
		// goods.setItemName("test");
		// goods.setActualPrice(10.0);
		// goods.setItemPrice(10.0);
		// goods.setItemQuantity(2);
		// list.add(goods);
		// goods = new OrderGoods();
		// goods.setItemCode("310518789100000004");
		// goods.setSku("310518789100000004");
		// goods.setItemName("testtest");
		// goods.setItemPrice(20.0);
		// goods.setActualPrice(20.0);
		// goods.setItemQuantity(5);
		// list.add(goods);
		// info.setOrderDetail(detail);
		// info.setOrderGoodsList(list);
		// UserInfo user = new UserInfo();
		// UserDetail d = new UserDetail();
		// d.setIdNum("530121197008214197");
		// d.setName("李政");
		// user.setUserDetail(d);
		// System.out.println(joint.sendOrder(info, user));
		// 查询订单状态
		List<String> list = new ArrayList<String>();
		list.add("GX1001001212110412");
		System.out.println(joint.checkOrderStatus(list));
		// //查询库存
		// OrderBussinessModel model = new OrderBussinessModel();
		// model.setItemCode("KD000111");
		// model.setItemId("c00083");
		// List<OrderBussinessModel> list = new
		// ArrayList<OrderBussinessModel>();
		// list.add(model);
		// model = new OrderBussinessModel();
		// model.setSku("KD000111");
		// model.setItemId("c00084");
		// list.add(model);
		// System.out.println(joint.checkStock(list));
		// System.out.println(joint.getGoods("KD000110"));
	}

}
