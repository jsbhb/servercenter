package com.zm.order.pojo.bo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zm.order.pojo.GoodsTagEntity;
import com.zm.order.utils.JSONUtil;

/**
 * @fun 前端展示规格类
 * @author user
 *
 */
public class GoodsSpecsVO {

	private String specsTpId;

	private double tariff;

	private double incrementTax;

	private double exciseTax;

	private String unit;

	private String itemId;

	private String info;

	private int weight;

	private int stock;
	//供应商信息为null可忽略
	@JsonInclude(Include.NON_NULL) 
	private int supplierId;
	@JsonInclude(Include.NON_NULL) 
	private String supplierName;

	private double retailPrice;

	private double linePrice;
	
	private int minBuyCount;//最小购买量

	private List<GoodsTagEntity> tagList;

	private int saleNum;

	private String carton;
	
	private int conversion;
	
	private boolean bigSale;
	
	public void infoFilter() {
		if (info != null && !"".equals(info.trim())) {
			List<TempSpecs> temList = JSONUtil.parse(info, new TypeReference<List<TempSpecs>>() {
			});
			StringBuilder sb = new StringBuilder("{");
			for (TempSpecs temp : temList) {
				sb.append("\"" + temp.getSkV() + "\":\"" + temp.getSvV() + "\",");
			}
			info = sb.substring(0, sb.length() - 1);
			info = info + "}";
		}
	}
	
	public void init() {
		saleNum = saleNum * 11;
		infoFilter();
	}

	public boolean isBigSale() {
		return bigSale;
	}

	public void setBigSale(boolean bigSale) {
		this.bigSale = bigSale;
	}

	public int getConversion() {
		return conversion;
	}

	public void setConversion(int conversion) {
		this.conversion = conversion;
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public int getMinBuyCount() {
		return minBuyCount;
	}

	public void setMinBuyCount(int minBuyCount) {
		this.minBuyCount = minBuyCount;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSpecsTpId() {
		return specsTpId;
	}

	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
	}

	public double getTariff() {
		return tariff;
	}

	public void setTariff(double tariff) {
		this.tariff = tariff;
	}

	public double getIncrementTax() {
		return incrementTax;
	}

	public void setIncrementTax(double incrementTax) {
		this.incrementTax = incrementTax;
	}

	public double getExciseTax() {
		return exciseTax;
	}

	public void setExciseTax(double exciseTax) {
		this.exciseTax = exciseTax;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public double getLinePrice() {
		return linePrice;
	}

	public void setLinePrice(double linePrice) {
		this.linePrice = linePrice;
	}

	public List<GoodsTagEntity> getTagList() {
		return tagList;
	}

	public void setTagList(List<GoodsTagEntity> tagList) {
		this.tagList = tagList;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

}
