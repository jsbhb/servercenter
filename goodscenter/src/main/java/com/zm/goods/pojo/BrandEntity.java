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
 * ClassName: BrandEntity <br/>
 * Function: 品牌. <br/>
 * date: Nov 9, 2017 7:37:40 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class BrandEntity extends Pagination{

	private int id;
	private String brandId;
	private String brand;
	private String attr;
	private String createTime;
	private String updateTime;
	private String opt;

	public BrandEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
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
