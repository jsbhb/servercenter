package com.zm.auth.model;

import java.util.Date;
import java.util.List;

/**
 * @author
 * @version 1.0 2016/10/10
 * @description
 */
public class UserInfo {

	private String userId;
	private String userName;
	private String password;
	private String phone;
	private int userCenterId;
	private String email;
	private Integer status;
	private Date creationDate;
	private Integer createdBy;
	private Date lastUpdateDate;
	private Integer lastUpdatedBy;
	private Date lastPasswordResetDate;
	private List<String> authorities;
	private String token;
	private String openId;
	private int platUserType;
	private int loginType;

	/**
	 * Creates a new instance of UserInfo.
	 * 
	 * @param openId2
	 */
	
	public UserInfo(){}

	public UserInfo(String openId) {
		this.openId = openId;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getPlatUserType() {
		return platUserType;
	}

	public void setPlatUserType(int platUserType) {
		this.platUserType = platUserType;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public int getUserCenterId() {
		return userCenterId;
	}

	public void setUserCenterId(int userCenterId) {
		this.userCenterId = userCenterId;
	}
}