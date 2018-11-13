package com.zm.user.pojo;

public class ThirdLogin {

	private Integer userId;

	private Integer userType;

	private Integer loginType;

	private String thirdAccount;

	public ThirdLogin() {
	}

	public ThirdLogin(Integer userId, String thirdAccount, Integer loginType, Integer userType) {
		this.userId = userId;
		this.thirdAccount = thirdAccount;
		this.loginType = loginType;
		this.userType = userType;
	}

	public boolean check() {
		return thirdAccount != null && loginType != null && userType != null;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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
		return "ThirdLogin [userId=" + userId + ", userType=" + userType + ", loginType=" + loginType + ", thirdAccount="
				+ thirdAccount + "]";
	}

}
