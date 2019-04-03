package com.zm.supplier.supplierinf.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.supplier.log.LogUtil;
import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.supplierinf.model.YouStongGoods;
import com.zm.supplier.supplierinf.model.YouStongOrder;
import com.zm.supplier.supplierinf.model.YouStongOrderStatus;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.DateUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.JSONUtil;
import com.zm.supplier.util.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class YouStongButtjoint extends AbstractSupplierButtJoint {

	private Map<String, Long> timeMap = new HashMap<String, Long>();

	private final String createOrder = "/order/create/v_1_0";
	private final String getProduct = "/product/get/v_1_0";
	private final String getOrder = "/order/get/v_1_0";
	private final Long limitTime = Long.valueOf(10000L);// 间隔10秒发请求

	@Resource
	RedisTemplate<String, Object> template;

	@Override
	public Set<SendOrderResult> sendOrder(List<OrderInfo> infoList) {
		if (checkTimeLimit(appKey)) {
			String msg = ButtJointMessageUtils.getYouStongOrder(infoList);
			String fullUrl = url + createOrder;
			Set<SendOrderResult> set = sendYouStongWarehouse(fullUrl, msg, SendOrderResult.class);
			if (set != null) {
				for (SendOrderResult result : set) {
					result.setSupplierId(infoList.get(0).getSupplierId());
				}
			}
			return set;
		}
		return null;
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<OrderIdAndSupplierId> orderList) {
		if (checkTimeLimit(appKey)) {
			List<String> orderIds = orderList.stream().map(tem -> tem.getOrderId()).collect(Collectors.toList());
			String msg = ButtJointMessageUtils.getYouStongOrderStatus(orderIds);
			String fullUrl = url + getOrder;
			Set<OrderStatus> set = sendYouStongWarehouse(fullUrl, msg, OrderStatus.class);
			if (set != null) {
				for (OrderStatus result : set) {
					result.setSupplierId(orderList.get(0).getSupplierId());
				}
			}
			return set;
		}
		return null;
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		List<String> itemCodeList = list.stream().map(tem -> tem.getItemCode()).collect(Collectors.toList());
		String msg = ButtJointMessageUtils.getYouStongStock(itemCodeList);
		String fullUrl = url + getProduct;
		Set<CheckStockModel> set = sendYouStongWarehouse(fullUrl, msg, CheckStockModel.class);
		return set;
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

	private <T> Set<T> sendYouStongWarehouse(String url, String msg, Class<T> clazz) {
		String timestamp = DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
		String requestId = System.currentTimeMillis() + "";
		String sign = SignUtil.youStongSign(appKey, appSecret, timestamp, requestId, msg);
		Map<String, String> param = new HashMap<String, String>();
		param.put("sign", sign);
		param.put("parter_id", appKey);
		param.put("request_id", requestId);
		param.put("timestamp", timestamp);
		param.put("data", msg);
		String result = HttpClientUtil.post(url, param, "", false);
		if (result == null) {
			return null;
		}
		// 放入发送的时间
		timeMap.put(appKey, Long.valueOf(System.currentTimeMillis()));
		logger.info("返回：===" + result);
		try {
			Object obj = clazz.newInstance();
			if (obj instanceof OrderStatus) {
				return renderStatusResult(result, clazz);
			} else if (obj instanceof SendOrderResult) {
				return renderOrderResult(result, clazz);
			} else if (obj instanceof CheckStockModel) {
				return renderStockResult(result, clazz);
			} else {
				return renderResult(result, "JSON", clazz);
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("转换出错", e);
		}

		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> Set<T> renderStockResult(String result, Class<T> clazz) {
		JSONObject json = JSONObject.fromObject(result);
		if (json.getBoolean("error")) {
			return null;
		}
		JSONArray obj = json.getJSONArray("data");
		List<YouStongGoods> list = new ArrayList<YouStongGoods>();
		if (obj != null && obj.size() > 0) {
			for (int i = 0; i < obj.size(); i++) {
				JSONObject jobj = obj.getJSONObject(i);
				list.add(JSONUtil.parse(jobj.toString(), YouStongGoods.class));
			}
			Set set = new HashSet();
			for (YouStongGoods goods : list) {
				if (!goods.isHasQ4S() && !goods.isOnSale()) {
					set.add(goods.convert());
				}
			}
			return set;
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> Set<T> renderOrderResult(String result, Class<T> clazz) {
		JSONObject json = JSONObject.fromObject(result);
		if (json.getBoolean("error")) {
			return null;
		}
		JSONArray obj = json.getJSONArray("data");
		List<YouStongOrder> list = new ArrayList<YouStongOrder>();
		if (obj != null && obj.size() > 0) {
			for (int i = 0; i < obj.size(); i++) {
				JSONObject jobj = obj.getJSONObject(i);
				list.add(JSONUtil.parse(jobj.toString(), YouStongOrder.class));
			}
			Set set = new HashSet();
			for (YouStongOrder order : list) {
				if (order.isSuccess()) {
					set.add(order.convert());
				}
			}
			return set;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> Set<T> renderStatusResult(String result, Class<T> clazz) {
		JSONObject json = JSONObject.fromObject(result);
		if (json.getBoolean("error")) {
			return null;
		}
		JSONArray obj = json.getJSONArray("data");
		List<YouStongOrderStatus> list = new ArrayList<YouStongOrderStatus>();
		if (obj != null && obj.size() > 0) {
			for (int i = 0; i < obj.size(); i++) {
				JSONObject jobj = obj.getJSONObject(i);
				list.add(JSONUtil.parse(jobj.toString(), YouStongOrderStatus.class));
			}
			Set set = new HashSet();
			for (YouStongOrderStatus status : list) {
				set.addAll(status.convertToOrderStatus());
			}
			return set;
		}
		return null;
	}

	/**
	 * @fun 每个接口间隔10秒发
	 * @param lastTimeKey
	 * @return
	 */
	private boolean checkTimeLimit(String lastTimeKey) {
		Long current = Long.valueOf(System.currentTimeMillis());
		Long lastTime = Long
				.valueOf(timeMap.get(lastTimeKey) != null ? Long.parseLong(timeMap.get(lastTimeKey).toString()) : 0L);
		if (lastTime.longValue() > 0L) {
			Long timeInterva = Long.valueOf(current.longValue() - lastTime.longValue());
			if (timeInterva.longValue() < limitTime.longValue())
				return false;
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		String s = "{\"error\":false,\"requestId\":\"1548146760619\",\"data\":[{\"ParterOrderNo\":\"GX0190122095302397002\",\"OrderNo\":69382,\"TotalOrderAmt\":1425.00,\"NeedToPayAmt\":0.00,\"Status\":3,\"StatusDesc\":\"全部出库\",\"Splitted\":true,\"Packages\":[{\"ExpressName\":\"百世快递\",\"TrackingNo\":\"20190122ABCC1\",\"Items\":[{\"SKUNo\":10013880,\"Qty\":1}]},{\"ExpressName\":\"百世快递\",\"TrackingNo\":\"20190122ABBD2\",\"Items\":[{\"SKUNo\":10014160,\"Qty\":1}]}]}]}";
		String ss = "{\"error\":false,\"requestId\":\"1548222635346\",\"data\":[{\"SKUNo\":10014160,\"SKUName\":\"[重庆]施华蔻（Schauma）男士薄荷清爽控油洗发露 400ml\",\"Barcodes\":[\"4015001005356\"],\"HasQ4S\":true,\"OnSale\":true,\"CurrentExpiredDate\":null,\"CurrentPrices\":[{\"QtyPerOrder\":1,\"UnitPrice\":25.00},{\"QtyPerOrder\":2,\"UnitPrice\":21.00},{\"QtyPerOrder\":3,\"UnitPrice\":20.00},{\"QtyPerOrder\":4,\"UnitPrice\":19.00},{\"QtyPerOrder\":5,\"UnitPrice\":19.00},{\"QtyPerOrder\":6,\"UnitPrice\":18.00},{\"QtyPerOrder\":10,\"UnitPrice\":17.00},{\"QtyPerOrder\":12,\"UnitPrice\":16.00},{\"QtyPerOrder\":24,\"UnitPrice\":16.00}],\"ImageUrl\":\"http://test.ibb360.com/productimage/2017/01/03/eb906ca0-4b3b-4cf3-8204-7f8d43e76722.jpg@0r_85Q_1e_1c_0l_400w_400h\"}]}";
		JSONObject json = JSONObject.fromObject(ss);
		JSONArray obj = json.getJSONArray("data");
		List<YouStongGoods> list = new ArrayList<YouStongGoods>();
		if (obj != null && obj.size() > 0) {
			for (int i = 0; i < obj.size(); i++) {
				JSONObject jobj = obj.getJSONObject(i);
				list.add(JSONUtil.parse(jobj.toString(), YouStongGoods.class));
			}
			Set set = new HashSet();
			for (YouStongGoods status : list) {
				set.add(status.convert());
			}
			System.out.println(set.toString());

		}
	}

}
