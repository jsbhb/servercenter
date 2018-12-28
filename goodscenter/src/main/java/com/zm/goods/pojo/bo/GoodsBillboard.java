package com.zm.goods.pojo.bo;

public class GoodsBillboard {

	private int pic_X_Coordinates;//主图X坐标
	private int pic_Y_Coordinates;//主图Y坐标
	private int picWidth;
	private int picHeight;
	private int type_X_Coordinates;//商品类型X坐标
	private int type_Y_Coordinates;//商品类型Y坐标
	private int typeWidth;
	private int typeHeight;
	private int name_X_Coordinates;//名称X坐标；
	private int name_Y_Coordinates;//名称Y坐标；
	private String nameFont;//名称字体；
	private int nameFontSize;//字号
	private int nameNumPerLine;//每一行多少个字
	private int nameSpacing;//每一行间距
	private String nameColor;//字体颜色
	private int nameRows;//名称行数
	private int code_X_Coordinates;//二维码X坐标；
	private int code_Y_Coordinates;//二维码Y坐标；
	private int codeWidth;
	private int codeHeight;
	private int price_X_Coordinates;//价格X坐标；
	private int price_Y_Coordinates;//价格Y坐标;
	private int virtualPrice_X_Coordinates;//价格X坐标；
	private int virtualPrice_Y_Coordinates;//价格Y坐标;
	private int tag_X_Coordinates;//标签X坐标；
	private int tag_Y_Coordinates;//标签Y坐标;
	private int tagSpacing;//标签间距
	private int tagPerLine;//每一行标签个数
	private String priceFont;//价格字体；
	private int priceFontSize;//字号；
	private String priceColor;//价格颜色
	private String templatePath;//模板地址
	private int templateWidth;
	private int templateHeight;
	
	public void setDefault(String prePath){
		pic_X_Coordinates = 80;
		pic_Y_Coordinates = 150;
		picWidth = 585;
		picHeight = 585;
		type_X_Coordinates = 28;
		type_Y_Coordinates = 757;
		code_X_Coordinates = 417;
		code_Y_Coordinates = 955;
		codeWidth = 251;
		codeHeight = 251;
		price_X_Coordinates = 88;
		price_Y_Coordinates = 1062;
		name_X_Coordinates = 28;
		name_Y_Coordinates = 820;
		nameFont = "黑体";
		nameFontSize = 30;
		nameNumPerLine = 23;
		nameColor = "0,0,0";
		priceFont = "微软雅黑";
		priceFontSize = 97;
		priceColor = "255,0,0";
//		templatePath = prePath + "/images/template/default.png";
		templatePath = prePath + "/images/template/default.jpg";
		templateWidth = 455;
		templateHeight = 880;
		nameSpacing = 38;
		nameRows = 2;
	}

	public int getNameRows() {
		return nameRows;
	}

	public void setNameRows(int nameRows) {
		this.nameRows = nameRows;
	}

	public int getNameSpacing() {
		return nameSpacing;
	}

	public void setNameSpacing(int nameSpacing) {
		this.nameSpacing = nameSpacing;
	}

	public int getPic_X_Coordinates() {
		return pic_X_Coordinates;
	}

	public void setPic_X_Coordinates(int pic_X_Coordinates) {
		this.pic_X_Coordinates = pic_X_Coordinates;
	}

	public int getPic_Y_Coordinates() {
		return pic_Y_Coordinates;
	}

	public void setPic_Y_Coordinates(int pic_Y_Coordinates) {
		this.pic_Y_Coordinates = pic_Y_Coordinates;
	}

	public int getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(int picWidth) {
		this.picWidth = picWidth;
	}

	public int getPicHeight() {
		return picHeight;
	}

	public void setPicHeight(int picHeight) {
		this.picHeight = picHeight;
	}

	public int getType_X_Coordinates() {
		return type_X_Coordinates;
	}

	public void setType_X_Coordinates(int type_X_Coordinates) {
		this.type_X_Coordinates = type_X_Coordinates;
	}

	public int getType_Y_Coordinates() {
		return type_Y_Coordinates;
	}

	public void setType_Y_Coordinates(int type_Y_Coordinates) {
		this.type_Y_Coordinates = type_Y_Coordinates;
	}

	public int getTypeWidth() {
		return typeWidth;
	}

	public void setTypeWidth(int typeWidth) {
		this.typeWidth = typeWidth;
	}

	public int getTypeHeight() {
		return typeHeight;
	}

	public void setTypeHeight(int typeHeight) {
		this.typeHeight = typeHeight;
	}

	public int getName_X_Coordinates() {
		return name_X_Coordinates;
	}

	public void setName_X_Coordinates(int name_X_Coordinates) {
		this.name_X_Coordinates = name_X_Coordinates;
	}

	public int getName_Y_Coordinates() {
		return name_Y_Coordinates;
	}

	public void setName_Y_Coordinates(int name_Y_Coordinates) {
		this.name_Y_Coordinates = name_Y_Coordinates;
	}

	public String getNameFont() {
		return nameFont;
	}

	public void setNameFont(String nameFont) {
		this.nameFont = nameFont;
	}

	public int getNameFontSize() {
		return nameFontSize;
	}

	public void setNameFontSize(int nameFontSize) {
		this.nameFontSize = nameFontSize;
	}

	public int getNameNumPerLine() {
		return nameNumPerLine;
	}

	public void setNameNumPerLine(int nameNumPerLine) {
		this.nameNumPerLine = nameNumPerLine;
	}

	public String getNameColor() {
		return nameColor;
	}

	public void setNameColor(String nameColor) {
		this.nameColor = nameColor;
	}

	public int getCode_X_Coordinates() {
		return code_X_Coordinates;
	}

	public void setCode_X_Coordinates(int code_X_Coordinates) {
		this.code_X_Coordinates = code_X_Coordinates;
	}

	public int getCode_Y_Coordinates() {
		return code_Y_Coordinates;
	}

	public void setCode_Y_Coordinates(int code_Y_Coordinates) {
		this.code_Y_Coordinates = code_Y_Coordinates;
	}

	public int getCodeWidth() {
		return codeWidth;
	}

	public void setCodeWidth(int codeWidth) {
		this.codeWidth = codeWidth;
	}

	public int getCodeHeight() {
		return codeHeight;
	}

	public void setCodeHeight(int codeHeight) {
		this.codeHeight = codeHeight;
	}

	public int getPrice_X_Coordinates() {
		return price_X_Coordinates;
	}

	public void setPrice_X_Coordinates(int price_X_Coordinates) {
		this.price_X_Coordinates = price_X_Coordinates;
	}

	public int getPrice_Y_Coordinates() {
		return price_Y_Coordinates;
	}

	public void setPrice_Y_Coordinates(int price_Y_Coordinates) {
		this.price_Y_Coordinates = price_Y_Coordinates;
	}

	public int getVirtualPrice_X_Coordinates() {
		return virtualPrice_X_Coordinates;
	}

	public void setVirtualPrice_X_Coordinates(int virtualPrice_X_Coordinates) {
		this.virtualPrice_X_Coordinates = virtualPrice_X_Coordinates;
	}

	public int getVirtualPrice_Y_Coordinates() {
		return virtualPrice_Y_Coordinates;
	}

	public void setVirtualPrice_Y_Coordinates(int virtualPrice_Y_Coordinates) {
		this.virtualPrice_Y_Coordinates = virtualPrice_Y_Coordinates;
	}

	public int getTag_X_Coordinates() {
		return tag_X_Coordinates;
	}

	public void setTag_X_Coordinates(int tag_X_Coordinates) {
		this.tag_X_Coordinates = tag_X_Coordinates;
	}

	public int getTag_Y_Coordinates() {
		return tag_Y_Coordinates;
	}

	public void setTag_Y_Coordinates(int tag_Y_Coordinates) {
		this.tag_Y_Coordinates = tag_Y_Coordinates;
	}

	public int getTagSpacing() {
		return tagSpacing;
	}

	public void setTagSpacing(int tagSpacing) {
		this.tagSpacing = tagSpacing;
	}

	public int getTagPerLine() {
		return tagPerLine;
	}

	public void setTagPerLine(int tagPerLine) {
		this.tagPerLine = tagPerLine;
	}

	public String getPriceFont() {
		return priceFont;
	}

	public void setPriceFont(String priceFont) {
		this.priceFont = priceFont;
	}

	public int getPriceFontSize() {
		return priceFontSize;
	}

	public void setPriceFontSize(int priceFontSize) {
		this.priceFontSize = priceFontSize;
	}

	public String getPriceColor() {
		return priceColor;
	}

	public void setPriceColor(String priceColor) {
		this.priceColor = priceColor;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public int getTemplateWidth() {
		return templateWidth;
	}

	public void setTemplateWidth(int templateWidth) {
		this.templateWidth = templateWidth;
	}

	public int getTemplateHeight() {
		return templateHeight;
	}

	public void setTemplateHeight(int templateHeight) {
		this.templateHeight = templateHeight;
	}

}
