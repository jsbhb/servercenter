package com.zm.supplier.supplierinf.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
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
import com.zm.supplier.util.DateUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.SignUtil;

@Component
public class TianTianButtJoint extends AbstractSupplierButtJoint {

//	private static final String CUSTOMER = "ZGGXHWG";//正式
	private static final String CUSTOMER = "aa001";//测试

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user) {

		String msg = ButtJointMessageUtils.getTianTianOrderMsg(info, user, CUSTOMER);// 报文
		String url = "http://121.196.224.76:8022/nredi/base/api/service?method=order.create";//测试
//		String url = "http://114.55.149.118:8181/nredi/base/api/service?method=order.create";//正式
		return (Set<SendOrderResult>) sendTianTianWarehouse(url, msg, SendOrderResult.class);
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		String msg = ButtJointMessageUtils.getTianTianCheckOrderMsg(orderIds, CUSTOMER);// 报文
		String url = "http://121.196.224.76:8022/nredi/base/api/service?method=order.query";//测试
//		String url = "http://114.55.149.118:8181/nredi/base/api/service?method=order.query";//正式
		return (Set<OrderStatus>) sendTianTianWarehouse(url, msg, OrderStatus.class);
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
	
	private <T> Set<T> sendTianTianWarehouse(String url, String msg, Class<T> clazz){
		String date = DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
		String sign = SignUtil.TianTianSign(msg, appSecret, date);// 签名
		Map<String, String> param = new HashMap<String, String>();
		param.put("datetime", date);
		param.put("msgtype", "xml");
		param.put("customer", CUSTOMER);
		param.put("sign", sign);
		param.put("sign_method", "md5");
		try {
			param.put("data", URLEncoder.encode(msg, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		param.put("app_key", appKey);
		param.put("secretKey", appSecret);
		
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

}
