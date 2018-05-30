/**  
 * Project Name:ordercenter  
 * File Name:OrderBackMapper.java  
 * Package Name:com.zm.order.bussiness.dao  
 * Date:Jan 1, 20182:50:55 PM  
 *  
 */
package com.zm.order.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.OrderInfoListForDownload;

/**  
 * ClassName: OrderBackMapper <br/>  
 * Function: 后台订单操作持久层. <br/>   
 * date: Jan 1, 2018 2:50:55 PM <br/>  
 *  
 * @author hebin  
 * @version   
 * @since JDK 1.7  
 */
public interface OrderStockOutMapper {

	/**  
	 * selectForPage:为分页检索. <br/>  
	 *  
	 * @author hebin  
	 * @param entity
	 * @return  
	 * @since JDK 1.7  
	 */
	List<OrderInfo> selectForPage(Map<String,Object> param);
	
	/**
	 * 查询总数
	 * @param param
	 * @return
	 */
	int queryCountOrderInfo(Map<String,Object> param);

	/**  
	 * selectByOrderId:根据订单编号检索订单. <br/>  
	 *  
	 * @author hebin  
	 * @param orderId
	 * @return  
	 * @since JDK 1.7  
	 */
	OrderInfo selectByOrderId(String orderId);

	/**  
	 * selectOrderGoodsForPage:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<OrderGoods> selectOrderGoodsForPage(OrderGoods entity);

	/**  
	 * selectOrdreListForDownload:订单导出. <br/>  
	 *  
	 * @author hebin  
	 * @param entity
	 * @return  
	 * @since JDK 1.7  
	 */
	List<OrderInfoListForDownload> selectOrdreListForDownload(Map<String,Object> param);

	void insertOrderBaseBatch(List<OrderInfo> list);

	void insertOrderGoodsBatch(List<OrderGoods> goodsList);

	void insertOrderDetailBatch(List<OrderDetail> detailList);

}
