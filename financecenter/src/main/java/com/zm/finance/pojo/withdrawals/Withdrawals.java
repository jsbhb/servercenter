package com.zm.finance.pojo.withdrawals;

import com.zm.finance.pojo.Pagination;

public class Withdrawals  extends Pagination{

	private Integer id;
	private Integer operatorId;
	private Integer operatorType;//0区域中心，1店铺，2推手
	private Double startMoney;
	private Double outMoney;
	private Integer status;
	private String cardBank;
	private String cardNo;
	private String cardName;
	private String payNo;
	private String createTime;
	private String updateTime;
	private String opt;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public Integer getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(Integer operatorType) {
		this.operatorType = operatorType;
	}
	public Double getStartMoney() {
		return startMoney;
	}
	public void setStartMoney(Double startMoney) {
		this.startMoney = startMoney;
	}
	public Double getOutMoney() {
		return outMoney;
	}
	public void setOutMoney(Double outMoney) {
		this.outMoney = outMoney;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCardBank() {
		return cardBank;
	}
	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	
}
