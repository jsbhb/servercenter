package com.zm.goods.pojo.po;

/**
 * @fun 每周特卖商品记录表PO
 * @author user
 *
 */
public class BigSalesGoodsRecord {

	private int id;
	private String itemId;
	private double newRebate;//特卖时的返佣
	private double newRetailPrice;//特卖时的价格
	private double oldRebate;//原来的返佣
	private double oldRetailPrice;//原来的零售价
	private double linePrice;//划线价
	private String picPath;//
	private int year;//年
	private int week;//第几周
	private int sort;
	private String goodsId;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	private String createTime;
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public double getNewRebate() {
		return newRebate;
	}
	public void setNewRebate(double newRebate) {
		this.newRebate = newRebate;
	}
	public double getNewRetailPrice() {
		return newRetailPrice;
	}
	public void setNewRetailPrice(double newRetailPrice) {
		this.newRetailPrice = newRetailPrice;
	}
	public double getOldRebate() {
		return oldRebate;
	}
	public void setOldRebate(double oldRebate) {
		this.oldRebate = oldRebate;
	}
	public double getOldRetailPrice() {
		return oldRetailPrice;
	}
	public void setOldRetailPrice(double oldRetailPrice) {
		this.oldRetailPrice = oldRetailPrice;
	}
	public double getLinePrice() {
		return linePrice;
	}
	public void setLinePrice(double linePrice) {
		this.linePrice = linePrice;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
