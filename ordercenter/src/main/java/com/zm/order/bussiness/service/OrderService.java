package com.zm.order.bussiness.service;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;

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

	
	/**  
	 * saveOrder:新增订单，并调用支付接口进行支付. <br/>  
	 *  
	 * @author wqy  
	 * @param info
	 * @return
	 * @throws DataIntegrityViolationException
	 * @throws Exception  
	 * @since JDK 1.7  
	 */
	ResultPojo saveOrder(OrderInfo info) throws DataIntegrityViolationException, Exception;
	
	/**  
	 * listUserOrder:查询用户订单. <br/>  
	 *  
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	ResultPojo listUserOrder(Map<String,Integer> param);
	
	/**  
	 * removeUserOrder:用户删除订单. <br/>  
	 * 
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	ResultPojo removeUserOrder(Map<String,Object> param);
	
	/**  
	 * confirmUserOrder:确认订单. <br/>  
	 *  
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	ResultPojo confirmUserOrder(Map<String,Object> param);
}
