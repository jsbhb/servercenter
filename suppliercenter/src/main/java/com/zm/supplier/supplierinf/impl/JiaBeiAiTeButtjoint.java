package com.zm.supplier.supplierinf.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
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

import net.sf.json.JSONObject;

@Component
public class JiaBeiAiTeButtjoint extends AbstractSupplierButtJoint {

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user) {
		String msg = ButtJointMessageUtils.getJiaBeiAiTeOrderMsg(info, user);
		String nonce_str = System.currentTimeMillis()+"";
		String sign = SignUtil.JiaBeiAiTeSign(appKey, appSecret, nonce_str);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appKey", appKey);
		params.put("data", msg);
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
	
	private <T> Set<T> sendJiaBeiAiTeOrderPool(String url,String jsonStr, Class<T> clazz, String parem) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("jsonStr", jsonStr);

		String result = HttpClientUtil.post(url, param);
		logger.info("返回：" + parem + "===" + result);

		try {
			JSONObject json = JSONObject.fromObject(result);
			if (!"200".equals(json.getString("errorCode"))) {
				logger.info("订单:"+parem+"发送失败===");
				logger.info("errorCode:"+json.getString("errorCode")+"===msg:"+json.getString("msg"));
				return null;
			}
			return renderResult(result, "JSON", clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String appKey = "kabrita_gongxiaohaiwaigou";
		String appSecret = "A255BE77577E49CABC357C76D6AB9BC789YYE5WWK7PP";
		String nonce_str = System.currentTimeMillis()+"";
		String sign = SignUtil.JiaBeiAiTeSign(appKey, appSecret, nonce_str);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appKey", appKey);
		params.put("data", "");
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
	}

}
