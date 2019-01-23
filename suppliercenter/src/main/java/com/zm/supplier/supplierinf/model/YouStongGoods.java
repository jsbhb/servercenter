package com.zm.supplier.supplierinf.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zm.supplier.pojo.CheckStockModel;

public class YouStongGoods {

	@JsonProperty("SKUNo")
	private String skuNo;
	
	@JsonProperty("SKUName")
	private String skuName;
	
	@JsonProperty("Barcodes")
	private List<String> barcodes;
	
	@JsonProperty("HasQ4S")
	private boolean hasQ4S;
	
	@JsonProperty("OnSale")
	private boolean onSale;
	
	@JsonProperty("CurrentExpiredDate")
	private String currentExpiredDate;
	
	@JsonProperty("CurrentPrices")
	private List<CurrentPrices> currentPrices;

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public List<String> getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(List<String> barcodes) {
		this.barcodes = barcodes;
	}

	public boolean isHasQ4S() {
		return hasQ4S;
	}

	public void setHasQ4S(boolean hasQ4S) {
		this.hasQ4S = hasQ4S;
	}

	public boolean isOnSale() {
		return onSale;
	}

	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}

	public String getCurrentExpiredDate() {
		return currentExpiredDate;
	}

	public void setCurrentExpiredDate(String currentExpiredDate) {
		this.currentExpiredDate = currentExpiredDate;
	}

	public List<CurrentPrices> getCurrentPrices() {
		return currentPrices;
	}

	public void setCurrentPrices(List<CurrentPrices> currentPrices) {
		this.currentPrices = currentPrices;
	}

	public CheckStockModel convert() {
		CheckStockModel model = new CheckStockModel();
		model.setItemCode(skuNo);
		model.setQuantity("0");
		model.setSku(skuNo);
		return model;
	}
}

class CurrentPrices{
	
	@JsonProperty("QtyPerOrder")
	private String qtyPerOrder;
	
	@JsonProperty("UnitPrice")
	private String unitPrice;

	public String getQtyPerOrder() {
		return qtyPerOrder;
	}

	public void setQtyPerOrder(String qtyPerOrder) {
		this.qtyPerOrder = qtyPerOrder;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
}
