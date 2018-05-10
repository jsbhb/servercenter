/**  
 * Project Name:ordercenter  
 * File Name:OrderBackService.java  
 * Package Name:com.zm.order.bussiness.service  
 * Date:Dec 30, 20172:03:03 PM  
 *  
 */
package com.zm.order.bussiness.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.OrderInfoListForDownload;
import com.zm.order.pojo.ThirdOrderInfo;

/**
 * ClassName: OrderBackService <br/>
 * Function: 分页查询订单. <br/>
 * date: Dec 30, 2017 2:03:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface OrderStockOutService {
	/**
	 * queryByPage:分页查询订单信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<OrderInfo> queryByPage(OrderInfo entity);

	/**
	 * queryByOrderId:根据编号查询订单. <br/>
	 * 
	 * @author orderId
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	OrderInfo queryByOrderId(String orderId);

	/**  
	 * queryByPageForGoods:查询订单商品. <br/>  
	 *  
	 * @author hebin  
	 * @param entity
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<OrderGoods> queryByPageForGoods(OrderGoods entity);

	/**
	 * queryByOrderId:根据编号查询订单. <br/>
	 * 
	 * @author orderId
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	List<ThirdOrderInfo> queryThirdInfo(String orderId);

	/**
	 * queryByOrderId:根据编号查询订单. <br/>
	 * 
	 * @author orderId
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	List<OrderInfoListForDownload> queryOrdreListForDownload(String startTime, String endTime, String gradeId);

}
