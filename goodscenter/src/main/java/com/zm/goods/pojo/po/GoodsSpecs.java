package com.zm.goods.pojo.po;

/**
 * ClassName: GoodsSpecs <br/>
 * Function: 商品规格. <br/>
 * date: Aug 22, 2017 2:17:27 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public class GoodsSpecs {

	private Integer id;

	private String specsId;

	private String encode;

	private String info;

	private Integer weight;
	
	private String description;
	
	private String createTime;

	private String updateTime;

	private String opt;

	private Integer conversion;//商品转换比例
	
	private String carton;
	
	private String unit;
	
	private String specsGoodsName;
	
	public String getSpecsGoodsName() {
		return specsGoodsName;
	}

	public void setSpecsGoodsName(String specsGoodsName) {
		this.specsGoodsName = specsGoodsName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public Integer getConversion() {
		return conversion;
	}

	public void setConversion(Integer conversion) {
		this.conversion = conversion;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getSpecsId() {
		return specsId;
	}

	public void setSpecsId(String specsId) {
		this.specsId = specsId;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
