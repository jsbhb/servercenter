package com.zm.goods.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zm.goods.utils.JSONUtil;

@JsonInclude(Include.NON_NULL)
public class GoodsDetail {

	@JsonIgnore
	private String id;
	private String itemId;
	private String goodsId;
	private Integer weight;
	private String itemName;
	private Double incrementTax;
	private Double tariff;
	private String info;
	private Double retailPrice;
	private Integer type;
	private String origin;
	private Integer supplierId;
	private String brand;
	private Integer min;
	private Integer max;
	
	public void infoFilter() {
		if (info != null && !"".equals(info.trim())) {
			List<TempSpecs> temList = JSONUtil.parse(info, new TypeReference<List<TempSpecs>>() {
			});
			StringBuilder sb = new StringBuilder("{");
			for (TempSpecs temp : temList) {
				sb.append("\"" + temp.skV + "\":\"" + temp.svV + "\",");
			}
			info = sb.substring(0, sb.length() - 1);
			info = info + "}";
		}
	}
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getIncrementTax() {
		return incrementTax;
	}
	public void setIncrementTax(Double incrementTax) {
		this.incrementTax = incrementTax;
	}
	public Double getTariff() {
		return tariff;
	}
	public void setTariff(Double tariff) {
		this.tariff = tariff;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	
	private static class TempSpecs {
		private String svV;

		private String skV;

		@SuppressWarnings("unused")
		public void setSvV(String svV) {
			this.svV = svV;
		}

		@SuppressWarnings("unused")
		public void setSkV(String skV) {
			this.skV = skV;
		}

		@Override
		public String toString() {
			return "TempSpecs [svV=" + svV + ", skV=" + skV + "]";
		}

	}
}
