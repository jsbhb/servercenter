package com.zm.supplier.custominf;

import java.util.Set;

import javax.annotation.Resource;

import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.custominf.model.CustomConfig;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.bo.SupplierResponse;
import com.zm.supplier.pojo.bo.SupplierResponseEnum;
import com.zm.supplier.pojo.callback.ReceiveLogisticsCompany;

/**
 * @fun 海关接口
 * @author user
 *
 */
public abstract class AbstractCustom {

	protected CustomConfig config;

	@Resource
	protected SupplierMapper supplierMapper;

	public void init(CustomConfig config) {
		this.config = config;
	}

	public void saveResponse(String orderId, String result, SupplierResponseEnum srenum) {
		SupplierResponse response = new SupplierResponse();
		response.setContent(result);
		response.setOrderId(orderId);
		response.setType(srenum.ordinal());
		supplierMapper.saveResponse(response);
	}

	/**
	 * @fun 订单申报
	 * @param info
	 * @return
	 */
	public abstract Set<OrderStatus> orderCustom(OrderInfo info, ReceiveLogisticsCompany receiveLogisticsCompany);

	/**
	 * @fun 订单加签并申报总署
	 * @param info
	 * @return
	 */
	public abstract Set<OrderStatus> addSignatureAndZsCustoms(OrderInfo info,
			ReceiveLogisticsCompany receiveLogisticsCompany, int appType);
}
