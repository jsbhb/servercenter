package com.zm.pay.bussiness.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayApiException;
import com.zm.pay.exception.PayUtilException;
import com.zm.pay.pojo.CustomModel;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.RefundPayModel;
import com.zm.pay.pojo.ResultModel;

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

	Map<String, String> weiXinPay(Integer clientId, String type, PayModel model);

	/**
	 * @fun 支付报关
	 * @param model
	 * @return
	 */
	boolean payCustom(CustomModel model) throws Exception;

	Map<String, Object> aliPay(Integer clientId, String type, PayModel model);

	Map<String, Object> unionPay(Integer clientId, PayModel model, String type);

	/**
	 * 阿里 退款接口
	 * 
	 * @param clientId
	 * @param model
	 * @return
	 */
	Map<String, Object> aliRefundPay(Integer clientId, RefundPayModel model) throws AlipayApiException;

	/**
	 * 微信 退款接口
	 * 
	 * @param clientId
	 * @param model
	 * @return
	 */
	Map<String, Object> wxRefundPay(Integer clientId, RefundPayModel model) throws Exception;

	/**
	 * 支付接口
	 * 
	 * @return ResultModel
	 */
	ResultModel pay(Double version, String type, Integer payType, HttpServletRequest req, String orderId)
			throws PayUtilException;

	/**
	 * @fun 测试验证微信https服务器
	 * @return
	 */
	String testHttps();

	/**
	 * 
	 * @param clientId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	String yopPay(Integer clientId, PayModel model) throws PayUtilException;

}
