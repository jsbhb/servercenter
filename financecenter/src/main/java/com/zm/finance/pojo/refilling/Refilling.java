package com.zm.finance.pojo.refilling;

import com.zm.finance.pojo.Pagination;

public class Refilling  extends Pagination{

	private Integer id;
	private Integer centerId;
	private Double startMoney;//反充前可提现金额
	private Double poolMoney;//资金池余额
	private Double money;
	private Integer status;
	private String remark;
	private String createTime;
	private String updateTime;
	private String opt;
	
	public Double getStartMoney() {
		return startMoney;
	}
	public void setStartMoney(Double startMoney) {
		this.startMoney = startMoney;
	}
	public Double getPoolMoney() {
		return poolMoney;
	}
	public void setPoolMoney(Double poolMoney) {
		this.poolMoney = poolMoney;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCenterId() {
		return centerId;
	}
	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
