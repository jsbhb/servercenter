package com.zm.order.pojo;
/**
 * @fun 微店首页统计数据
 * @author user
 *
 */
public class ShopManagerStatisIndex {

	private String visitView;
	private String orderNum;
	private String rebate;
	private String pageView;
	public String getPageView() {
		return pageView;
	}
	public void setPageView(String pageView) {
		this.pageView = pageView;
	}
	public String getVisitView() {
		return visitView;
	}
	public void setVisitView(String visitView) {
		this.visitView = visitView;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getRebate() {
		return rebate;
	}
	public void setRebate(String rebate) {
		this.rebate = rebate;
	}
}
