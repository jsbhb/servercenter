package com.zm.goods.pojo;

import io.swagger.annotations.ApiModel;

@ApiModel(value="layout",description="模块布局对象，如果先获取模块再获取数据，带上id,如果一次性获取，传空对象")
public class Layout {

	private Integer id;
	
	private String page;
	
	private String code;
	
	private String config;
	
	private Integer type;
	
	private Integer show;
	
	private String description;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public Integer getShow() {
		return show;
	}

	public void setShow(Integer show) {
		this.show = show;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return "Layout [id=" + id + ", page=" + page + ", code=" + code + ", config=" + config + ", show=" + show
				+ ", description=" + description + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", opt=" + opt + "]";
	}
	
}
