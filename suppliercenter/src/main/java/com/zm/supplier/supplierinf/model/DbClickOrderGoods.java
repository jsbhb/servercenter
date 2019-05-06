package com.zm.supplier.supplierinf.model;

import java.util.Calendar;
import java.util.Date;

import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.util.DateUtil;
import com.zm.supplier.util.JSONUtil;

/**
 * @fun 重庆双击科技订单商品
 * @author user
 *
 */
public class DbClickOrderGoods {

	private String supplies_sn;
	private String goods_name;
	private String term;
	private String goods_attr_number;
	private String goods_attr_name;
	private String goods_price;
	private String goods_costprice;

	public DbClickOrderGoods(OrderGoods goods) {
		supplies_sn = goods.getItemCode();
		goods_name = goods.getItemName();
		Date date = DateUtil.getSpecifiedTime(new Date(), 1, Calendar.YEAR);
		String startTime = DateUtil.getDateString(new Date(), "yyyy-MM");
		String endTime = DateUtil.getDateString(date, "yyyy-MM");
		term = startTime + "/" + endTime;
		goods_attr_number = goods.getItemQuantity() + "";
		goods_attr_name = goods.getItemQuantity() + "";
		goods_price = goods.getItemPrice() + "";
		goods_costprice = goods.getProxyPrice() + "";
	}

	public String getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}

	public String getGoods_costprice() {
		return goods_costprice;
	}

	public void setGoods_costprice(String goods_costprice) {
		this.goods_costprice = goods_costprice;
	}

	public String getSupplies_sn() {
		return supplies_sn;
	}

	public void setSupplies_sn(String supplies_sn) {
		this.supplies_sn = supplies_sn;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getGoods_attr_number() {
		return goods_attr_number;
	}

	public void setGoods_attr_number(String goods_attr_number) {
		this.goods_attr_number = goods_attr_number;
	}

	public String getGoods_attr_name() {
		return goods_attr_name;
	}

	public void setGoods_attr_name(String goods_attr_name) {
		this.goods_attr_name = goods_attr_name;
	}

	@Override
	public String toString() {
		return JSONUtil.toJson(this);
	}

}
