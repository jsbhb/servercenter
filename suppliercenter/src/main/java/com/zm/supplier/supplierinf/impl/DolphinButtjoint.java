package com.zm.supplier.supplierinf.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderDetail;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.supplierinf.model.DolphinToken;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.EncryptUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.JSONUtil;
import com.zm.supplier.util.SignUtil;

@Component
public class DolphinButtjoint extends AbstractSupplierButtJoint {

	@Resource
	RedisTemplate<String, String> template;

	@Override
	public Set<SendOrderResult> sendOrder(List<OrderInfo> info) {
		DolphinToken token = getToken();
		Map<String, String> getParam = getParam(token.getName(), "pushOrderDataInfo");
		String jsonData = ButtJointMessageUtils.getDolphinOrder(info.get(0));
		// rc4加密
		byte[] rc4Str = EncryptUtil.encry_RC4_byte(jsonData, token.getKey());
		// base64加密
		String base64Str = Base64.encodeBase64String(rc4Str);
		String sign = SignUtil.getDolphinSign(getParam, token.getKey(), base64Str);
		getParam.put("md5", sign);
		Map<String, String> postParam = new HashMap<>();
		postParam.put("data", base64Str);
		String result = HttpClientUtil.post(url, postParam, getParam);
		// 先base64解密，在rc4解密
		result = EncryptUtil.decry_RC4(Base64.decodeBase64(result.getBytes()), token.getKey());
		//保存回执
		saveResponse(info.get(0).getOrderId(), result);
		JSONObject obj = JSONObject.parseObject(result);
		if ("20000".equals(obj.getString("status"))) {
			String data = obj.getString("data");
			obj = JSONObject.parseObject(data);
			String orderJson = obj.getString(info.get(0).getOrderId());
			String status = JSONObject.parseObject(orderJson).getString("status");
			if ("yes".equals(status)) {
				SendOrderResult orderResult = new SendOrderResult();
				orderResult.setOrderId(info.get(0).getOrderId());
				orderResult.setThirdOrderId(info.get(0).getOrderId());
				orderResult.setSupplierId(50);
				Set<SendOrderResult> set = new HashSet<>();
				set.add(orderResult);
				return set;
			} else {
				return null;
			}
		}
		return null;
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<OrderIdAndSupplierId> orderList) {
		DolphinToken token = getToken();
		Map<String, String> getParam = getParam(token.getName(), "getOrderStatus");
		String jsonData = ButtJointMessageUtils.getDolphinOrderStatus(orderList.get(0));
		// rc4加密
		byte[] rc4Str = EncryptUtil.encry_RC4_byte(jsonData, token.getKey());
		// base64加密
		String base64Str = Base64.encodeBase64String(rc4Str);
		String sign = SignUtil.getDolphinSign(getParam, token.getKey(), base64Str);
		getParam.put("md5", sign);
		Map<String, String> postParam = new HashMap<>();
		postParam.put("data", base64Str);
		String result = HttpClientUtil.post(url, postParam, getParam);
		// 先base64解密，在rc4解密
		result = EncryptUtil.decry_RC4(Base64.decodeBase64(result.getBytes()), token.getKey());
		JSONObject obj = JSONObject.parseObject(result);
		if ("20000".equals(obj.getString("status"))) {
			String data = obj.getString("data");
			obj = JSONObject.parseObject(data);
			JSONObject orderObj = obj.getJSONObject("childOrder");
			Set<String> keySet = orderObj.keySet();
			Iterator<String> it = keySet.iterator();
			OrderStatus status = null;
			Set<OrderStatus> set = new HashSet<OrderStatus>();
			while (it.hasNext()) {
				StringBuilder sb = new StringBuilder();
				status = new OrderStatus();
				String thirdOrderId = it.next();
				status.setExpressId(orderObj.getJSONObject(thirdOrderId).getString("logisticsTrackingNo"));
				status.setLogisticsName(orderObj.getJSONObject(thirdOrderId).getString("logisticsName"));
				status.setLogisticsCode(status.getLogisticsName());
				status.setThirdOrderId(thirdOrderId);
				Set<String> skuKeySet = orderObj.getJSONObject(thirdOrderId).getJSONObject("skus").keySet();
				skuKeySet.stream().forEach(sku -> sb.append(sku + ","));
				status.setItemCode(sb.toString().substring(0, sb.length() - 1));
				status.setOrderId(obj.getString("orderSn"));
				status.setStatus(orderObj.getJSONObject(thirdOrderId).getString("orderStatus"));
				status.setSupplierId(orderList.get(0).getSupplierId());
				set.add(status);
			}
			// 由于推送订单时没有第三方订单号，所以用的是本地订单号，这里把第一条第三方订单号改为本地订单号，以免更新时找不到记录
			set.stream().findFirst().get().setThirdOrderId(orderList.get(0).getOrderId());
			return set;
		} else {
			return null;
		}
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OrderCancelResult> orderCancel(OrderInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	public DolphinToken getToken() {
		DolphinToken token = null;
		String tokenStr = template.opsForValue().get("dolphin:" + appKey);
		if (!StringUtils.isEmpty(tokenStr)) {
			token = JSONUtil.parse(tokenStr, DolphinToken.class);
			return token;
		}
		Map<String, String> param = getParam(appKey, "token");
		String sign = SignUtil.getDolphinSign(param, appSecret, null);
		param.put("md5", sign);
		String result = HttpClientUtil.post(url, null, param);
		result = EncryptUtil.decry_RC4(Base64.decodeBase64(result.getBytes()), appSecret);
		JSONObject obj = JSONObject.parseObject(result);
		if ("20000".equals(obj.getString("status"))) {
			String data = obj.getString("data");
			// 设置redis4分钟过期
			template.opsForValue().set("dolphin:" + appKey, data.toString(), 4, TimeUnit.MINUTES);
			token = JSONUtil.parse(data, DolphinToken.class);
			return token;
		} else {
			throw new RuntimeException("获取海豚token出错:" + result);
		}
	}

	/**
	 * @fun 封装get参数
	 * @param token
	 * @return
	 */
	private Map<String, String> getParam(String appKey, String type) {
		String time = System.currentTimeMillis() / 1000 + "";
		Map<String, String> getParam = new HashMap<>();
		getParam.put("type", type);
		getParam.put("name", appKey);
		getParam.put("time", time);
		// getParam.put("debug", "on");
		return getParam;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		DolphinButtjoint b = new DolphinButtjoint();
		b.setAppKey("test");
		b.setUrl("http://qilinapi.dolphinsc.com/index.php");
		b.setAppSecret("123456");
		// DolphinToken token = b.getToken();
		// System.out.println("name = " + token.getName() + ",key=" +
		// token.getKey());
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
		detail.setReceiveName("李政");
		detail.setCustomerIdNum("530121197008214197");
		detail.setCustomerName("李政");
		detail.setPayment(0.02);
		detail.setPostFee(0.0);
		detail.setPayNo("530121197008214197");
		detail.setPayType(1);
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		OrderGoods goods = new OrderGoods();
		goods.setItemCode("ENPAM001");
		goods.setItemQuantity(2);
		goods.setItemName("ceshi");
		goods.setActualPrice(0.01);
		list.add(goods);
		info.setOrderDetail(detail);
		info.setOrderGoodsList(list);
		List<OrderInfo> infoList = new ArrayList<>();
		infoList.add(info);
		System.out.println(b.sendOrder(infoList));
	}

}
