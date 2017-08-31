package com.zm.order.bussiness.service;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;

import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.ResultModel;

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
	ResultModel saveOrder(OrderInfo info, Double version, String openId, String payType, String type) throws DataIntegrityViolationException, Exception;
	
	/**  
	 * listUserOrder:查询用户订单. <br/>  
	 *  
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	ResultModel listUserOrder(Map<String,Object> param, Pagination pagination);
	
	/**  
	 * removeUserOrder:用户删除订单. <br/>  
	 * 
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	ResultModel removeUserOrder(Map<String,Object> param);
	
	/**  
	 * confirmUserOrder:确认订单. <br/>  
	 *  
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	ResultModel confirmUserOrder(Map<String,Object> param);
	
	/**  
	 * updateOrder:更新订单状态. <br/>  
	 *  
	 * @author wqy  
	 * @param orderId
	 * @return  
	 * @since JDK 1.7  
	 */
	ResultModel updateOrderStatusByOrderId(Map<String,Object> param);
	
	Integer getClientIdByOrderId(String orderId);
}
