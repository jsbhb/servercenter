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
import com.zm.pay.pojo.WeixinPayConfig;

@RestController
public class NotifyController {
	
	@Resource
	RedisTemplate<String, ?> redisTemplate;
	
	@Resource
	LogFeignClient logFeignClient;
	
	@Resource
	OrderFeignClient orderFeignClient;

	@RequestMapping(value = "/payMng/wxPayReturn" ,method = RequestMethod.GET)
	public void wxNotify(HttpServletRequest req,HttpServletResponse res) throws Exception{
		//读取参数  
        InputStream inputStream ;  
        StringBuffer sb = new StringBuffer();  
        inputStream = req.getInputStream();  
        String s ;  
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
        while ((s = in.readLine()) != null){  
            sb.append(s);  
        }  
        in.close();  
        inputStream.close();  
        
        Map<String, String> notifyMap = WXPayUtil.xmlToMap(sb.toString());  // 转换成map
        
        String orderId = notifyMap.get("out_trade_no");
        
        Integer clientId = orderFeignClient.getClientIdByOrderId(orderId, 1.0);
        
        WeixinPayConfig config = (WeixinPayConfig) redisTemplate.opsForValue().get(clientId + Constants.WX_PAY);
  
        WXPay wxpay = new WXPay(config);

        String resXml = ""; 
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            // 进行处理。
        	// 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
        	if("SUCCESS".equals((String)notifyMap.get("result_code"))){
        		//通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.  
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"  
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";  
        	} else {
        		
        		resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
                        + "<return_msg><![CDATA[失败]]></return_msg>" + "</xml> ";  
        	}
        	
        }
        else {
        	 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
                     + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";  
        }
        
      //------------------------------  
        //处理业务完毕  
        //------------------------------  
        BufferedOutputStream out = new BufferedOutputStream(  
                res.getOutputStream());  
        out.write(resXml.getBytes());  
        out.flush();  
        out.close();  
    }
          
}
