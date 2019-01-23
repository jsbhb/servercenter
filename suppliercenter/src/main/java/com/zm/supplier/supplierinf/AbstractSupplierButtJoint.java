package com.zm.supplier.supplierinf;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.SupplierInterface;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.util.JSONUtil;
import com.zm.supplier.util.XmlUtil;

public abstract class AbstractSupplierButtJoint {
	
	protected String appKey;
	
	protected String appSecret;
	
	protected String url;
	
	protected String accountId;
	
	protected String memberId;
	
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
	
	public void init(SupplierInterface inf){
		this.appKey = inf.getAppKey();
		this.appSecret = inf.getAppSecret();
		this.url = inf.getUrl();
		this.accountId = inf.getAccountId();
		this.memberId = inf.getMemberId();
	}
	
	
	/**
	 * @fun 发送订单给第三方
	 * @param info
	 * @param user
	 * @return
	 */
	public abstract Set<SendOrderResult> sendOrder(List<OrderInfo> info);
	

	/**
	 * @fun 获取订单状态
	 * @param orderId
	 * @return
	 */
	public abstract Set<OrderStatus> checkOrderStatus(List<OrderIdAndSupplierId> orderList);
	
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
	
	/**
	 * @fun 退单
	 * @param info
	 * @return
	 */
	public abstract Set<OrderCancelResult> orderCancel(OrderInfo info);

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


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getAccountId() {
		return accountId;
	}


	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	
}
