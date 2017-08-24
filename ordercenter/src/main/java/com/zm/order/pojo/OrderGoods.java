package com.zm.order.pojo;


/**  
 * ClassName: OrderGoods <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 11, 2017 3:16:08 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public class OrderGoods {

	private Integer id;
	
	private String orderId;
	
	private String itemId;
	
	private String sku;
	
	private String itemName;
	
	private String itemInfo;
	
	private String itemCode;
	
	private Integer itemQuantit;
	
	private Double itemPrice;
	
	private Double actualPrice;
	
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getItemQuantit() {
		return itemQuantit;
	}

	public void setItemQuantit(Integer itemQuantit) {
		this.itemQuantit = itemQuantit;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(String itemInfo) {
		this.itemInfo = itemInfo;
	}

	@Override
	public String toString() {
		return "OrderGoods [id=" + id + ", orderId=" + orderId + ", itemId=" + itemId + ", sku=" + sku + ", itemName="
				+ itemName + ", itemInfo=" + itemInfo + ", itemCode=" + itemCode + ", itemQuantit=" + itemQuantit
				+ ", itemPrice=" + itemPrice + ", actualPrice=" + actualPrice + ", remark=" + remark + "]";
	}
}