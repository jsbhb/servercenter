package com.zm.pay.bussiness.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.zm.pay.constants.Constants;
import com.zm.pay.feignclient.LogFeignClient;
import com.zm.pay.feignclient.OrderFeignClient;
import com.zm.pay.feignclient.UserFeignClient;
import com.zm.pay.feignclient.model.UserVip;
import com.zm.pay.pojo.ResultModel;
import com.zm.pay.pojo.WeixinPayConfig;

@RestController
public class NotifyController {

	@Resource
	RedisTemplate<String, ?> redisTemplate;

	@Resource
	LogFeignClient logFeignClient;

	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	UserFeignClient userFeignClient;

	@RequestMapping(value = "auth/payMng/wxPayReturn", method = RequestMethod.POST)
	public void wxNotify(HttpServletRequest req, HttpServletResponse res) throws Exception {
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

		String orderId = notifyMap.get("out_trade_no");
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
					// TODO 发送第三方？是否需要做幂等？
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

}
