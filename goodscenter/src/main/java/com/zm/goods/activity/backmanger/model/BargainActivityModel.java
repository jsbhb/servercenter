package com.zm.goods.activity.backmanger.model;

import java.util.List;


public class BargainActivityModel extends BaseActivityModel {

	private List<BargainActivityGoodsModel> itemList;

	public List<BargainActivityGoodsModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<BargainActivityGoodsModel> itemList) {
		this.itemList = itemList;
	}
}
