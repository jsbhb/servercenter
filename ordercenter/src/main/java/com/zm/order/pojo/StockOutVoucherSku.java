package com.zm.order.pojo;

public class StockOutVoucherSku {

	private Integer expectedQuantity;

	private String memo;
	
	private String skuCode;
	
	private String goodsId;

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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
}