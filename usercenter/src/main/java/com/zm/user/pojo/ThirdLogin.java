package com.zm.user.pojo;

import com.zm.user.constants.Constants;

public class ThirdLogin {
	
	private Integer userId;

	private Integer userType;

	private String wechat;

	private String qq;

	private String sinaBlog;
	
	public ThirdLogin(){}
	
	public ThirdLogin(Integer userId, Integer userType, String thirdAccount, Integer type){
		this.userType = userType;
		this.userId = userId;
		if(Constants.WX_LOGIN.equals(type)){
			this.wechat = thirdAccount;
		}
		if(Constants.QQ_LOGIN.equals(type)){
			this.qq = thirdAccount;
		}
		if(Constants.SINABLOG_LOGIN.equals(type)){
			this.sinaBlog = thirdAccount;
		}
	}

	public boolean check() {
		return userType != null && (wechat != null || qq != null || sinaBlog != null);
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

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getSinaBlog() {
		return sinaBlog;
	}

	public void setSinaBlog(String sinaBlog) {
		this.sinaBlog = sinaBlog;
	}

	@Override
	public String toString() {
		return "ThirdLogin [userId=" + userId + ", userType=" + userType + ", wechat=" + wechat + ", qq=" + qq
				+ ", sinaBlog=" + sinaBlog + "]";
	}

}
