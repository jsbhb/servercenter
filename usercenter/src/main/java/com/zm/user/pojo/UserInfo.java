package com.zm.user.pojo;

/**  
 * ClassName: UserInfo <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 16, 2017 2:22:47 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public class UserInfo {

	private Integer id;
	
	private String account;
	
	private String phone;
	
	private String email;
	
	private String wechat;
	
	private String qq;
	
	private String sina_blog;
	
	private String pwd;
	
	private Integer parent_id;
	
	private Integer band;
	
	private Integer phoneValidate;
	
	private Integer emailValidate;
	
	private Integer status;
	
	private Integer centerId;
	
	private Integer shopId;
	
	private String lastLoginTime;
	
	private String lastLoginIP;
	
	private String ipCity;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;
	
	private UserDetail userDetail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getSina_blog() {
		return sina_blog;
	}

	public void setSina_blog(String sina_blog) {
		this.sina_blog = sina_blog;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getBand() {
		return band;
	}

	public void setBand(Integer band) {
		this.band = band;
	}

	public Integer getPhoneValidate() {
		return phoneValidate;
	}

	public void setPhoneValidate(Integer phoneValidate) {
		this.phoneValidate = phoneValidate;
	}

	public Integer getEmailValidate() {
		return emailValidate;
	}

	public void setEmailValidate(Integer emailValidate) {
		this.emailValidate = emailValidate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public String getIpCity() {
		return ipCity;
	}

	public void setIpCity(String ipCity) {
		this.ipCity = ipCity;
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

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", account=" + account + ", phone=" + phone + ", email=" + email + ", wechat="
				+ wechat + ", qq=" + qq + ", sina_blog=" + sina_blog + ", pwd=" + pwd + ", parent_id=" + parent_id
				+ ", band=" + band + ", phoneValidate=" + phoneValidate + ", emailValidate=" + emailValidate
				+ ", status=" + status + ", centerId=" + centerId + ", shopId=" + shopId + ", lastLoginTime="
				+ lastLoginTime + ", lastLoginIP=" + lastLoginIP + ", ipCity=" + ipCity + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", opt=" + opt + ", userDetail=" + userDetail + "]";
	}
	
}
