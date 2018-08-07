package com.zm.supplier.supplierinf.model;

import javax.xml.bind.annotation.XmlElement;

public class EdbGoodsInfo {

	
	private String barCode;//条形码
	
	private String proName;//产品名称
	
	private String standard;//规格
	
	private String proPicture;//产品明细图
	
	private String weight;//重量
	
	private String brandName;//品牌名称
	
	private String pictureurl;//产品图片
	
	@XmlElement(name = "bar_code")
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	@XmlElement(name = "pro_name")
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	
	@XmlElement(name = "standard")
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	
	@XmlElement(name = "pro_picture")
	public String getProPicture() {
		return proPicture;
	}
	public void setProPicture(String proPicture) {
		this.proPicture = proPicture;
	}
	
	@XmlElement(name = "weight")
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	@XmlElement(name = "brand_name")
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	@XmlElement(name = "pictureurl")
	public String getPictureurl() {
		return pictureurl;
	}
	public void setPictureurl(String pictureurl) {
		this.pictureurl = pictureurl;
	}
	@Override
	public String toString() {
		return "EdbGoodsInfo [barCode=" + barCode + ", proName=" + proName + ", standard=" + standard + ", proPicture="
				+ proPicture + ", weight=" + weight + ", brandName=" + brandName + ", pictureurl=" + pictureurl + "]";
	}
	
}
