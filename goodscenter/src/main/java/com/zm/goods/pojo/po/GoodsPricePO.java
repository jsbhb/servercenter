package com.zm.goods.pojo.po;
/**
 * @fun 对应kj_goods_price表数据
 * @author user
 *
 */
public class GoodsPricePO {

	private Integer id;
	private String itemId;
	private Integer min;
	private Integer max;
	private double costPrice;
	private double internalPrice;
	private String createTime;
	private String updateTime;
	private String opt;
	private String specsTpId;

	public String getSpecsTpId() {
		return specsTpId;
	}

	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
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

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getInternalPrice() {
		return internalPrice;
	}

	public void setInternalPrice(double internalPrice) {
		this.internalPrice = internalPrice;
	}
}
