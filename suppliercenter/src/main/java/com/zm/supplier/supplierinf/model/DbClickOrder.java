package com.zm.supplier.supplierinf.model;

import java.util.Date;

import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.util.CalculationUtils;
import com.zm.supplier.util.DateUtil;
/**
 * @fun 重庆双击科技订单类
 * @author user
 *
 */
public class DbClickOrder {

	private String app_id;
	private String version = "1.0";
	private String sign;
	private String order_sn;
	private String add_time;
	private String pay_time;
	private String pay_id;
	private String order_type = "1";
	private String consignee;
	private String nric;
	private String tel;
	private String province;
	private String city;
	private String district;
	private String address;
	private String goods_amount;
	private String shipping_fee;
	private String tax_amount;
	private String order_amount;
	private String pay_sn;
	private String dg_name;
	private String dg_phone;
	private String dg_card;
	private DbClickOrderGoods goods_data;

	public DbClickOrder(OrderInfo info, String appId) {
		app_id = appId;
		order_sn = info.getOrderId();
		Date addTime = DateUtil.getDateByString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		add_time = addTime.getTime()/1000 + "";
		Date date = DateUtil.getDateByString(info.getOrderDetail().getPayTime(), "yyyy-MM-dd HH:mm:ss");
		pay_time = date.getTime()/1000 + "";
		int payType = info.getOrderDetail().getPayType();
		pay_id = payType == Constants.ALI_PAY ? "6" : payType == Constants.WX_PAY ? "5" : "2";
		consignee = info.getOrderDetail().getCustomerName();
		nric = info.getOrderDetail().getCustomerIdNum();
		tel = info.getOrderDetail().getReceivePhone();
		province = info.getOrderDetail().getReceiveProvince();
		city = info.getOrderDetail().getReceiveCity();
		district = info.getOrderDetail().getReceiveArea();
		address = info.getOrderDetail().getReceiveAddress();
		double goodsAmount = info.getOrderGoodsList().stream()
				.mapToDouble(g -> CalculationUtils.mul(g.getItemPrice(), g.getItemQuantity())).sum();
		goods_amount = goodsAmount + "";
		shipping_fee = info.getOrderDetail().getPostFee() + "";
		tax_amount = info.getOrderDetail().getTaxFee()+"";
		order_amount = info.getOrderDetail().getPayment()+"";
		pay_sn = info.getOrderDetail().getPayNo();
		dg_name = info.getOrderDetail().getCustomerName();
		dg_phone = info.getOrderDetail().getCustomerPhone();
		dg_card = info.getOrderDetail().getCustomerIdNum();
		for(OrderGoods goods : info.getOrderGoodsList()){
			goods_data = new DbClickOrderGoods(goods);
		}
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public String getPay_id() {
		return pay_id;
	}

	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGoods_amount() {
		return goods_amount;
	}

	public void setGoods_amount(String goods_amount) {
		this.goods_amount = goods_amount;
	}

	public String getShipping_fee() {
		return shipping_fee;
	}

	public void setShipping_fee(String shipping_fee) {
		this.shipping_fee = shipping_fee;
	}

	public String getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(String tax_amount) {
		this.tax_amount = tax_amount;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	public String getDg_name() {
		return dg_name;
	}

	public void setDg_name(String dg_name) {
		this.dg_name = dg_name;
	}

	public String getDg_phone() {
		return dg_phone;
	}

	public void setDg_phone(String dg_phone) {
		this.dg_phone = dg_phone;
	}

	public String getDg_card() {
		return dg_card;
	}

	public void setDg_card(String dg_card) {
		this.dg_card = dg_card;
	}

	public DbClickOrderGoods getGoods_data() {
		return goods_data;
	}

	public void setGoods_data(DbClickOrderGoods goods_data) {
		this.goods_data = goods_data;
	}
}
