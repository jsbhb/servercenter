package com.zm.supplier.supplierinf.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.feignclient.GoodsFeignClient;
import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.JSONUtil;

@Component
public class DbClickButtjoint extends AbstractSupplierButtJoint {

	@Resource
	GoodsFeignClient goodsFeignClient;
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<SendOrderResult> sendOrder(List<OrderInfo> info) {
		OrderInfo order = info.get(0);
		List<String> itemIds = order.getOrderGoodsList().stream().map(g -> g.getItemId()).collect(Collectors.toList());
		String resultJson = goodsFeignClient.getGoodsItemProxyPrice(Constants.FIRST_VERSION, itemIds);
		Map<String,Double> tmpMap = JSONUtil.parse(resultJson, Map.class);
		order.getOrderGoodsList().stream().forEach(g -> {
			g.setProxyPrice(tmpMap.get(g.getItemId()));
		});
		Map<String, String> param = ButtJointMessageUtils.getDbClickOrder(order, appKey, appSecret);
		String orderUrl = url + "/dealer/order/Receive";
		String result = HttpClientUtil.post(orderUrl, param, "application/x-www-form-urlencoded;charset=UTF-8", false);
		saveResponse(info.get(0).getOrderId(), result);
		Map<String, String> resultMap = JSONUtil.parse(result, Map.class);
		if ("0000".equals(resultMap.get("code"))) {
			SendOrderResult orderResult = new SendOrderResult();
			orderResult.setOrderId(info.get(0).getOrderId());
			orderResult.setThirdOrderId(info.get(0).getOrderId());
			orderResult.setSupplierId(info.get(0).getSupplierId());
			Set<SendOrderResult> set = new HashSet<>();
			set.add(orderResult);
			return set;
		}
		return null;
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<OrderIdAndSupplierId> orderList) {
		Map<String, String> param = ButtJointMessageUtils.getDbClickOrderStatus(orderList.get(0), appKey, appSecret);
		String statusUrl = url + "/dealer/order/getlogistic";
		String result = HttpClientUtil.post(statusUrl, param, "application/x-www-form-urlencoded;charset=UTF-8", false);
		JSONObject obj = JSONObject.parseObject(result);
		if ("0000".equals(obj.get("code"))) {
			JSONArray ja = obj.getJSONArray("msg");
			if (ja == null || ja.isEmpty()) {
				return null;
			}
			Set<OrderStatus> set = new HashSet<OrderStatus>();
			obj = ja.getJSONObject(0);
			if(obj.getString("waybill_number") == null){
				return null;
			}
			OrderStatus status = new OrderStatus();
			String thirdOrderId = obj.getString("order_sn");
			status.setExpressId(obj.getString("waybill_number"));
			status.setLogisticsName(obj.getString("shipping_name"));
			status.setLogisticsCode(status.getLogisticsName());
			status.setThirdOrderId(thirdOrderId);
			status.setOrderId(thirdOrderId);
			status.setSupplierId(orderList.get(0).getSupplierId());
			status.setStatus("已发货");
			set.add(status);
			return set;
		}
		return null;
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
}
