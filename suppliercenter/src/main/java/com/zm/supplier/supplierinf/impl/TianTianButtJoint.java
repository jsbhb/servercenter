package com.zm.supplier.supplierinf.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Component;

import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.DateUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.SignUtil;
import com.zm.supplier.util.XmlUtil;

@Component
public class TianTianButtJoint extends AbstractSupplierButtJoint {

	private static final String CUSTOMER = "";

	@Override
	public Map<String, Object> sendOrder(OrderInfo info, UserInfo user) {

		String msg = ButtJointMessageUtils.getTianTianOrderMsg(info, user, CUSTOMER);// 报文
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
		String url = "nredi/base/api/service?method=order.create";
		logger.info("发送报文：" + msg + ",签名：" + sign);
		String result = HttpClientUtil.post(url, param);
		logger.info("返回：" + result);
		
		return null;
	}

}
