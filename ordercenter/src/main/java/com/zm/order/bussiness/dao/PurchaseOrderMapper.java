/**  
 * Project Name:ordercenter  
 * File Name:OrderBackMapper.java  
 * Package Name:com.zm.order.bussiness.dao  
 * Date:Jan 1, 20182:50:55 PM  
 *  
 */
package com.zm.order.bussiness.dao;

import com.github.pagehelper.Page;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.PurchaseOrderInfo;

/**  
 * ClassName: OrderBackMapper <br/>  
 * Function: 后台订单操作持久层. <br/>   
 * date: Jan 1, 2018 2:50:55 PM <br/>  
 *  
 * @author hebin  
 * @version   
 * @since JDK 1.7  
 */
public interface PurchaseOrderMapper {

//	Page<PurchaseOrderInfo> selectPurchaseOrderInfoForPage(PurchaseOrderInfo entity);
	
	Page<OrderInfo> selectPurchaseOrderInfoForPage(OrderInfo entity);

}
