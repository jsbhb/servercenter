package com.zm.goods.activity.backmanger.model;

import java.util.List;


public class BargainActivityModel extends BaseActivityModel {

	private List<BargainActivityGoodsModel> itemList;
	private Integer joinPerson;
	private String buyFlg;

	public Integer getJoinPerson() {
		return joinPerson;
	}

	public void setJoinPerson(Integer joinPerson) {
		this.joinPerson = joinPerson;
	}

	public String getBuyFlg() {
		return buyFlg;
	}

	public void setBuyFlg(String buyFlg) {
		this.buyFlg = buyFlg;
	}

	public List<BargainActivityGoodsModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<BargainActivityGoodsModel> itemList) {
		this.itemList = itemList;
	}
}
