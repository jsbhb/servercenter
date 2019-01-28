package com.zm.thirdcenter.feignclient.model;

public class ThirdLogin {

	private Integer userType;

	private Integer loginType;

	private String thirdAccount;

	public ThirdLogin() {
	}

	public ThirdLogin(Integer userType, String thirdAccount, Integer loginType) {
		this.userType = userType;
		this.loginType = loginType;
		this.thirdAccount = thirdAccount;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public String getThirdAccount() {
		return thirdAccount;
	}

	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}

	@Override
	public String toString() {
		return "ThirdLogin [userType=" + userType + ", loginType=" + loginType + ", thirdAccount=" + thirdAccount + "]";
	}

}
