package com.zm.thirdcenter.feignclient.model;

public class ThirdLogin {

	private Integer userType;

	private Integer type;

	private String thirdAccount;

	public ThirdLogin() {
	}

	public ThirdLogin(Integer userType, String thirdAccount, Integer type) {
		this.userType = userType;
		this.type = type;
		this.thirdAccount = thirdAccount;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getThirdAccount() {
		return thirdAccount;
	}

	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}

	@Override
	public String toString() {
		return "ThirdLogin [userType=" + userType + ", type=" + type + ", thirdAccount=" + thirdAccount + "]";
	}

}
