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
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.DateUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.SignUtil;

@Component("kjb2cButtJoint")
public class KJB2CButtJoint extends AbstractSupplierButtJoint {

	@Resource
	RedisTemplate<String, Object> template;

	private final String order = "cnec_jh_order";
	private final String order_status = "cnec_jh_decl_byorder";

	@Override
	public Set<SendOrderResult> sendOrder(List<OrderInfo> infoList) {
		String unionPayMerId = "";
		Object obj = template.opsForValue()
				.get(Constants.PAY + infoList.get(0).getCenterId() + Constants.UNION_PAY_MER_ID);
		if (obj != null) {
			unionPayMerId = obj.toString();
		}
		String msg = ButtJointMessageUtils.getKJB2COrderMsg(infoList.get(0), unionPayMerId, "201901231617");// 报文
		Set<SendOrderResult> set = sendKJB2C(url, msg, SendOrderResult.class, infoList.get(0).getOrderId(), order);
		return set;
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<OrderIdAndSupplierId> orderList) {
		// TODO Auto-generated method stub
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

	private <T> Set<T> sendKJB2C(String url, String msg, Class<T> clazz, String param, String msgType) {
		String date = DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
		String sign = SignUtil.getKjbSign(appKey, appSecret, date);// 签名
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("timestamp", date);
		paramMap.put("xmlstr", msg);
		paramMap.put("userid", appKey);
		paramMap.put("sign", sign);
		paramMap.put("customs", "3105");
		paramMap.put("msgtype", msgType);

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
