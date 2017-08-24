package com.zm.goods.pojo;

public class PriceContrast {

	private Integer id;
	
	private String itemId;
	
	private Double sh_price;
	
	private Double bj_price;
	
	private Double price;
	
	private Double reserve;
	
	private String contrastTime;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;

	public String getContrastTime() {
		return contrastTime;
	}

	public void setContrastTime(String contrastTime) {
		this.contrastTime = contrastTime;
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

	public Double getSh_price() {
		return sh_price;
	}

	public void setSh_price(Double sh_price) {
		this.sh_price = sh_price;
	}

	public Double getBj_price() {
		return bj_price;
	}

	public void setBj_price(Double bj_price) {
		this.bj_price = bj_price;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getReserve() {
		return reserve;
	}

	public void setReserve(Double reserve) {
		this.reserve = reserve;
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

	@Override
	public String toString() {
		return "PriceContrast [id=" + id + ", itemId=" + itemId + ", sh_price=" + sh_price + ", bj_price=" + bj_price
				+ ", price=" + price + ", reserve=" + reserve + ", contrastTime=" + contrastTime + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", opt=" + opt + "]";
	}
	
}
