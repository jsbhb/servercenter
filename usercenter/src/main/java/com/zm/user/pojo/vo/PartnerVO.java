package com.zm.user.pojo.vo;

import com.zm.user.pojo.po.PartnerPO;

public class PartnerVO {

	private String partnerName;// 企业名称
	private String aboutusPath;// 介绍地址
	private String picPath;// 图片地址

	public PartnerVO(PartnerPO po) {
		this.partnerName = po.getPartnerName();
		this.aboutusPath = po.getAboutusPath();
		this.picPath = po.getPicPath();
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getAboutusPath() {
		return aboutusPath;
	}

	public void setAboutusPath(String aboutusPath) {
		this.aboutusPath = aboutusPath;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
