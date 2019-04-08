package com.zm.supplier.supplierinf.model;

import java.util.ArrayList;
import java.util.List;

import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.util.CalculationUtils;

public class DolphinOrder {

	private String orderSn;
	private String consignee;
	private String country;
	private String province;
	private String city;
	private String district;
	private String address;
	private String zipcode;
	private String tel;
	private String mobile;
	private String idCardNumber;
	private String siteType;
	private String siteName;
	private String consumerNote;
	private String shippingFee;
	private String orderAmount;// 订单金额=商品单价*数量+运费
	private String moneyPaid;// 顾客实际支付金额
	private String discount;
	private String paymentInfoName;
	private String paymentInfoIdCardNumber;
	private String paymentInfoMethod;
	private String paymentInfoNumber;
	private String paymentAccount;
	private String isCheck;
	private List<DolphinItems> items;

	public DolphinOrder(OrderInfo info) {
		orderSn = info.getOrderId();
		consignee = info.getOrderDetail().getReceiveName();
		country = "中国";
		province = info.getOrderDetail().getReceiveProvince();
		city = info.getOrderDetail().getReceiveCity();
		district = info.getOrderDetail().getReceiveArea();
		address = info.getOrderDetail().getReceiveAddress();
		zipcode = info.getOrderDetail().getReceiveZipCode() == null ? "" : info.getOrderDetail().getReceiveZipCode();
		tel = info.getOrderDetail().getReceivePhone();
		mobile = info.getOrderDetail().getReceivePhone();
		idCardNumber = info.getOrderDetail().getCustomerIdNum();
		siteType = "中国供销海外购";
		siteName = "中国供销海外购";
		consumerNote = info.getRemark() == null ? "" : info.getRemark();
		shippingFee = info.getOrderDetail().getPostFee() == null ? "0" : info.getOrderDetail().getPostFee() + "";
		orderAmount = info.getOrderGoodsList().stream()
				.mapToDouble(g -> CalculationUtils.mul(g.getActualPrice(), g.getItemQuantity())).sum() + "";
		moneyPaid = info.getOrderDetail().getPayment() + "";
		discount = info.getOrderDetail().getDisAmount() == null ? "0" : info.getOrderDetail().getDisAmount() + "";
		paymentInfoName = info.getOrderDetail().getCustomerName();
		paymentInfoIdCardNumber = info.getOrderDetail().getCustomerIdNum();
		if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {// 支付方式
			paymentInfoMethod = "支付宝";
		} else if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
			paymentInfoMethod = "微信支付";
		}
		paymentInfoNumber = info.getOrderDetail().getPayNo();
		paymentAccount = "htdolphin@163.com";
		isCheck = "yes";
		items = new ArrayList<>();
		DolphinItems item = null;
		for (OrderGoods goods : info.getOrderGoodsList()) {
			item = new DolphinItems();
			item.setGoodsName(goods.getItemName());
			item.setGoodsPrice(goods.getItemPrice() + "");
			item.setGoodsSn(goods.getItemCode());
			item.setQuantity(goods.getItemQuantity() + "");
			items.add(item);
		}
	}

	public DolphinOrder() {
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getConsumerNote() {
		return consumerNote;
	}

	public void setConsumerNote(String consumerNote) {
		this.consumerNote = consumerNote;
	}

	public String getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(String shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getMoneyPaid() {
		return moneyPaid;
	}

	public void setMoneyPaid(String moneyPaid) {
		this.moneyPaid = moneyPaid;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getPaymentInfoName() {
		return paymentInfoName;
	}

	public void setPaymentInfoName(String paymentInfoName) {
		this.paymentInfoName = paymentInfoName;
	}

	public String getPaymentInfoIdCardNumber() {
		return paymentInfoIdCardNumber;
	}

	public void setPaymentInfoIdCardNumber(String paymentInfoIdCardNumber) {
		this.paymentInfoIdCardNumber = paymentInfoIdCardNumber;
	}

	public String getPaymentInfoMethod() {
		return paymentInfoMethod;
	}

	public void setPaymentInfoMethod(String paymentInfoMethod) {
		this.paymentInfoMethod = paymentInfoMethod;
	}

	public String getPaymentInfoNumber() {
		return paymentInfoNumber;
	}

	public void setPaymentInfoNumber(String paymentInfoNumber) {
		this.paymentInfoNumber = paymentInfoNumber;
	}

	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public List<DolphinItems> getItems() {
		return items;
	}

	public void setItems(List<DolphinItems> items) {
		this.items = items;
	}
}

class DolphinItems {
	private String quantity;
	private String goodsName;
	private String goodsSn;
	private String goodsPrice;

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

}
