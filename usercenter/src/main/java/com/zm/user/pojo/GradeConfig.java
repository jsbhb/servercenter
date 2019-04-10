package com.zm.user.pojo;

public class GradeConfig {

	private Integer id;
	
	private Integer gradeId;
	
	private String headImg;
	
	private String aboutus;
	
	private String name;
	
	private Integer shopExtensionFlg;  //微店推广入口
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getAboutus() {
		return aboutus;
	}

	public void setAboutus(String aboutus) {
		this.aboutus = aboutus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getShopExtensionFlg() {
		return shopExtensionFlg;
	}

	public void setShopExtensionFlg(Integer shopExtensionFlg) {
		this.shopExtensionFlg = shopExtensionFlg;
	}
}
