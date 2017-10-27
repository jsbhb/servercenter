package com.zm.order.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**  
 * ClassName: OrderGoods <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 11, 2017 3:16:08 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
@ApiModel(value="orderGoods", description="商品信息")
public class OrderGoods {

	private Integer id;
	
	private String orderId;
	
	@ApiModelProperty(value="商品唯一ID", dataType="String", required=true)
	private String itemId;
	
	@ApiModelProperty(value="跨境商品sku", dataType="String", required=false)
	private String sku;
	
	@ApiModelProperty(value="商品名称", dataType="String", required=true)
	private String itemName;
	
	@ApiModelProperty(value="图片地址", dataType="String", required=true)
	private String itemImg;
	
	@ApiModelProperty(value="商品规格", dataType="String", required=true)
	private String itemInfo;
	
	@ApiModelProperty(value="供应商自有编码(跨境商品)", dataType="String", required=false)
	private String itemCode;
	
	@ApiModelProperty(value="商品数量", dataType="Integer", required=true)
	private Integer itemQuantity;
	
	@ApiModelProperty(value="商品价格", dataType="Double", required=true)
	private Double itemPrice;
	
	@ApiModelProperty(value="商品实际数量", dataType="Double", required=true)
	private Double actualPrice;
	
	private String remark;

	public String getItemImg() {
		return itemImg;
	}

	public void setItemImg(String itemImg) {
		this.itemImg = itemImg;
	}

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

	public Integer getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(Integer itemQuantity) {
		this.itemQuantity = itemQuantity;
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
				+ itemName + ", itemInfo=" + itemInfo + ", itemCode=" + itemCode + ", itemQuantity=" + itemQuantity
				+ ", itemPrice=" + itemPrice + ", actualPrice=" + actualPrice + ", remark=" + remark + "]";
	}
}