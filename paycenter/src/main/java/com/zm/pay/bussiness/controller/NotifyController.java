package com.zm.pay.bussiness.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.zm.pay.constants.Constants;
import com.zm.pay.feignclient.OrderFeignClient;
import com.zm.pay.feignclient.UserFeignClient;
import com.zm.pay.feignclient.model.UserVip;
import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.ResultModel;
import com.zm.pay.pojo.WeixinPayConfig;
import com.zm.pay.utils.ali.util.AlipayNotify;

import springfox.documentation.annotations.ApiIgnore;

@RestController
public class NotifyController {

	@Resource
	RedisTemplate<String, Object> redisTemplate;

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

		WeixinPayConfig config = (WeixinPayConfig) redisTemplate.opsForValue()
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
	@RequestMapping(value = "auth/payMng/payReturn" ,method = RequestMethod.GET)
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
//				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
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

			AliPayConfigModel config = (AliPayConfigModel) redisTemplate.opsForValue()
					.get(Constants.PAY + clientId + Constants.ALI_PAY);
			
			Object o = redisTemplate.opsForValue().get(clientId + "-url");
			String url = (o == null ? null : o.toString());
			if(url == null){
				url = userFeignClient.getClientUrl(clientId, Constants.FIRST_VERSION);
				redisTemplate.opsForValue().set(clientId + "-url", url);
			}

			config.initParameter();
			// 计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(config, params);

			if (verify_result) {

				if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
					if (orderId.startsWith("GX")) {
						String payNo = params.get("trade_no");
						orderFeignClient.updateOrderPayStatusByOrderId(Constants.FIRST_VERSION, orderId, payNo);
						res.sendRedirect(url+"/personal.html?child=order");
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
						res.sendRedirect(url+"/personal.html");
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
	@RequestMapping(value = "auth/payMng/payNotify" ,method = RequestMethod.POST)
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

			AliPayConfigModel config = (AliPayConfigModel) redisTemplate.opsForValue()
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

	private void returnAli(PrintWriter pw, ResultModel result) {
		if (result.isSuccess()) {
			pw.print("success");
		} else {
			pw.print("fail");
		}
	}

}
