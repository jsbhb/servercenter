/**  
 * Project Name:cardmanager  
 * File Name:SpecsTemplate.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Dec 4, 20173:48:50 PM  
 *  
 */
package com.zm.goods.pojo;

import java.util.List;

import com.zm.goods.pojo.base.Pagination;

/**
 * ClassName: SpecsTemplateEntity <br/>
 * Function: 规格模板. <br/>
 * date: Dec 4, 2017 3:48:50 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class SpecsTemplateEntity extends Pagination {
	private int id;
	private String name;
	private String createTime;
	private String updateTime;
	private String opt;
	private List<SpecsEntity> specs;
	private int status;

	public SpecsTemplateEntity() {
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<SpecsEntity> getSpecs() {
		return specs;
	}

	public void setSpecs(List<SpecsEntity> specs) {
		this.specs = specs;
	}

}
