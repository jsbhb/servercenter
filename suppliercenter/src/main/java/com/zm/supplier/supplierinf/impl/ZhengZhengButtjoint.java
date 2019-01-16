package com.zm.supplier.supplierinf.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zm.supplier.log.LogUtil;
import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.JSONUtil;

@Component
public class ZhengZhengButtjoint extends AbstractSupplierButtJoint {

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info) {
		String msg = ButtJointMessageUtils.getZhengZhengOrderMsg(info, accountId, memberId);
		return sendZhengZhengWarehouse(url, msg, SendOrderResult.class);
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<OrderCancelResult> orderCancel(OrderInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		// TODO Auto-generated method stub
		return null;
	}

	private <T> Set<T> sendZhengZhengWarehouse(String url, String msg, Class<T> clazz) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("data", msg);
		String result = HttpClientUtil.post(url, param);
		logger.info("返回：===" + result);
		//返回json中有utf-8的bom头
		result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
		try {
			return renderResult(result, "JSON", clazz);
		} catch (Exception e) {
			LogUtil.writeErrorLog("转换出错", e);
		}

		return null;
	}

	public static void main(String[] args) {
		String msg = "{\"ExpressAmount\":0.0,\"Address\":\"我们的\",\"StoreName\":\"中国供销海外购\",\"CreateTime\":\"2019-01-14 14:00:17.0\",\"PaymentName\":\"财付通\",\"StoreOrderNo\":\"GX0190114140016375009\",\"TotalAmount\":0.01,\"City\":\"北京\",\"CustomerIDCard\":\"330206198812131113\",\"DiscountsAmount\":0.0,\"Province\":\"北京\",\"CustomerPhone\":\"15957456456\",\"CustomerAccount\":\"15957456456\",\"TaxAmount\":0.0,\"Payment\":\"13\",\"Area\":\"海淀区\",\"ConsigneePhone\":\"13800000000\",\"ConsigneeName\":\"我们\",\"StoreID\":\"57\",\"Goods_infos\":[{\"StoreItemName\":\"（ictm）Rooicell露怡斯维他命面膜V款 10片\",\"StoreItemNumber\":\"310518614380004078\",\"Quantity\":1,\"UnitSellPrice\":0.01}],\"CustomerName\":\"我们\",\"PayTime\":\"2019-01-14 14:04:40.0\",\"PaySerialsNo\":\"4200000269201901140538425692\",\"GoodsAmount\":0.01}";
		Map<String, String> param = new HashMap<String, String>();
		param.put("data", msg);
		String result = HttpClientUtil.post("http://121.40.230.53:8088/orderApi/updataOrderInfo", param);
		System.out.println(result);
		result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
		try {
			Set<SendOrderResult> set = JSONUtil.toObject(result, SendOrderResult.class);
			System.out.println(set);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
