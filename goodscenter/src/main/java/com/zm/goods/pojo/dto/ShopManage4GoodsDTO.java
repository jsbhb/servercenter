package com.zm.goods.pojo.dto;

import com.zm.goods.annotation.valid.Contain;
import com.zm.goods.annotation.valid.Min;
import com.zm.goods.pojo.base.Pagination;

/**
 * @fun 接收店铺管理里商品列表的查询条件的model
 * @author user
 * sortName : 1、rebate 根据佣金排序
 * 			  2、sale_num 根据销量排序
 *            3、fxqty 根据库存排序
 *            4、update_time 根据上架时间排序
 * sortRule ： desc 降序
 * 			  asc 升序
 */
public class ShopManage4GoodsDTO extends Pagination{

	private String goodsName;
	@Min(value = 1, message = "店铺ID错误")
	private int shopId;
	@Min(value = 0, message = "分级类型参数错误")
	private int gradeType;
	@Contain(value = {"rebate","sale_num","fxqty","update_time"}, ignoreCase = false, message = "排序字段错误错误")
	private String sortName;
	@Contain(value = {"desc","asc"}, ignoreCase = true, message = "排序类型错误错误")
	private String sortRule;
	private Integer tagId;//标签ID
	private String first;//一级分类
	
	public void init(){
		super.init();
		if("update_time".equals(sortName)){
			sortName = "gi.update_time";
		}
	}
	
	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public int getGradeType() {
		return gradeType;
	}
	public void setGradeType(int gradeType) {
		this.gradeType = gradeType;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public String getSortRule() {
		return sortRule;
	}
	public void setSortRule(String sortRule) {
		this.sortRule = sortRule;
	}
}
