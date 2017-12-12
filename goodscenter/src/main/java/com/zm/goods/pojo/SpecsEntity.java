/**  
 * Project Name:cardmanager  
 * File Name:SpecsEntity.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Dec 4, 20173:48:17 PM  
 *  
 */
package com.zm.goods.pojo;

import java.util.List;

/**
 * ClassName: SpecsEntity <br/>
 * Function: 规格实体. <br/>
 * date: Dec 4, 2017 3:48:17 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class SpecsEntity {
	private int templateId;
	private int id;
	private String name;
	private List<SpecsValueEntity> values;
	private String createTime;
	private String updateTime;
	private String opt;

	public SpecsEntity(String name,List<SpecsValueEntity> values) {
		this.name = name;
		this.values = values;
	}

	public SpecsEntity() {
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SpecsValueEntity> getValues() {
		return values;
	}

	public void setValues(List<SpecsValueEntity> values) {
		this.values = values;
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
