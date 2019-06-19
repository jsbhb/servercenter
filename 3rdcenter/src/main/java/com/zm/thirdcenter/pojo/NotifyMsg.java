package com.zm.thirdcenter.pojo;

public class NotifyMsg {

	private String phone;
	private String time;
	private String name;
	private String shopName;
	private String msg;
	private String account;
	private NotifyTypeEnum type;
	
	public boolean check(){
		return phone != null;
	}
	
	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public NotifyTypeEnum getType() {
		return type;
	}

	public void setType(NotifyTypeEnum type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "NotifyMsg [phone=" + phone + ", time=" + time + ", name=" + name + ", shopName=" + shopName + ", msg="
				+ msg + ", type=" + type + "]";
	}
	
	
	
}
