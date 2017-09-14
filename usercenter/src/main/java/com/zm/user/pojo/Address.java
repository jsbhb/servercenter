package com.zm.user.pojo;

/**
 * ClassName: Address <br/>
 * Function: 用户收货地址. <br/>
 * date: Aug 16, 2017 2:47:18 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public class Address {

	private Integer id;

	private Integer userId;

	private String province;

	private String city;

	private String area;

	private String address;

	private String zipCode;

	private String receivePhone;

	private String receiveName;

	private Integer setDefault;

	private String createTime;

	private String updateTime;

	private String opt;

	public boolean check() {
		return province != null && city != null && receivePhone != null && receiveName != null && address != null
				&& userId != null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public Integer getSetDefault() {
		return setDefault;
	}

	public void setSetDefault(Integer setDefault) {
		this.setDefault = setDefault;
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

	@Override
	public String toString() {
		return "Address [id=" + id + ", userId=" + userId + ", province=" + province + ", city=" + city + ", area="
				+ area + ", address=" + address + ", zipCode=" + zipCode + ", receivePhone=" + receivePhone
				+ ", receiveName=" + receiveName + ", setDefault=" + setDefault + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", opt=" + opt + "]";
	}

}
