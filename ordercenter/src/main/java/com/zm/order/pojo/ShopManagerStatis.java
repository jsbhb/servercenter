package com.zm.order.pojo;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @fun 前端统计页面数据结构
 * @author user
 *
 */
public class ShopManagerStatis {

	private List<GoodsView> goodsList;//商品排行榜
	private String orderNum;//订单总署总和
	private String rebate;//返佣总署总和
	private String pageView;//浏览次数总和
	private String visitView;//访客人数总和
	private List<String> rebateList;//返佣明细/天
	private List<String> orderNumList;//订单明细/天
	private List<String> pageViewList;//浏览明细/天
	private List<String> visitViewList;//访客明细/天
	
	public void init(int time) {
		//初始化返佣
		if(rebateList == null){
			rebateList = Collections.nCopies(time, "0");
		}
		if(rebateList.size() < time){
			List<String> tmp = new ArrayList<>(Collections.nCopies(time - rebateList.size(), "0"));
			tmp.addAll(rebateList);
			rebateList = tmp;
			tmp = null;
		}
		double tmpd = rebateList.stream().map(Double::new).mapToDouble(d->d).sum();
		DecimalFormat formater = new DecimalFormat();
		formater.setMaximumFractionDigits(2);
		formater.setGroupingSize(0);
		formater.setRoundingMode(RoundingMode.FLOOR);
		rebate = formater.format(tmpd);
		//初始订单量
		if(orderNumList == null){
			orderNumList = Collections.nCopies(time, "0");
		}
		if(orderNumList.size() < time){
			List<String> tmp = new ArrayList<>(Collections.nCopies(time - orderNumList.size(), "0"));
			tmp.addAll(orderNumList);
			orderNumList = tmp;
			tmp = null;
		}
		orderNum = orderNumList.stream().map(Integer::new).mapToInt(d->d).sum() +"";
		//初始化浏览量
		if(pageViewList == null){
			pageViewList = Collections.nCopies(time, "0");
		}
		if(pageViewList.size() < time){
			List<String> tmp = new ArrayList<>(Collections.nCopies(time - pageViewList.size(), "0"));
			tmp.addAll(pageViewList);
			pageViewList = tmp;
			tmp = null;
		}
		pageView = pageViewList.stream().map(Integer::new).mapToInt(d->d).sum() +"";
		//初始化访客
		if(visitViewList == null){
			visitViewList = Collections.nCopies(time, "0");
		}
		if(visitViewList.size() < time){
			List<String> tmp = new ArrayList<>(Collections.nCopies(time - visitViewList.size(), "0"));
			tmp.addAll(visitViewList);
			visitViewList = tmp;
			tmp = null;
		}
		visitView = visitViewList.stream().map(Integer::new).mapToInt(d->d).sum() +"";
		//商品排序
		if(goodsList != null){
			goodsList = goodsList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		}
	}
	
	public void createGoodsViewList(String goodsName,int pv,int uv,int ogn, String goodsId){
		if(this.goodsList == null){
			this.goodsList = new ArrayList<>();
		}
		GoodsView gv = new GoodsView();
		gv.setGoodsName(goodsName);
		gv.setGoodsPageView(pv+"");
		gv.setGoodsVisitView(uv+"");
		gv.setOrderGoodsNum(ogn+"");
		gv.setGoodsId(goodsId);
		goodsList.add(gv);
	}
	
	public List<GoodsView> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsView> goodsList) {
		this.goodsList = goodsList;
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
	public List<String> getRebateList() {
		return rebateList;
	}
	public void setRebateList(List<String> rebateList) {
		this.rebateList = rebateList;
	}
	public List<String> getOrderNumList() {
		return orderNumList;
	}
	public void setOrderNumList(List<String> orderNumList) {
		this.orderNumList = orderNumList;
	}
	public List<String> getPageViewList() {
		return pageViewList;
	}
	public void setPageViewList(List<String> pageViewList) {
		this.pageViewList = pageViewList;
	}
	public List<String> getVisitViewList() {
		return visitViewList;
	}
	public void setVisitViewList(List<String> visitViewList) {
		this.visitViewList = visitViewList;
	}
	class GoodsView implements Comparable<GoodsView>{
		//商品名称
		private String goodsName;
		//订单数量
		private String orderGoodsNum;
		//访问人数
		private String goodsVisitView;
		//访问次数
		private String goodsPageView;
		//商品ID
		private String goodsId;
		public String getGoodsId() {
			return goodsId;
		}
		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}
		public String getGoodsName() {
			return goodsName;
		}
		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}
		public String getOrderGoodsNum() {
			return orderGoodsNum;
		}
		public void setOrderGoodsNum(String orderGoodsNum) {
			this.orderGoodsNum = orderGoodsNum;
		}
		public String getGoodsVisitView() {
			return goodsVisitView;
		}
		public void setGoodsVisitView(String goodsVisitView) {
			this.goodsVisitView = goodsVisitView;
		}
		public String getGoodsPageView() {
			return goodsPageView;
		}
		public void setGoodsPageView(String goodsPageView) {
			this.goodsPageView = goodsPageView;
		}
		@Override
		public int compareTo(GoodsView o) {
			return orderGoodsNum.compareTo(o.getOrderGoodsNum());
		}
	}
}
