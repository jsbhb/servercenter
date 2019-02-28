package com.zm.goods.pojo.bo;

import java.util.List;

import com.zm.goods.pojo.po.Goods;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.po.Items;

/**
 * @fun 商品新增时，根据条码来查询是否存在，存在返回，填充数据
 * @author user
 *
 */
public class GoodsRender4New {

	private Goods goods;
	private List<GoodsSpecs> specsList;
	private List<Items> itemList;
	private List<GoodsSpecsTradePattern> specsTpList;
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public List<GoodsSpecs> getSpecsList() {
		return specsList;
	}
	public void setSpecsList(List<GoodsSpecs> specsList) {
		this.specsList = specsList;
	}
	public List<Items> getItemList() {
		return itemList;
	}
	public void setItemList(List<Items> itemList) {
		this.itemList = itemList;
	}
	public List<GoodsSpecsTradePattern> getSpecsTpList() {
		return specsTpList;
	}
	public void setSpecsTpList(List<GoodsSpecsTradePattern> specsTpList) {
		this.specsTpList = specsTpList;
	}
	
}
