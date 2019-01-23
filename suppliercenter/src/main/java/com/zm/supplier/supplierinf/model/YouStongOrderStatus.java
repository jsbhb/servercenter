package com.zm.supplier.supplierinf.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zm.supplier.pojo.OrderStatus;

public class YouStongOrderStatus {

	@JsonProperty("ParterOrderNo")
	private String parterOrderNo;

	@JsonProperty("OrderNo")
	private String orderNo;

	@JsonProperty("TotalOrderAmt")
	private String totalOrderAmt;

	@JsonProperty("NeedToPayAmt")
	private String needToPayAmt;

	@JsonProperty("Status")
	private Integer status;

	@JsonProperty("StatusDesc")
	private String statusDesc;

	@JsonProperty("splitted")
	private boolean Splitted;

	@JsonProperty("Packages")
	private List<Packages> packagesList;

	public String getParterOrderNo() {
		return parterOrderNo;
	}

	public void setParterOrderNo(String parterOrderNo) {
		this.parterOrderNo = parterOrderNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTotalOrderAmt() {
		return totalOrderAmt;
	}

	public void setTotalOrderAmt(String totalOrderAmt) {
		this.totalOrderAmt = totalOrderAmt;
	}

	public String getNeedToPayAmt() {
		return needToPayAmt;
	}

	public void setNeedToPayAmt(String needToPayAmt) {
		this.needToPayAmt = needToPayAmt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public boolean isSplitted() {
		return Splitted;
	}

	public void setSplitted(boolean splitted) {
		Splitted = splitted;
	}

	public List<Packages> getPackagesList() {
		return packagesList;
	}

	public void setPackagesList(List<Packages> packagesList) {
		this.packagesList = packagesList;
	}

	public Set<OrderStatus> convertToOrderStatus() {
		Set<OrderStatus> set = new HashSet<OrderStatus>();
		OrderStatus orderStatus = null;
		if (status == 3) {
			StringBuilder sb = null;
			for (int i = 0; i < packagesList.size(); i++) {
				orderStatus = new OrderStatus();
				sb = new StringBuilder();
				orderStatus.setOrderId(parterOrderNo);
				String orderNo_tmp = i == 0 ? orderNo : orderNo + "-" + i;
				orderStatus.setThirdOrderId(orderNo_tmp);
				orderStatus.setStatus(status + "");
				orderStatus.setExpressId(packagesList.get(i).getTrackingNo());
				orderStatus.setLogisticsName(packagesList.get(i).getExpressName());
				for (Items items : packagesList.get(i).getItemsList()) {
					sb.append(items.getsKUNo() + ",");
				}
				orderStatus.setItemCode(sb.toString().substring(0, sb.length() - 1));
				set.add(orderStatus);
			}
		} else {
			orderStatus = new OrderStatus();
			orderStatus.setOrderId(parterOrderNo);
			orderStatus.setThirdOrderId(orderNo);
			orderStatus.setStatus(status + "");
			set.add(orderStatus);
		}
		return set;
	}
}

class Items {

	@JsonProperty("SKUNo")
	private String sKUNo;

	@JsonProperty("Qty")
	private String qty;

	public String getsKUNo() {
		return sKUNo;
	}

	public void setsKUNo(String sKUNo) {
		this.sKUNo = sKUNo;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

}

class Packages {

	@JsonProperty("ExpressName")
	private String expressName;

	@JsonProperty("TrackingNo")
	private String trackingNo;

	@JsonProperty("Items")
	private List<Items> itemsList;

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public List<Items> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<Items> itemsList) {
		this.itemsList = itemsList;
	}

}
