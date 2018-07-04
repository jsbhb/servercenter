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
 * ClassName: EshopGoodsInventoryEntity <br/>
 * Function: 商品实体 <br/>
 * date: Nov 12, 2017 10:28:15 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EshopGoodsInventoryEntity{
	private Integer id;
	private Integer mallId;
	private Integer gradeId;
	private String itemId;
	private String loc;
	private Integer sysQty;
	private Integer checkQty;
	private Integer diffQty;
	private Integer operationType;
	private String createTime;// 创建时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	
	public boolean check() {
		if (mallId == null || gradeId == null || itemId == null || loc == null || 
			sysQty == null || checkQty == null || opt == null) {
			return false;
		}
		return true;
	}
	
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
	public Integer getSysQty() {
		return sysQty;
	}
	public void setSysQty(Integer sysQty) {
		this.sysQty = sysQty;
	}
	public Integer getCheckQty() {
		return checkQty;
	}
	public void setCheckQty(Integer checkQty) {
		this.checkQty = checkQty;
	}
	public Integer getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(Integer diffQty) {
		this.diffQty = diffQty;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
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
