package com.zm.thirdcenter.bussiness.maijiayun.model;

public class StockInVoucherSku {

	private Integer expectedQuantity;

	private String memo;
	
	private Double price;
	
	private String skuCode;

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Integer getExpectedQuantity() {
		return expectedQuantity;
	}

	public void setExpectedQuantity(Integer expectedQuantity) {
		this.expectedQuantity = expectedQuantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}