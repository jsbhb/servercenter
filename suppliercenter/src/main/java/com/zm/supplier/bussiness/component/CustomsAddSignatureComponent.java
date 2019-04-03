package com.zm.supplier.bussiness.component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.custominf.model.CustomConfig;
import com.zm.supplier.log.LogUtil;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.bo.SupplierResponse;
import com.zm.supplier.pojo.bo.SupplierResponseEnum;
import com.zm.supplier.pojo.callback.ReceiveLogisticsCompany;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.DateUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.SignUtil;
import com.zm.supplier.util.XmlUtil;

/**
 * @fun 订单加签
 * @author user
 *
 */
@Component
public class CustomsAddSignatureComponent {
	
	@Resource
	SupplierMapper supplierMapper;
	
	// 国际物流账号
	@Value("${userId}")
	private String userId;
	// 国际物流密码
	@Value("${password}")
	private String password;
	// 国际物流加签url
	@Value("${url}")
	private String url;

	/**
	 * @fun 调用国际物流加签接口
	 * @param info
	 * @param receiveLogisticsCompany
	 * @param config
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	public String addSignature(OrderInfo info, ReceiveLogisticsCompany receiveLogisticsCompany, CustomConfig config)
			throws UnsupportedEncodingException, DocumentException {

		String msg = ButtJointMessageUtils.getKJBAddSignature(info, receiveLogisticsCompany, config);
		Map<String, String> paramMap = initData(msg, "CEB311");
		boolean https = false;
		if (url.startsWith("https")) {
			https = true;
		}
		String result = HttpClientUtil.post(url, paramMap, "", https);
		LogUtil.writeLog("加签返回：" + info.getOrderId() + "====" + result);
		//保存返回报文
		saveResponse(info.getOrderId(),result);
		Map<String, String> resultMap = XmlUtil.xmlToMap(result);
		if ("T".equalsIgnoreCase(resultMap.get("Result"))) {
			String signatureStr = new String(Base64.decodeBase64(resultMap.get("ResultMsg")), "UTF-8");
			LogUtil.writeLog("加签结果报文，base64解密后：" + signatureStr);
			return signatureStr;
		} else {
			LogUtil.writeLog("加签失败：" + resultMap.get("Remark"));
			return null;
		}
	}

	private void saveResponse(String orderId, String result) {
		SupplierResponse response = new SupplierResponse();
		response.setContent(result);
		response.setOrderId(orderId);
		response.setType(SupplierResponseEnum.SIGN.ordinal());
		supplierMapper.saveResponse(response);
	}

	private Map<String, String> initData(String msg, String msgType) throws UnsupportedEncodingException {
		String date = DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
		String sign = SignUtil.getKjbSign(userId, password, date);// 签名
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("timestamp", date);
		paramMap.put("xmlstr", Base64.encodeBase64String(msg.getBytes("UTF-8")));
		paramMap.put("userid", userId);
		paramMap.put("sign", sign);
		paramMap.put("msgtype", msgType);

		LogUtil.writeLog("发送报文：" + msg + ",签名：" + sign);
		return paramMap;
	}

	public String signature(String originData) throws UnsupportedEncodingException {
		Map<String, String> paramMap = initData(originData, "CNECSIGN");
		boolean https = false;
		if (url.startsWith("https")) {
			https = true;
		}
		String result = HttpClientUtil.post(url, paramMap, "", https);
		LogUtil.writeLog("获取签名返回：====" + result);
		return result;
	}
}
