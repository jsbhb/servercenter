package com.zm.order.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.order.feignclient.model.SendOrderResult;
import com.zm.order.pojo.CustomModel;
import com.zm.order.pojo.Express;
import com.zm.order.pojo.ExpressFee;
import com.zm.order.pojo.OrderCount;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ShoppingCart;
import com.zm.order.pojo.ThirdOrderInfo;

/**  
 * ClassName: OrderMapper <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 11, 2017 3:40:13 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public interface OrderMapper {

	void saveOrder(OrderInfo info);
	
	void saveOrderDetail(OrderDetail orderDetail);
	
	void saveOrderGoods(List<OrderGoods> goodsList);
	
	List<OrderInfo> listOrderByParam(Map<String,Object> param);
	
	void removeUserOrder(Map<String,Object> param);
	
	void confirmUserOrder(Map<String,Object> param);
	
	void updateOrderPayStatusByOrderId(String orderId);
	
	Integer getClientIdByOrderId(String orderId);
	
	void saveShoppingCart(ShoppingCart cart);
	
	List<ShoppingCart> listShoppingCart(Map<String,Object> param);
	
	void updateOrderDetailPayTime(Map<String,Object> param);
	
	List<OrderCount> getCountByStatus(Map<String,Object> param);
	
	void removeShoppingCart(Map<String,Object> param);
	
	Integer countShoppingCart(Map<String,Object> param);
	
	void updateOrderCancel(String orderId);
	
	Integer getOrderStatusByOrderId(String orderId);
	
	OrderInfo getOrderByOrderId(String orderId);
	
	void updateOrderPayType(OrderDetail detail);
	
	void updateOrderClose(String orderId);
	
	List<String> listTimeOutOrderIds(String time);
	
	List<CustomModel> listPayCustomOrder();
	
	void updatePayCustom(String orderId);
	
	void createFreeExpressFee(@Param("centerId") Integer centerId);
	
	void createExpressFee(@Param("centerId") Integer centerId);
	
	Double getFreePostFee(@Param("id") String id);
	
	ExpressFee getExpressFee(Map<String,Object> param);
	
	Double getDefaultFee(@Param("carrierKey") String carrierKey);
	
	List<Express> listExpress();

	void updateRefundPayNo(OrderDetail detail);

	List<OrderInfo> listOrderForSendToTTWarehouse();
	
	List<OrderInfo> listOrderForSendToOtherWarehouse();

	void saveThirdOrder(List<SendOrderResult> list);

	void updateOrderSendToWarehouse(String orderId);

	void updateThirdOrderInfo(List<ThirdOrderInfo> list);

	void updateOrderStatusByThirdStatus(ThirdOrderInfo thirdOrderInfo);
	
	Integer queryCountOrderInfo(Map<String,Object> param);
}