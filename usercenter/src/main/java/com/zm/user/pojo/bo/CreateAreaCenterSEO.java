package com.zm.user.pojo.bo;

public class CreateAreaCenterSEO {

	private Integer centerId;
	private String region;
	private String domainName;
	private String mDomainName;

	public CreateAreaCenterSEO(Integer centerId, String domainName, String mDomainName) {
		this.centerId = centerId;
		if (domainName.startsWith("http")) {
			this.region = domainName.substring(domainName.indexOf("//") + 2, domainName.indexOf("."));
		} else {
			this.region = domainName.substring(0, domainName.indexOf("."));
		}
		this.domainName = domainName;
		this.mDomainName = mDomainName;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getmDomainName() {
		return mDomainName;
	}

	public void setmDomainName(String mDomainName) {
		this.mDomainName = mDomainName;
	}

}
