package com.zm.order.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.order.pojo.OrderGoodsDTO;
import com.zm.order.pojo.OrderInfoDTO;

/**  
 * ClassName: OrderMapper <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 11, 2017 3:40:13 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public interface EshopOrderMapper {
	
	List<OrderInfoDTO> userOrderListByParam(Map<String,Object> param);
	
	List<OrderInfoDTO> userOrderDetailByParam(Map<String,Object> param);

	void updateOrderBaseEshopIn(OrderInfoDTO info);

	void insertSellOrder(OrderInfoDTO info);

	void insertSellOrderDetail(List<OrderGoodsDTO> orderGoodsList);

	void updateSellOrder(OrderInfoDTO info);
	
	List<OrderInfoDTO> sellOrderListByParam(Map<String,Object> param);
	
	List<OrderInfoDTO> sellOrderDetailByParam(Map<String,Object> param);
	
	OrderInfoDTO selectSellOrderCountInfo(Map<String,Object> param);
	
	List<OrderGoodsDTO> selectSellOrderGoodsCountInfo(Map<String,Object> param);
	
}