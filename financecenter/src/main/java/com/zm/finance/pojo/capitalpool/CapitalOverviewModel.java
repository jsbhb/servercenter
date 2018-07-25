package com.zm.finance.pojo.capitalpool;

import java.util.List;

public class CapitalOverviewModel {

	private Integer total;
	private List<Integer> warningId;
	private Double totalFee;
	public List<Integer> getWarningId() {
		return warningId;
	}
	public void setWarningId(List<Integer> warningId) {
		this.warningId = warningId;
	}
	public Double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
}
