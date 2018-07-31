/**  
 * Project Name:cardmanager  
 * File Name:GoodsItem.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Nov 12, 201710:35:27 PM  
 *  
 */
package com.zm.order.pojo;

/**
 * ClassName: GoodsItemEntity <br/>
 * Function: 商品明细实体. <br/>
 * date: Nov 12, 2017 10:35:27 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class GoodsItemEntity{
	private int id;//
	private String goodsId;// 商品ID
	private String itemId;// itemID
	private String itemCode;// 商家自有编码
	private String sku;// sku信息
	private String encode;// sku信息
	private double weight;// 商品重量（克）
	private double exciseTax;// 消费税
	private int isPromotion;// 是否促销0：否；1：是
	private String status;// 商品状态0：停售，1：在售
	private double discount;// 促销折扣
	private String info;// 规格信息
	private String createTime;// 创建时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	private int conversion;
	private String shelfLife;
	private String carTon;
	private String isDel;
	private Integer isFx;
	private Double retailPrice;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getExciseTax() {
		return exciseTax;
	}
	public void setExciseTax(double exciseTax) {
		this.exciseTax = exciseTax;
	}
	public int getIsPromotion() {
		return isPromotion;
	}
	public void setIsPromotion(int isPromotion) {
		this.isPromotion = isPromotion;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
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
	public int getConversion() {
		return conversion;
	}
	public void setConversion(int conversion) {
		this.conversion = conversion;
	}
	public String getShelfLife() {
		return shelfLife;
	}
	public void setShelfLife(String shelfLife) {
		this.shelfLife = shelfLife;
	}
	public String getCarTon() {
		return carTon;
	}
	public void setCarTon(String carTon) {
		this.carTon = carTon;
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	public Integer getIsFx() {
		return isFx;
	}
	public void setIsFx(Integer isFx) {
		this.isFx = isFx;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
}
