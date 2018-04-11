/**  
 * Project Name:cardmanager  
 * File Name:GoodsEntity.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Nov 12, 201710:28:15 PM  
 *  
 */
package com.zm.goods.pojo;

import com.zm.goods.common.Pagination;

/**
 * ClassName: GoodsEntity <br/>
 * Function: 商品实体 <br/>
 * date: Nov 12, 2017 10:28:15 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class GoodsRebateEntity extends Pagination {
	private Integer id;
	private String itemId;
	private Integer gradeType;// 客户类型
	private double proportion;// 返佣比例
	private String remark;// 备注
	private String opt;
	public Integer getId() {
		return id;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Integer getGradeType() {
		return gradeType;
	}
	public void setGradeType(Integer gradeType) {
		this.gradeType = gradeType;
	}
	public double getProportion() {
		return proportion;
	}
	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
