package com.zm.supplier.supplierinf.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderDetail;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.JSONUtil;
import com.zm.supplier.util.SignUtil;

import net.sf.json.JSONArray;

@Component
public class JiaBeiAiTeButtjoint extends AbstractSupplierButtJoint {

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info) {
		String msg = ButtJointMessageUtils.getJiaBeiAiTeOrderMsg(info);
		String nonce_str = System.currentTimeMillis()+"";
		String sign = SignUtil.JiaBeiAiTeSign(appKey, appSecret, nonce_str);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appKey", appKey);
		params.put("data", JSONArray.fromObject("["+msg+"]"));
		params.put("nonce_str", nonce_str);
		params.put("sign", sign);
		String jsonStr = JSONUtil.toJson(params);
		System.out.println("jsonStr:" + jsonStr);
		try {
			jsonStr = URLEncoder.encode(jsonStr, "utf-8");
			System.out.println("encode_jsonStr:" + jsonStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sendJiaBeiAiTeOrderPool(url, jsonStr, SendOrderResult.class, info.getOrderId());
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		return null;
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		
		return null;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		
		return null;
	}
	
	@Override
	public Set<OrderCancelResult> orderCancel(OrderInfo info) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private <T> Set<T> sendJiaBeiAiTeOrderPool(String url,String jsonStr, Class<T> clazz, String parem) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("jsonStr", jsonStr);

		String result = HttpClientUtil.post(url, param);
		logger.info("返回：" + parem + "===" + result);

		try {
			return renderResult(result, "JSON", clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		OrderInfo info = new OrderInfo();
		info.setOrderId("GX0190115094403924015");
		info.setStatus(12);
		info.setRemark("190115001测试订单");
		info.setCreateTime("2019-01-15 09:44:04");
		info.setUpdateTime("2019-01-15 09:46:38");
		OrderDetail detail = new OrderDetail();
		detail.setPayment(688.00);
		detail.setPostFee(0.00);
		detail.setDisAmount(0.00);
		detail.setPayTime("2019-01-15 09:46:38");
		detail.setReceiveName("CC");
		detail.setReceivePhone("13858365603");
		detail.setReceiveZipCode("000000");
		detail.setReceiveProvince("山西省");
		detail.setReceiveCity("晋城市");
		detail.setReceiveArea("泽州县");
		detail.setReceiveAddress("11111");
		info.setOrderDetail(detail);
		List<OrderGoods> goodsList = new ArrayList<OrderGoods>();
		OrderGoods goods = new OrderGoods();
		goods.setItemId("100000132");
		goods.setItemCode("GZKJ0495010180");
		goods.setItemName("荷兰 Kabrita 佳贝艾特 婴儿羊奶粉1段 800g （0-6个月）");
		goods.setItemQuantity(1);
		goods.setActualPrice(688.00);
		goodsList.add(goods);
		info.setOrderGoodsList(goodsList);
		UserInfo user = new UserInfo();
		user.setEmail("test@163.com.cn");
		
		String msg = ButtJointMessageUtils.getJiaBeiAiTeOrderMsg(info);
		String appKey = "kabrita_gongxiaohaiwaigou";
		String appSecret = "A255BE77577E49CABC357C76D6AB9BC789YYE5WWK7PP";
		String url = "http://115.28.238.213:30002/huanovo-sync/dingdan/saveOrderByTerrace.ac";
		String nonce_str = System.currentTimeMillis()+"";
		String sign = SignUtil.JiaBeiAiTeSign(appKey, appSecret, nonce_str);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appKey", appKey);
		params.put("data", JSONArray.fromObject("["+msg+"]"));
		params.put("nonce_str", nonce_str);
		params.put("sign", sign);
		String jsonStr = JSONUtil.toJson(params);
		System.out.println("jsonStr:" + jsonStr);
		try {
			jsonStr = URLEncoder.encode(jsonStr, "utf-8");
			System.out.println("jsonStr2:" + jsonStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		JiaBeiAiTeButtjoint jiabei = new JiaBeiAiTeButtjoint();
		
		Set<SendOrderResult> set = jiabei.sendJiaBeiAiTeOrderPool(url, jsonStr, SendOrderResult.class, info.getOrderId());
		
		if (set == null || set.size() == 0) {
			return;
		}
	}
}
