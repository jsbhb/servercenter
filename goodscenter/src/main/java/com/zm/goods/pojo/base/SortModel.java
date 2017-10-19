package com.zm.goods.pojo.base;

/**
 * @author wqy
 * @fun 排序规则的model
 * @date 2017年6月5日
 */
public class SortModel {

	//需要排序的字段名
	private String sortField;
	//排序规则desc：降序；asc：升序
	private String sortRule;
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getSortRule() {
		return sortRule;
	}
	public void setSortRule(String sortRule) {
		this.sortRule = sortRule;
	}
	@Override
	public String toString() {
		return "SortModel [sortField=" + sortField + ", sortRule=" + sortRule + "]";
	}
	
}
