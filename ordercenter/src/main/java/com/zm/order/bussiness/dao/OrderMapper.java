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
import com.zm.order.pojo.bo.CustomOrderReturn;
import com.zm.order.pojo.bo.ExpressMaintenanceBO;
import com.zm.order.pojo.bo.ExpressRule;
import com.zm.order.pojo.bo.Rebate4Order;
import com.zm.order.pojo.bo.SupplierPostFeeBO;

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
	
	OrderInfo getClientIdByOrderId(String orderId);
	
	void saveShoppingCart(ShoppingCart cart);
	
	List<ShoppingCart> listShoppingCart(ShoppingCart shoppingCart);
	
	int updateOrderDetailPayTime(Map<String,Object> param);
	
	List<OrderCount> getCountByStatus(Map<String,Object> param);
	
	void removeShoppingCart(Map<String,Object> param);
	
	Integer countShoppingCart(Map<String,Object> param);
	
	int updateOrderCancel(String orderId);
	
	List<String> isExist(OrderInfo order);
	
	OrderInfo getOrderByOrderId(String orderId);
	
	void updateOrderPayType(OrderDetail detail);
	
	int updateOrderClose(String orderId);
	
	List<String> listTimeOutOrderIds(String time);
	
	List<CustomModel> listPayCustomOrder();
	
	int updatePayCustom(String orderId);
	
	ProfitProportion getProfitProportion(@Param("centerId") Integer centerId);
	
	List<SupplierPostFeeBO> getFreePostFee();
	
	List<ExpressFee> getExpressFee(Map<String,Object> param);
	
	Double getDefaultFee();
	
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
	/**
	 * @fun 获取跨境推送订单
	 * @return
	 */
	List<OrderInfo> listOrderForSendToWarehouse();
	/**
	 * @fun 获取一般贸易推送订单
	 * @return
	 */
	List<OrderInfo> listOrderForSendToWarehouseGeneralTrade();

	void updateOrderRefunds(String orderId);

	List<OrderInfo> listCapitalPoolNotEnough();
	
	List<ThirdOrderInfo> getThirdInfo(String orderId);

	List<Integer> listOrderStatus(String orderId);

	Integer getGradeId(String orderId);
	
	void updateThirdOrderInfoById(ExpressMaintenanceBO model);
	
	void saveThirdOrderInfo(ExpressMaintenanceBO model);
	
	List<ExpressRule> listExpressRule(Integer supplierId);
	
	void insertRebateConsume(Rebate4Order rebate4Order);

	List<Rebate4Order> listRebate4Order();

	void deleteRebate4Order(List<Rebate4Order> list);

	List<String> getThirdOrderId(String orderId);

	String getOrderIdFromThirdOrderId(String orderId);
	
	/**
	 * @fun 根据订单号获取推送信息
	 * @return
	 */
	List<OrderInfo> listOrderForSendByOrderId(String orderId);

	List<OrderGoods> listOrderGoodsNameByOrderId(List<String> orderIds);
	/**
	 * @fun 保存订单对应的物流企业信息
	 * @param orderExpress
	 */
	void saveCustomOrderExpress(CustomOrderReturn orderExpress);

	/**
	 * @fun 根据订单号获取订单商品
	 * @param orderId
	 * @return
	 */
	List<OrderGoods> listOrderGoodsByOrderId(String orderId);
	/**
	 * @fun 根据订单号获取订单详情
	 * @return
	 */
	Map<String,String> getOrderDetailByOrderId(String orderId);

	OrderInfo getOrderForSupplierCenterHandle(String orderId);

	String getOrderExpressDetail(String orderId);
	
}