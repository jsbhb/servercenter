package com.zm.pay.bussiness.service;

import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.zm.pay.pojo.CustomModel;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.RefundPayModel;

/**
 * ClassName: PayService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 11, 2017 3:45:10 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public interface PayService {

	Map<String, String> weiXinPay(Integer clientId, String type, PayModel model) throws Exception;

	/**
	 * @fun 支付报关
	 * @param model
	 * @return
	 */
	boolean payCustom(CustomModel model) throws Exception;

	String aliPay(Integer clientId, String type, PayModel model) throws Exception;

	/**
	 * 阿里 退款接口
	 * 
	 * @param clientId
	 * @param model
	 * @return
	 */
	Map<String,Object> aliRefundPay(Integer clientId, RefundPayModel model) throws AlipayApiException;

	/**
	 * 微信 退款接口
	 * 
	 * @param clientId
	 * @param model
	 * @return
	 */
	Map<String, Object> wxRefundPay(Integer clientId, RefundPayModel model) throws Exception;
}
