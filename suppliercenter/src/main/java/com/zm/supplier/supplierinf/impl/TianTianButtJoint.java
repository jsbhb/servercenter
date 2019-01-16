package com.zm.supplier.supplierinf.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
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
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.DateUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.SignUtil;

@Component
public class TianTianButtJoint extends AbstractSupplierButtJoint {

	// private static final String CUSTOMER = "ZGGXHWG";// 正式
	// private static final String CUSTOMER = "aa001";// 测试
	// 正式店铺代码17000，测试店铺代码11612

	// private static String base_url =
	// "http://114.55.149.118:8181/nredi/base/api/service?method={action}";// 正式
	// private static String base_url =
	// "http://121.196.224.76:8081/nredi/base/api/service?method={action}";// 测试

	@Resource
	RedisTemplate<String, Object> template;

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info) {
		String unionPayMerId = "";
		Object obj = template.opsForValue().get(Constants.PAY + info.getCenterId() + Constants.UNION_PAY_MER_ID);
		if (obj != null) {
			unionPayMerId = obj.toString();
		}
		String msg = ButtJointMessageUtils.getTianTianOrderMsg(info, memberId, unionPayMerId, accountId);// 报文
		String targetUrl = url.replace("{action}", "order.create");
		return (Set<SendOrderResult>) sendTianTianWarehouse(targetUrl, msg, SendOrderResult.class, info.getOrderId());
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		String msg = ButtJointMessageUtils.getTianTianCheckOrderMsg(orderIds, memberId);// 报文
		String targetUrl = url.replace("{action}", "order.query");
		return (Set<OrderStatus>) sendTianTianWarehouse(targetUrl, msg, OrderStatus.class, orderIds.get(0));
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
	
	@Override
	public Set<OrderCancelResult> orderCancel(OrderInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	private <T> Set<T> sendTianTianWarehouse(String url, String msg, Class<T> clazz, String param) {
		String date = DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
		String sign = SignUtil.TianTianSign(msg, appSecret, date);// 签名
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("datetime", date);
		paramMap.put("msgtype", "xml");
		paramMap.put("customer", memberId);
		paramMap.put("sign", sign);
		paramMap.put("sign_method", "md5");
		try {
			paramMap.put("data", URLEncoder.encode(msg, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		paramMap.put("app_key", appKey);
		paramMap.put("secretKey", appSecret);

		logger.info("发送报文：" + msg + ",签名：" + sign);
		String result = HttpClientUtil.post(url, paramMap);
		logger.info("返回：" + param + "====" + result);

		try {
			return renderResult(result, "XML", clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
