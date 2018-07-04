/**  
 * Project Name:cardmanager  
 * File Name:GoodsEntity.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Nov 12, 201710:28:15 PM  
 *  
 */
package com.zm.goods.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ClassName: EshopGoodsStockEntity <br/>
 * Function: 商品实体 <br/>
 * date: Nov 12, 2017 10:28:15 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EshopGoodsStockEntity{
	private Integer id;
	private Integer mallId;
	private Integer gradeId;
	private String itemId;
	private String loc;
	private Integer isDefault;
	private Integer quantity;
	private String createTime;// 创建时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMallId() {
		return mallId;
	}
	public void setMallId(Integer mallId) {
		this.mallId = mallId;
	}
	public Integer getGradeId() {
		return gradeId;
	}
	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
