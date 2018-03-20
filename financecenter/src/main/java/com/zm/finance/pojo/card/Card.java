package com.zm.finance.pojo.card;

import com.zm.finance.pojo.Pagination;

public class Card  extends Pagination{
	private Integer id;
	private Integer typeId;
	private Integer setDefault;
	private String cardBank;
	private String cardNo;
	private String cardName;
	private String cardMobile;
	private String remark;
	private String createTime;
	private String updateTime;
	private String opt;
	private Integer type;//0区域中心，1店铺，2推手
	public Integer getId() {
		return id;
	}
	public Integer getSetDefault() {
		return setDefault;
	}
	public void setSetDefault(Integer setDefault) {
		this.setDefault = setDefault;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
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
	public String getCardMobile() {
		return cardMobile;
	}
	public void setCardMobile(String cardMobile) {
		this.cardMobile = cardMobile;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
