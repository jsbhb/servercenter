package com.zm.goods.pojo;

import com.zm.goods.common.Pagination;

public class GoodsRatioPlatformEntity extends Pagination {
	private Integer id;
	private String ratioPlatformName;// 比价平台名称
	private Integer isUse;// 是否使用，0：是;1：否
	private String createTime;// 创建时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRatioPlatformName() {
		return ratioPlatformName;
	}
	public void setRatioPlatformName(String ratioPlatformName) {
		this.ratioPlatformName = ratioPlatformName;
	}
	public Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
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
}
