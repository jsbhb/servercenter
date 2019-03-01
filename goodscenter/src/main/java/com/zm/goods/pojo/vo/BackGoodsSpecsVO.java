package com.zm.goods.pojo.vo;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zm.goods.pojo.TempSpecs;
import com.zm.goods.utils.JSONUtil;

/**
 * @fun 后台展示规格类
 * @author user
 *
 */
public class BackGoodsSpecsVO {

	private String specsTpId;

	private double tariff;

	private double incrementTax;

	private double exciseTax;

	private String unit;

	private String info;

	private int weight;
	
	private int status;

	private int stock;

	private double retailPrice;

	private double linePrice;
	
	private String tagNames;

	private int saleNum;

	private String carton;
	
	private int conversion;
	
	private boolean welfare;
	
	private boolean distribution;
	
	private int display;
	
	private double distributionPrice;
	
	private double instantRatio;
	
	private String upshelfTime;
	
	private String createTime;
	
	private String opt;
	
	public double getInstantRatio() {
		return instantRatio;
	}

	public void setInstantRatio(double instantRatio) {
		this.instantRatio = instantRatio;
	}

	public String getUpshelfTime() {
		return upshelfTime;
	}

	public void setUpshelfTime(String upshelfTime) {
		this.upshelfTime = upshelfTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public double getDistributionPrice() {
		return distributionPrice;
	}

	public void setDistributionPrice(double distributionPrice) {
		this.distributionPrice = distributionPrice;
	}

	public boolean isWelfare() {
		return welfare;
	}

	public void setWelfare(boolean welfare) {
		this.welfare = welfare;
	}

	public boolean isDistribution() {
		return distribution;
	}

	public void setDistribution(boolean distribution) {
		this.distribution = distribution;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

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

	public String getTagNames() {
		return tagNames;
	}

	public void setTagNames(String tagNames) {
		this.tagNames = tagNames;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

}
