package com.zm.supplier.supplierinf.model;

import javax.xml.bind.annotation.XmlElement;

public class EdbStockModel {

	private String barCode;
	private String sellStock;
	
	@XmlElement(name = "bar_code")
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	@XmlElement(name = "sell_stock")
	public String getSellStock() {
		return sellStock;
	}
	public void setSellStock(String sellStock) {
		this.sellStock = sellStock;
	}
	@Override
	public String toString() {
		return "EdbStockModel [barCode=" + barCode + ", sellStock=" + sellStock + "]";
	}
	
}
