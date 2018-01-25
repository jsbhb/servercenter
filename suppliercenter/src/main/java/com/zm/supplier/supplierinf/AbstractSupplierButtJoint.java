package com.zm.supplier.supplierinf;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.util.JSONUtil;
import com.zm.supplier.util.XmlUtil;

public abstract class AbstractSupplierButtJoint {
	
	protected String appKey;
	
	protected String appSecret;
	
	private static final String XML = "XML";
	private static final String JSON = "JSON";
	
	protected Logger logger = LoggerFactory.getLogger(AbstractSupplierButtJoint.class);
	
	/**
	 * @fun 返回key为obj，value为calss实体类的list集合
	 * @param result
	 * @param format
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> Set<T> renderResult(String result, String format, Class<T> clazz) throws Exception{
		Set<T> set = null;
		if(result != null){
			if(XML.equalsIgnoreCase(format)){
				set = XmlUtil.parseXml(result, clazz);
			}else if(JSON.equalsIgnoreCase(format)){
				set = JSONUtil.toObject(result, clazz);
			}
		}
		if(set == null){
			//失败后处理
		}
		return set;
	}
	
	
	/**
	 * @fun 发送订单给第三方
	 * @param info
	 * @param user
	 * @return
	 */
	public abstract Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user);
	

	/**
	 * @fun 获取订单状态
	 * @param orderId
	 * @return
	 */
	public abstract Set<OrderStatus> checkOrderStatus(List<String> orderIds);
	
	/**
	 * @fun 同步库存
	 * @param list
	 * @return
	 */
	public abstract Set<CheckStockModel> checkStock(List<OrderBussinessModel> list);
	
	/**
	 * @fun 获取第三方商品
	 * @param itemCode
	 * @return
	 */
	public abstract Set<ThirdWarehouseGoods> getGoods(String itemCode);

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	
}
