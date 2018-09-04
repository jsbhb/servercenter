package com.zm.goods.pojo.bo;

/**
 * @fun 商品生命周期实体类
 * @author user
 *
 */
public class GoodsLifeCycleModel {

	private String itemId;
	private int status;
	private int isFx;
	private String remark;
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsFx() {
		return isFx;
	}

	public void setIsFx(int isFx) {
		this.isFx = isFx;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
