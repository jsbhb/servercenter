/**  
 * Project Name:cardmanager  
 * File Name:SpecsValuesEntity.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Dec 4, 20173:56:46 PM  
 *  
 */
package com.zm.goods.pojo;

/**
 * ClassName: SpecsValueEntity <br/>
 * Function: 规格值. <br/>
 * date: Dec 4, 2017 3:56:46 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class SpecsValueEntity {
	private int specsId;
	private int id;
	private String value;
	private String createTime;
	private String updateTime;
	private String opt;

	public SpecsValueEntity() {
	}

	public SpecsValueEntity(String value) {
		this.value = value;
	}

	public int getSpecsId() {
		return specsId;
	}

	public void setSpecsId(int specsId) {
		this.specsId = specsId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
