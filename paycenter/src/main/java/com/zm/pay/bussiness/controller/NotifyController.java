package com.zm.pay.bussiness.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.zm.pay.constants.Constants;
import com.zm.pay.feignclient.OrderFeignClient;
import com.zm.pay.feignclient.UserFeignClient;
import com.zm.pay.feignclient.model.UserVip;
import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.ResultModel;
import com.zm.pay.pojo.UnionPayConfig;
import com.zm.pay.pojo.WeixinPayConfig;
import com.zm.pay.pojo.YopConfigModel;
import com.zm.pay.utils.IOUtils;
import com.zm.pay.utils.JSONUtil;
import com.zm.pay.utils.ali.util.AlipayNotify;
import com.zm.pay.utils.unionpay.sdk.AcpService;
import com.zm.pay.utils.unionpay.sdk.Base;
import com.zm.pay.utils.unionpay.sdk.LogUtil;
import com.zm.pay.utils.unionpay.sdk.SDKConstants;
import com.zm.pay.utils.yop.YeepayService;

import springfox.documentation.annotations.ApiIgnore;

@RestController
public class NotifyController {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	UserFeignClient userFeignClient;

	private Logger logger = LoggerFactory.getLogger(NotifyController.class);

	@RequestMapping(value = "auth/payMng/wxPayReturn", method = RequestMethod.POST)
	@ApiIgnore
	public void wxNotify(HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.info("微信NOTIFY回调");
		// 读取参数
		InputStream inputStream;
		StringBuffer sb = new StringBuffer();
		inputStream = req.getInputStream();
		String s;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		while ((s = in.readLine()) != null) {
			sb.append(s);
		}
		in.close();
		inputStream.close();

		Map<String, String> notifyMap = WXPayUtil.xmlToMap(sb.toString()); // 转换成map

		String orderId = notifyMap.get("out_trade_no").split("_")[0];

		Integer clientId = null;
		UserVip user = null;
		if (orderId != null && orderId.startsWith("GX")) {
			clientId = orderFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
		}
		if (orderId != null && orderId.startsWith("VIP")) {
			user = userFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
			clientId = user.getCenterId();
		}

		WeixinPayConfig config = (WeixinPayConfig) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.WX_PAY);

		WXPay wxpay = new WXPay(config);

		String resXml = "";
		if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
			// 签名正确
			// 进行处理。
			// 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
			if ("SUCCESS".equals((String) notifyMap.get("result_code"))) {
				if (orderId.startsWith("GX")) {
					String payNo = notifyMap.get("transaction_id");
					ResultModel result = orderFeignClient.updateOrderPayStatusByOrderId(Constants.FIRST_VERSION,
							orderId, payNo);
					if (result.isSuccess()) {
						// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
						resXml = getWXCallBackMsg("SUCCESS", "OK");
					} else {
						resXml = getWXCallBackMsg("FAIL", "失败");
					}
				}
				if (orderId.startsWith("VIP")) {
					if (!userFeignClient.isAlreadyPay(Constants.FIRST_VERSION, orderId)) {
						userFeignClient.updateVipOrder(Constants.FIRST_VERSION, orderId);
						boolean flag = userFeignClient.getVipUser(Constants.FIRST_VERSION, user.getUserId(), clientId);
						if (flag) {
							userFeignClient.updateUserVip(Constants.FIRST_VERSION, user);
						} else {
							userFeignClient.saveUserVip(Constants.FIRST_VERSION, user);
						}
					}
					resXml = getWXCallBackMsg("SUCCESS", "OK");
				}

			} else {

				resXml = getWXCallBackMsg("FAIL", "失败");
			}

		} else {

			resXml = getWXCallBackMsg("FAIL", "验签失败");
		}

		// ------------------------------
		// 处理业务完毕
		// ------------------------------
		BufferedOutputStream out = new BufferedOutputStream(res.getOutputStream());
		out.write(resXml.getBytes());
		out.flush();
		out.close();
	}

	private String getWXCallBackMsg(String success, String msg) {
		return "<xml>" + "<return_code><![CDATA[" + success + "]]></return_code>" + "<return_msg><![CDATA[" + msg
				+ "]]></return_msg>" + "</xml> ";
	}

	// return_url
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "auth/payMng/payReturn", method = RequestMethod.GET)
	@ApiIgnore
	public void payReturn(HttpServletRequest req, HttpServletResponse res) {
		logger.info("支付宝RETURN回调");
		try {
			// 获取支付宝GET过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = req.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "utf-8");
				params.put(name, valueStr);
			}

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// 商户订单号

			String orderId = new String(req.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

			// 交易状态
			String trade_status = new String(req.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

			Integer clientId = null;
			UserVip user = null;
			if (orderId != null && orderId.startsWith("GX")) {
				clientId = orderFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
			}
			if (orderId != null && orderId.startsWith("VIP")) {
				user = userFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
				clientId = user.getCenterId();
			}

			AliPayConfigModel config = (AliPayConfigModel) template.opsForValue()
					.get(Constants.PAY + clientId + Constants.ALI_PAY);

			config.initParameter();
			// 计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(config, params);

			if (verify_result) {

				if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
					if (orderId.startsWith("GX")) {
						String payNo = params.get("trade_no");
						orderFeignClient.updateOrderPayStatusByOrderId(Constants.FIRST_VERSION, orderId, payNo);
						res.sendRedirect(config.getUrl() + "/personal.html?child=order");
						return;
					}
					if (orderId.startsWith("VIP")) {
						if (!userFeignClient.isAlreadyPay(Constants.FIRST_VERSION, orderId)) {
							userFeignClient.updateVipOrder(Constants.FIRST_VERSION, orderId);
							boolean flag = userFeignClient.getVipUser(Constants.FIRST_VERSION, user.getUserId(),
									clientId);
							if (flag) {
								userFeignClient.updateUserVip(Constants.FIRST_VERSION, user);
							} else {
								userFeignClient.saveUserVip(Constants.FIRST_VERSION, user);
							}
						}
						res.sendRedirect(config.getUrl() + "/personal.html");
						return;
					}
				}

			} else {
				logger.info("验证失败");
			}
		} catch (Exception e) {
			logger.error("支付宝支付", e);
		}
	}

	// notify_url
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "auth/payMng/payNotify", method = RequestMethod.POST)
	@ApiIgnore
	public void payNotify(HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.info("支付宝NOTIFY回调");
		PrintWriter pw = res.getWriter();
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = req.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "utf-8");
				params.put(name, valueStr);
			}

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// 商户订单号

			String orderId = new String(req.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			String trade_status = new String(req.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

			Integer clientId = null;
			UserVip user = null;
			if (orderId != null && orderId.startsWith("GX")) {
				clientId = orderFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
			}
			if (orderId != null && orderId.startsWith("VIP")) {
				user = userFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
				clientId = user.getCenterId();
			}

			AliPayConfigModel config = (AliPayConfigModel) template.opsForValue()
					.get(Constants.PAY + clientId + Constants.ALI_PAY);
			config.initParameter();

			if (AlipayNotify.verify(config, params)) {// 验证成功
				if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
					if (orderId.startsWith("GX")) {
						String payNo = params.get("trade_no");
						ResultModel result = orderFeignClient.updateOrderPayStatusByOrderId(Constants.FIRST_VERSION,
								orderId, payNo);
						returnAli(pw, result);
					}
					if (orderId.startsWith("VIP")) {
						if (!userFeignClient.isAlreadyPay(Constants.FIRST_VERSION, orderId)) {
							userFeignClient.updateVipOrder(Constants.FIRST_VERSION, orderId);
							boolean flag = userFeignClient.getVipUser(Constants.FIRST_VERSION, user.getUserId(),
									clientId);
							if (flag) {
								ResultModel result = userFeignClient.updateUserVip(Constants.FIRST_VERSION, user);
								returnAli(pw, result);
							} else {
								ResultModel result = userFeignClient.saveUserVip(Constants.FIRST_VERSION, user);
								returnAli(pw, result);
							}
						} else {
							pw.println("success");
						}
					}
				}
			} else {// 验证失败
				logger.info("支付宝验证失败");
				pw.print("fail");
			}
		} catch (Exception e) {
			logger.error("支付宝回调出错", e);
			pw.print("fail");
		}
	}

	@RequestMapping(value = "auth/payMng/unionNotify", method = RequestMethod.POST)
	@ApiIgnore
	public void unionPayNotify(HttpServletRequest req, HttpServletResponse resp) {
		LogUtil.writeLog("BackRcvResponse接收后台通知开始");

		String encoding = req.getParameter(SDKConstants.param_encoding);
		// 获取银联通知服务器发送的后台通知参数
		Map<String, String> respParam = getAllRequestParam(req);
		Map<String, String> valideData = null;
		if (null != respParam && !respParam.isEmpty()) {
			Iterator<Entry<String, String>> it = respParam.entrySet().iterator();
			valideData = new HashMap<String, String>(respParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				try {
					value = new String(value.getBytes(encoding), encoding);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				valideData.put(key, value);
			}
		}
		LogUtil.printRequestLog(respParam);
		String orderId = valideData.get("orderId");
		Integer clientId = null;
		UserVip user = null;
		if (orderId != null && orderId.startsWith("GX")) {
			clientId = orderFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
		}
		if (orderId != null && orderId.startsWith("VIP")) {
			user = userFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
			clientId = user.getCenterId();
		}

		UnionPayConfig config = (UnionPayConfig) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.UNION_PAY);

		// 重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
		if (!AcpService.validate(valideData, encoding, config)) {
			LogUtil.writeLog("验证签名结果[失败].");
			// 验签失败，需解决验签问题

		} else {
			LogUtil.writeLog("验证签名结果[成功].");
			if (orderId.startsWith("GX")) {
				String payNo = valideData.get("queryId");
				orderFeignClient.updateOrderPayStatusByOrderId(Constants.FIRST_VERSION, orderId, payNo);
			}
			if (orderId.startsWith("VIP")) {
				if (!userFeignClient.isAlreadyPay(Constants.FIRST_VERSION, orderId)) {
					userFeignClient.updateVipOrder(Constants.FIRST_VERSION, orderId);
					boolean flag = userFeignClient.getVipUser(Constants.FIRST_VERSION, user.getUserId(), clientId);
					if (flag) {
						userFeignClient.updateUserVip(Constants.FIRST_VERSION, user);
					} else {
						userFeignClient.saveUserVip(Constants.FIRST_VERSION, user);
					}
				}
			}

		}
		LogUtil.writeLog("接收后台通知结束");
		// 返回给银联服务器http 200 状态码
		try {
			resp.getWriter().print("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "auth/payMng/union-frontrcv", method = RequestMethod.POST)
	@ApiIgnore
	public void UnionPayFrontRcvResp(HttpServletRequest req, HttpServletResponse res) {
		LogUtil.writeLog("FrontRcvResponse前台接收报文返回开始");

		String encoding = req.getParameter(SDKConstants.param_encoding);
		LogUtil.writeLog("返回报文中encoding=[" + encoding + "]");
		Map<String, String> respParam = getAllRequestParam(req);

		// 打印请求报文
		LogUtil.printRequestLog(respParam);
		Map<String, String> valideData = null;
		if (null != respParam && !respParam.isEmpty()) {
			Iterator<Entry<String, String>> it = respParam.entrySet().iterator();
			valideData = new HashMap<String, String>(respParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				try {
					value = new String(value.getBytes(encoding), encoding);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				valideData.put(key, value);
			}
		}

		String orderId = valideData.get("orderId");
		Integer clientId = null;
		UserVip user = null;
		if (orderId != null && orderId.startsWith("GX")) {
			clientId = orderFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
		}
		if (orderId != null && orderId.startsWith("VIP")) {
			user = userFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
			clientId = user.getCenterId();
		}

		UnionPayConfig config = (UnionPayConfig) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.UNION_PAY);

		if (!AcpService.validate(valideData, encoding, config)) {
			LogUtil.writeLog("验证签名结果[失败].");
		} else {
			LogUtil.writeLog("验证签名结果[成功].");
			String payNo = valideData.get("queryId");
			if (orderId.startsWith("GX")) {
				orderFeignClient.updateOrderPayStatusByOrderId(Constants.FIRST_VERSION, orderId, payNo);
				try {
					res.sendRedirect(config.getUrl() + "/personal.html?child=order");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			if (orderId.startsWith("VIP")) {
				if (!userFeignClient.isAlreadyPay(Constants.FIRST_VERSION, orderId)) {
					userFeignClient.updateVipOrder(Constants.FIRST_VERSION, orderId);
					boolean flag = userFeignClient.getVipUser(Constants.FIRST_VERSION, user.getUserId(), clientId);
					if (flag) {
						userFeignClient.updateUserVip(Constants.FIRST_VERSION, user);
					} else {
						userFeignClient.saveUserVip(Constants.FIRST_VERSION, user);
					}
				}
				try {
					res.sendRedirect(config.getUrl() + "/personal.html");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}

	private void returnAli(PrintWriter pw, ResultModel result) {
		if (result.isSuccess()) {
			pw.print("success");
		} else {
			pw.print("fail");
		}
	}

	/**
	 * 获取请求参数中所有的信息。
	 * 非struts可以改用此方法获取，好处是可以过滤掉request.getParameter方法过滤不掉的url中的参数。
	 * struts可能对某些content-type会提前读取参数导致从inputstream读不到信息，所以可能用不了这个方法。
	 * 理论应该可以调整struts配置使不影响，但请自己去研究。
	 * 调用本方法之前不能调用req.getParameter("key");这种方法，否则会导致request取不到输入流。
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Map<String, String> getAllRequestParamStream(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		try {
			String notifyStr = new String(IOUtils.toByteArray(request.getInputStream()), Base.encoding);
			LogUtil.writeLog("收到通知报文：" + notifyStr);
			String[] kvs = notifyStr.toString().split("&");
			for (String kv : kvs) {
				String[] tmp = kv.split("=");
				if (tmp.length >= 2) {
					String key = tmp[0];
					String value = URLDecoder.decode(tmp[1], Base.encoding);
					res.put(key, value);
				}
			}
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeLog("getAllRequestParamStream.UnsupportedEncodingException error: " + e.getClass() + ":"
					+ e.getMessage());
		} catch (IOException e) {
			LogUtil.writeLog("getAllRequestParamStream.IOException error: " + e.getClass() + ":" + e.getMessage());
		}
		return res;
	}

	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (res.get(en) == null || "".equals(res.get(en))) {
					// System.out.println("======为空的字段名===="+en);
					res.remove(en);
				}
			}
		}
		return res;
	}

	@RequestMapping(value = "auth/payMng/yop-payReturn")
	@ApiIgnore
	public void yopPayReturn(HttpServletRequest req, HttpServletResponse res) {
		String merchantNo = req.getParameter("merchantNo");
		String parentMerchantNo = req.getParameter("parentMerchantNo");
		String orderId = req.getParameter("orderId");
		String sign = req.getParameter("sign");
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("merchantNo", merchantNo);
		responseMap.put("parentMerchantNo", parentMerchantNo);
		responseMap.put("orderId", orderId);
		responseMap.put("sign", sign);
		Integer clientId = null;
		UserVip user = null;
		if (orderId != null && orderId.startsWith("GX")) {
			clientId = orderFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
		}
		if (orderId != null && orderId.startsWith("VIP")) {
			user = userFeignClient.getClientIdByOrderId(orderId, Constants.FIRST_VERSION);
			clientId = user.getCenterId();
		}
		YopConfigModel config = (YopConfigModel) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.YOP_PAY);

		if (YeepayService.verifyCallback(responseMap, config)) {

		} else {
			logger.info("易宝支付验签出错");
		}
	}

	@RequestMapping(value = "auth/payMng/yop-payNotify")
	@ApiIgnore
	public void yopPayNotify(HttpServletRequest req, HttpServletResponse res) throws IOException {
		YopConfigModel config = (YopConfigModel) template.opsForValue().get(Constants.PAY + 2 + Constants.YOP_PAY);
		// 获取回调数据
		String responseMsg = req.getParameter("response");
		Map<String, String> jsonMap = new HashMap<String, String>();
		DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
		dto.setCipherText(responseMsg);
		PrivateKey privateKey = YeepayService.getPrivateKey(config);
		System.out.println("privateKey: " + privateKey);
		PublicKey publicKey = YeepayService.getPubKey(config);
		System.out.println("publicKey: " + publicKey);

		dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
		System.out.println("解密结果:" + dto.getPlainText());
		jsonMap = JSONUtil.parse(dto.getPlainText(), new TypeReference<TreeMap<String, String>>() {
		});
		String orderId = jsonMap.get("orderId");
		String payNo = jsonMap.get("uniqueOrderNo");
		PrintWriter pw = res.getWriter();
		if (orderId.startsWith("GX")) {
			ResultModel result = orderFeignClient.updateOrderPayStatusByOrderId(Constants.FIRST_VERSION, orderId,
					payNo);
			if (result.isSuccess()) {
				LogUtil.writeLog("===========易宝支付回调成功===============");
				pw.println("SUCCESS");
				return;
			}
		}
		LogUtil.writeErrorLog("******************易宝支付回调失败****************");
		pw.println("FAIL");
	}

}
