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
 * ClassName: SecondCatalogEntity <br/>
 * Function: 二级分类. <br/>
 * date: Nov 10, 2017 11:38:07 AM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class SecondCatalogEntity {
	private int id;
	private String firstId;
	private String secondId;
	private String name;
	private String createTime;
	private String updateTime;
	private String opt;
	private Integer sort;
	private Integer status;
	private List<ThirdCatalogEntity> thirds;

	public SecondCatalogEntity() {

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

	public String getSecondId() {
		return secondId;
	}

	public void setSecondId(String secondId) {
		this.secondId = secondId;
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

	public List<ThirdCatalogEntity> getThirds() {
		return thirds;
	}

	public void setThirds(List<ThirdCatalogEntity> thirds) {
		this.thirds = thirds;
	}
}
