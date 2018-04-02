/**  
 * Project Name:cardmanager  
 * File Name:BrandEntity.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Nov 9, 20177:37:40 PM  
 *  
 */
package com.zm.goods.pojo;

import com.zm.goods.pojo.base.Pagination;

/**
 * ClassName: TagFuncEntity <br/>
 * Function: 功能. <br/>
 * date: Nov 9, 2017 7:37:40 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class TagFuncEntity extends Pagination{

	private int id;
	private String funcName;
	private String createTime;
	private String updateTime;
	private String opt;

	public TagFuncEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
}
