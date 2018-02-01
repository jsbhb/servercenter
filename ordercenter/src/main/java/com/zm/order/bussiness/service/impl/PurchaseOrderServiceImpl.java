/**  
 * Project Name:ordercenter  
 * File Name:OrderBackServiceImpl.java  
 * Package Name:com.zm.order.bussiness.service.impl  
 * Date:Jan 1, 20182:49:06 PM  
 *  
 */
package com.zm.order.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.order.bussiness.dao.PurchaseOrderMapper;
import com.zm.order.bussiness.service.PurchaseOrderService;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.PurchaseOrderInfo;

/**  
 * ClassName: OrderBackServiceImpl <br/>  
 * Function: 后台订单操作服务类. <br/>   
 * date: Jan 1, 2018 2:49:06 PM <br/>  
 *  
 * @author hebin  
 * @version   
 * @since JDK 1.7  
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	@Resource
	PurchaseOrderMapper purchaseOrderMapper;
	
//	@Override
//	public Page<PurchaseOrderInfo> queryByPage(PurchaseOrderInfo entity) {
//		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
//		return purchaseOrderMapper.selectPurchaseOrderInfoForPage(entity);
//	}
	
	@Override
	public Page<OrderInfo> queryByPage(OrderInfo entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return purchaseOrderMapper.selectPurchaseOrderInfoForPage(entity);
	}

}
