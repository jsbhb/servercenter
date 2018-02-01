/**  
 * Project Name:ordercenter  
 * File Name:OrderBackService.java  
 * Package Name:com.zm.order.bussiness.service  
 * Date:Dec 30, 20172:03:03 PM  
 *  
 */
package com.zm.order.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.PurchaseOrderInfo;

/**
 * ClassName: OrderBackService <br/>
 * Function: 分页查询订单. <br/>
 * date: Dec 30, 2017 2:03:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface PurchaseOrderService {
	
//	Page<PurchaseOrderInfo> queryByPage(PurchaseOrderInfo entity);
	
	Page<OrderInfo> queryByPage(OrderInfo entity);

}
