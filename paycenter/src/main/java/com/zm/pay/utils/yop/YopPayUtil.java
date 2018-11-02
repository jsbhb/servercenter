package com.zm.pay.utils.yop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.utils.CalculationUtils;

public class YopPayUtil {
	
	public String yopPay(PayModel payModel) throws IOException {
		String goodsParamExt = "{\"goodsName\":\""+payModel.getDetail()+"\",\"goodsDesc\":\"\"}";
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", payModel.getOrderId());
		try {
			params.put("orderAmount", CalculationUtils.div(payModel.getTotalAmount(), 100, 2) + "");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
//		params.put("timeoutExpress", timeoutExpress);//单位：分钟，默认24小时，最小1分钟，最大180天
//		params.put("requestDate", requestDate);//请求时间，用于计算订单有效期，格式 yyyy-MM-dd HH:mm:ss，不传默认为易宝接收到请求的时间
		params.put("redirectUrl", Constants.YOP_RETURN_URL);
		params.put("notifyUrl", Constants.YOP_NOTIFY_URL);
		params.put("goodsParamExt", goodsParamExt);
		params.put("paymentParamExt", "");//支付扩展信息当需要限制交易所使用的卡的时候，可以使用本参数来对支付的卡号，姓名，身份证进行限制，仅对快捷支付有效
		params.put("industryParamExt", "");//行业拓展参数,预留字段，暂时不需要输入
		params.put("memo", "");//自定义自身业务需要使用的字段，如对账时定义该订单应属的会计科目等最多支持21个中文或32位英文字符
//		params.put("riskParamExt", riskParamExt);//风控拓展参数,如需填写，请联系易宝技术支持提供
		
		Map<String, String> result = new HashMap<String,String>();
		String uri = YeepayService.getUrl(YeepayService.TRADEORDER_URL);
		result = YeepayService.requestYOP(params, uri, YeepayService.TRADEORDER);
	}
}
