package com.zm.thirdcenter.pojo;

public enum NotifyTypeEnum {

	AUDIT("审核"), REPAYING("清退"), CODE("验证码"),INVITATION_CODE("邀请码"),SHOP_MANAGER_AUDIT_PASS(
			"微店审核通过"), SHOP_MANAGER_AUDIT_UNPASS("微店审核未通过");
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
