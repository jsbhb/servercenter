package com.zm.thirdcenter.pojo;

public enum NotifyTypeEnum {

	AUDIT("审核"), REPAYING("清退"), CODE("验证码");
	private String name;

	private NotifyTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
