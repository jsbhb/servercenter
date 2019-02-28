package com.zm.goods.pojo.po;

import java.util.List;

import com.zm.goods.pojo.GoodsRebateEntity;
import com.zm.goods.pojo.GoodsTagBindEntity;
import com.zm.goods.processWarehouse.model.WarehouseModel;

/**
 * @fun 后台新增商品时 同时保存 多张表/单个商品查看也用该对象
 * @author user
 *
 */
public class BackGoodsPO {

	private List<Items> itemsList;//供应商商品
	private GoodsSpecs specs;//商品规格
	private Goods goods;//商品
	private GoodsSpecsTradePattern goodsSpecsTradePattern;
	private List<GoodsPricePO> priceList;//价格
	private List<WarehouseModel> stockList;//库存
	private List<GoodsTagBindEntity> tagList;//标签
	private List<GoodsRebateEntity> rebateList;//返佣
	public List<GoodsTagBindEntity> getTagList() {
		return tagList;
	}
	public void setTagList(List<GoodsTagBindEntity> tagList) {
		this.tagList = tagList;
	}
	public List<GoodsRebateEntity> getRebateList() {
		return rebateList;
	}
	public void setRebateList(List<GoodsRebateEntity> rebateList) {
		this.rebateList = rebateList;
	}
	public List<GoodsPricePO> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<GoodsPricePO> priceList) {
		this.priceList = priceList;
	}
	public List<WarehouseModel> getStockList() {
		return stockList;
	}
	public void setStockList(List<WarehouseModel> stockList) {
		this.stockList = stockList;
	}
	public GoodsSpecs getSpecs() {
		return specs;
	}
	public List<Items> getItemsList() {
		return itemsList;
	}
	public void setItemsList(List<Items> itemsList) {
		this.itemsList = itemsList;
	}
	public void setSpecs(GoodsSpecs specs) {
		this.specs = specs;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public GoodsSpecsTradePattern getGoodsSpecsTradePattern() {
		return goodsSpecsTradePattern;
	}
	public void setGoodsSpecsTradePattern(GoodsSpecsTradePattern goodsSpecsTradePattern) {
		this.goodsSpecsTradePattern = goodsSpecsTradePattern;
	}
	
}
