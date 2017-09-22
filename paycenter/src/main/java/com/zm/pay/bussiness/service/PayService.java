package com.zm.pay.bussiness.service;

import java.util.Map;

import com.zm.pay.pojo.PayModel;

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

	Map<String,String> weiXinPay(Integer clientId, String type, PayModel model) throws Exception;
}
