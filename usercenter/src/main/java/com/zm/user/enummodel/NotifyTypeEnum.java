package com.zm.user.enummodel;

public enum NotifyTypeEnum {

	AUDIT("审核"), REPAYING("清退"), CODE("验证码"),INVITATION_CODE("邀请码");
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
