package com.zm.order.bussiness.service;

import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ResultPojo;

/**  
 * ClassName: OrderService <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 11, 2017 3:45:10 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public interface OrderService {

	ResultPojo saveOrder(OrderInfo info);
}
