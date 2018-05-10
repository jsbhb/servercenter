package com.zm.order.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.order.feignclient.model.SendOrderResult;
import com.zm.order.pojo.CustomModel;
import com.zm.order.pojo.Express;
import com.zm.order.pojo.ExpressFee;
import com.zm.order.pojo.Order4Confirm;
import com.zm.order.pojo.OrderCount;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderIdAndSupplierId;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ProfitProportion;
import com.zm.order.pojo.PushUserOrderCount;
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
	
	int confirmUserOrder(Map<String,Object> param);
	
	int updateOrderPayStatusByOrderId(String orderId);
	
	Integer getClientIdByOrderId(String orderId);
	
	void saveShoppingCart(ShoppingCart cart);
	
	List<ShoppingCart> listShoppingCart(Map<String,Object> param);
	
	int updateOrderDetailPayTime(Map<String,Object> param);
	
	List<OrderCount> getCountByStatus(Map<String,Object> param);
	
	void removeShoppingCart(Map<String,Object> param);
	
	Integer countShoppingCart(Map<String,Object> param);
	
	int updateOrderCancel(String orderId);
	
	Integer getOrderStatusByOrderId(String orderId);
	
	OrderInfo getOrderByOrderId(String orderId);
	
	void updateOrderPayType(OrderDetail detail);
	
	int updateOrderClose(String orderId);
	
	List<String> listTimeOutOrderIds(String time);
	
	List<CustomModel> listPayCustomOrder();
	
	int updatePayCustom(String orderId);
	
	void createFreeExpressFee(@Param("centerId") Integer centerId);
	
	void createExpressFee(@Param("centerId") Integer centerId);
	
	void createProfitProportion(@Param("centerId") Integer centerId);
	
	ProfitProportion getProfitProportion(@Param("centerId") Integer centerId);
	
	Double getFreePostFee(@Param("id") String id);
	
	ExpressFee getExpressFee(Map<String,Object> param);
	
	Double getDefaultFee(@Param("carrierKey") String carrierKey);
	
	List<Express> listExpress();

	void updateRefundPayNo(OrderDetail detail);

	void saveThirdOrder(List<SendOrderResult> list);

	int updateOrderSendToWarehouse(String orderId);

	void updateThirdOrderInfo(List<ThirdOrderInfo> list);

	int updateOrderStatusByThirdStatus(ThirdOrderInfo thirdOrderInfo);
	
	Integer queryCountOrderInfo(Map<String,Object> param);

	Integer countShoppingCartQuantity(Map<String, Object> param);

	List<OrderIdAndSupplierId> listUnDeliverOrder();

	List<Order4Confirm> listUnConfirmOrder(String time);
	
	void addOrderStatusRecord(Map<String,Object> param);

	List<OrderCount> getPushCountByStatus(Map<String, Object> param);

	int repayingPushJudge(Map<String, Object> param);

	List<PushUserOrderCount> pushUserOrderCount(Map<String, Object> param);

	void updateOrderCapitalNotEnough(List<String> list);

	List<OrderInfo> listOrderForCalCapital(String orderId);
	
	void updateOrderCapitalEnough(List<String> list);

	OrderInfo getOrderByOrderIdForRebate(String orderId);
	
	void updateOrderRebate(String orderId);

	/**
	 * @fun 获取天天仓订单，过时，统一所有订单支付单报关后发仓库
	 * @return
	 */
	@Deprecated
	List<OrderInfo> listOrderForSendToTTWarehouse();
	/**
	 * @fun 获取其他仓仓订单，过时，统一所有订单支付单报关后发仓库
	 * @return
	 */
	@Deprecated
	List<OrderInfo> listOrderForSendToOtherWarehouse();
	
	List<OrderInfo>listOrderForSendToWarehouse();

	void updateOrderRefunds(String orderId);

	List<OrderInfo> listCapitalPoolNotEnough();
	
	List<ThirdOrderInfo> getThirdInfo(String orderId);

	List<Integer> listOrderStatus(String orderId);

	Integer getGradeId(String orderId);
	
}