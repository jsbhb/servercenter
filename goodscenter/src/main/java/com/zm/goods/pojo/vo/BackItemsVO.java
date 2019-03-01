package com.zm.goods.pojo.vo;

import java.util.List;

import com.zm.goods.pojo.po.GoodsPricePO;

/**
 * @fun 后台根据specsTpId 获取供应商商品信息
 * @author user
 *
 */
public class BackItemsVO {

	private String itemId;
	private String sku;
	private String itemCode;
	private String supplierId;
	private String supplierName;
	private List<GoodsPricePO> priceList;
	private int stock;
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
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public List<GoodsPricePO> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<GoodsPricePO> priceList) {
		this.priceList = priceList;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
}
