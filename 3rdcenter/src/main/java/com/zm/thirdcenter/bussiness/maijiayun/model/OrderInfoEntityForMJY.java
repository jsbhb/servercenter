package com.zm.thirdcenter.bussiness.maijiayun.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderInfoEntityForMJY{

	private String storeCode;
	
	private String code;
	
	private String relatedVoucherCode;

	private Integer expectedSkuQuantity;

	private String memo;

	private List<StockInVoucherSku> stockInVoucherSkus;

	private List<StockOutVoucherSku> stockOutVoucherSkus;

	public boolean check() {
		if (storeCode == null || code == null || relatedVoucherCode == null || 
			expectedSkuQuantity == 0) {
			return false;
		}
		return true;
	}
	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRelatedVoucherCode() {
		return relatedVoucherCode;
	}

	public void setRelatedVoucherCode(String relatedVoucherCode) {
		this.relatedVoucherCode = relatedVoucherCode;
	}

	public Integer getExpectedSkuQuantity() {
		return expectedSkuQuantity;
	}

	public void setExpectedSkuQuantity(Integer expectedSkuQuantity) {
		this.expectedSkuQuantity = expectedSkuQuantity;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<StockInVoucherSku> getStockInVoucherSkus() {
		return stockInVoucherSkus;
	}

	public void setStockInVoucherSkus(List<StockInVoucherSku> stockInVoucherSkus) {
		this.stockInVoucherSkus = stockInVoucherSkus;
	}
	public List<StockOutVoucherSku> getStockOutVoucherSkus() {
		return stockOutVoucherSkus;
	}
	public void setStockOutVoucherSkus(List<StockOutVoucherSku> stockOutVoucherSkus) {
		this.stockOutVoucherSkus = stockOutVoucherSkus;
	}
}