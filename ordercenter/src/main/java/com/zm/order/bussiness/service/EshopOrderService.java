package com.zm.order.bussiness.service;

import com.zm.order.pojo.OrderInfoDTO;
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
public interface EshopOrderService {

	/**
	 * queryUserOrderList:Eshop查询用户订单. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel queryUserOrderList(OrderInfoDTO info, Pagination pagination);

	/**
	 * queryUserOrderDetail:Eshop查询用户订单明细. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel queryUserOrderDetail(OrderInfoDTO info);
	
	/**
	 * setUserOrderFlg:设置用户订单标记. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void setUserOrderFlg(OrderInfoDTO info);

	/**
	 * userOrderInstock:Eshop用户订单入库. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel userOrderInstock(OrderInfoDTO info);

	/**
	 * createSellOrderInfo:Eshop创建销售订单. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel createSellOrderInfo(OrderInfoDTO info);

	/**
	 * confirmSellOrderInfo:Eshop确认销售订单. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel confirmSellOrderInfo(OrderInfoDTO info);

	/**
	 * querySellOrderInfo:Eshop查询销售订单. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel querySellOrderInfo(OrderInfoDTO info);

	/**
	 * querySellOrderDetail:Eshop查询销售订单详情. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel querySellOrderDetail(OrderInfoDTO info);

	/**
	 * querySellOrderCountInfo:Eshop查询销售订单统计信息. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel querySellOrderCountInfo(OrderInfoDTO info);

	/**
	 * querySellOrderGoodsCountInfo:Eshop查询销售订单商品统计信息. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel querySellOrderGoodsCountInfo(OrderInfoDTO info);
}
