package com.zm.order.pojo.bo;

/**
 * 
 * @author user
 *
 */
public class TaxFeeBO {

	private double exciseTax;
	private double incremTax;
	private double taxFee;
	
	public TaxFeeBO(){}

	public TaxFeeBO(Double exciseTax, Double incremTax, Double taxFee) {
		this.exciseTax = exciseTax;
		this.incremTax = incremTax;
		this.taxFee = taxFee;
	}

	public Double getExciseTax() {
		return exciseTax;
	}

	public void setExciseTax(Double exciseTax) {
		this.exciseTax = exciseTax;
	}

	public Double getIncremTax() {
		return incremTax;
	}

	public void setIncremTax(Double incremTax) {
		this.incremTax = incremTax;
	}

	public Double getTaxFee() {
		return taxFee;
	}

	public void setTaxFee(Double taxFee) {
		this.taxFee = taxFee;
	}

}
