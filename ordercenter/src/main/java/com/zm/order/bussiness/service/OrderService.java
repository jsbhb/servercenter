package com.zm.order.bussiness.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;

import com.zm.order.feignclient.model.SendOrderResult;
import com.zm.order.pojo.CustomModel;
import com.zm.order.pojo.Express;
import com.zm.order.pojo.OrderCount;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderIdAndSupplierId;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.PostFeeDTO;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.ShoppingCart;
import com.zm.order.pojo.ThirdOrderInfo;
import com.zm.order.pojo.bo.GoodsVO;
import com.zm.order.pojo.bo.OrderStatusCallBack;
import com.zm.order.pojo.bo.SupplierPostFeeBO;

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
	ResultModel saveOrder(OrderInfo info, String payType, String type, HttpServletRequest req)
			throws DataIntegrityViolationException, Exception;

	/**
	 * listUserOrder:查询用户订单. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel listUserOrder(OrderInfo info, Pagination pagination);

	/**
	 * removeUserOrder:用户删除订单. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel removeUserOrder(Map<String, Object> param);

	/**
	 * confirmUserOrder:确认订单. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel confirmUserOrder(Map<String, Object> param);

	/**
	 * updateOrderPayStatusByOrderId:更新订单状态为已支付. <br/>
	 * 
	 * @author wqy
	 * @param orderId
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel updateOrderPayStatusByOrderId(Map<String, Object> param);

	/**
	 * getClientIdByOrderId:根据订单号获取客户端ID. <br/>
	 * 
	 * @author wqy
	 * @param orderId
	 * @return
	 * @since JDK 1.7
	 */
	OrderInfo getClientIdByOrderId(String orderId);

	/**
	 * saveShoppingCart:保存购物车. <br/>
	 * 
	 * @author wqy
	 * @param orderId
	 * @return
	 * @since JDK 1.7
	 */
	void saveShoppingCart(ShoppingCart cart);

	/**
	 * listShoppingCart:获取购物车. <br/>
	 * 
	 * @author wqy
	 * @param orderId
	 * @return
	 * @since JDK 1.7
	 */
	List<GoodsVO> listShoppingCart(ShoppingCart shoppingCart, Pagination pagination) throws Exception;

	/**
	 * getCountByStatus:获取各状态的订单数量. <br/>
	 * 
	 * @author wqy
	 * @param orderId
	 * @return
	 * @since JDK 1.7
	 */
	List<OrderCount> getCountByStatus(Map<String, Object> param, String type);

	/**
	 * removeShoppingCart:删除购物车. <br/>
	 * 
	 * @author wqy
	 * @param orderId
	 * @return
	 * @since JDK 1.7
	 */
	void removeShoppingCart(Map<String, Object> param);

	/**
	 * countShoppingCart:获取购物车数量. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	Integer countShoppingCart(Map<String, Object> param);

	/**
	 * orderCancel:订单取消. <br/>
	 * 
	 * @author wqy
	 * @param info
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel orderCancel(Integer userId, String orderId) throws Exception;

	/**
	 * getOrderByOrderIdForPay:支付中心根据订单号获取订单. <br/>
	 * 
	 * @author wqy
	 * @param info
	 * @return
	 * @since JDK 1.7
	 */
	OrderInfo getOrderByOrderIdForPay(String orderId);

	/**
	 * updateOrderPayType:更新支付方式. <br/>
	 * 
	 * @author wqy
	 * @param info
	 * @return
	 * @since JDK 1.7
	 */
	boolean updateOrderPayType(OrderDetail detail);

	/**
	 * closeOrder:付款时如果订单超时关闭. <br/>
	 * 
	 * @author wqy
	 * @param orderId
	 * @return
	 * @since JDK 1.7
	 */
	boolean closeOrder(Integer userId, String orderId);

	/**
	 * closeOrder:定时器关闭超时订单. <br/>
	 * 
	 * @author wqy
	 * @param orderId
	 * @return
	 * @since JDK 1.7
	 */
	void timeTaskcloseOrder();

	/**
	 * listPayCustomOrder:获取需要支付单报关的订单. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	List<CustomModel> listPayCustomOrder();

	/**
	 * updatePayCustom:更新订单支付报关状态. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void updatePayCustom(String orderId);

	/**
	 * getPostFee:获取订单邮费. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	List<SupplierPostFeeBO> getPostFee(List<PostFeeDTO> postFee);

	/**
	 * listExpress:获取快递公司. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	List<Express> listExpress();

	/**
	 * updateRefundPayNo:更新退款交易单号. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void updateRefundPayNo(OrderDetail detail);

	/**
	 * listOrderForSendToWarehouse:获取需要推送仓库的订单. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	List<OrderInfo> listOrderForSendToWarehouse();
	
	/**
	 * saveThirdOrder:保存第三方订单号. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void saveThirdOrder(List<SendOrderResult> list);

	/**
	 * checkOrderStatus:查询第三方订单状态. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel checkOrderStatus(List<OrderIdAndSupplierId> list);

	/**
	 * changeOrderStatusByThirdWarehouse:根据第三方订单状态修改本地状态. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void changeOrderStatusByThirdWarehouse(List<ThirdOrderInfo> list);

	/**
	 * countShoppingCartQuantity:根据itemID和centerId获取itemID购物车内数量. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	Integer countShoppingCartQuantity(Map<String, Object> param);

	List<Object> getProfit(Integer shopId);

	List<OrderIdAndSupplierId> listUnDeliverOrder();

	void confirmByTimeTask();

	ResultModel repayingPushJudge(Integer pushUserId, Integer shopId);

	ResultModel pushUserOrderCount(Integer shopId, List<Integer> pushUserIdList);

	ResultModel orderBackCancel(String orderId,String payNo);

	/**
	 * @fun 退款中
	 * @param orderId
	 * @return
	 */
	ResultModel refunds(String orderId);

	/**
	 * @fun 资金池重新计算
	 * @return
	 */
	boolean capitalPoolRecount();
	
	/**
	 * @fun 供应商订单状态回传
	 * @param callBack
	 * @return
	 */
	ResultModel orderStatusCallBack(OrderStatusCallBack callBack);

	/**
	 * @fun 退款中并通知供应商不发货
	 * @param orderId
	 * @return
	 */
	ResultModel refundsWithSendOrder(String orderId);
}
