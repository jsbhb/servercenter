/**  
 * Project Name:cardmanager  
 * File Name:FirstCatalogEntity.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Nov 10, 201711:38:07 AM  
 *  
 */
package com.zm.goods.pojo;

import java.util.List;

/**
 * ClassName: FirstCatalogEntity <br/>
 * Function: 一级分类. <br/>
 * date: Nov 10, 2017 11:38:07 AM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class FirstCatalogEntity {
	private int id;
	private String firstId;
	private String name;
	private String createTime;
	private String updateTime;
	private String opt;
	private Integer sort;
	private Integer status;
	private List<SecondCatalogEntity> seconds;
	private String accessPath;
	private String tagPath;

	public FirstCatalogEntity() {

	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstId() {
		return firstId;
	}

	public void setFirstId(String firstId) {
		this.firstId = firstId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<SecondCatalogEntity> getSeconds() {
		return seconds;
	}

	public void setSeconds(List<SecondCatalogEntity> seconds) {
		this.seconds = seconds;
	}

	public String getAccessPath() {
		return accessPath;
	}

	public void setAccessPath(String accessPath) {
		this.accessPath = accessPath;
	}

	public String getTagPath() {
		return tagPath;
	}

	public void setTagPath(String tagPath) {
		this.tagPath = tagPath;
	}

}
