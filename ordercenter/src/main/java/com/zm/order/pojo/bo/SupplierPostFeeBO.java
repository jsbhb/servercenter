package com.zm.order.pojo.bo;

public class SupplierPostFeeBO {

	private Integer supplierId;
	private Double postFee;
	
	public SupplierPostFeeBO(){}
	
	public SupplierPostFeeBO(Integer supplierId,Double postFee){
		
		this.supplierId = supplierId;
		this.postFee = postFee;
	}
	
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public Double getPostFee() {
		return postFee;
	}
	public void setPostFee(Double postFee) {
		this.postFee = postFee;
	}
}
