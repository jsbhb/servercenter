package com.zm.order.pojo.bo;

/**
 * ClassName: OrderGoods <br/>
 * Function: 订单商品通过商品中心校验完价格库存等信息后返回给订单中心，用于订单中心吧商品数据补全. <br/>
 * date: Aug 11, 2017 3:16:08 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public class OrderGoodsCompleteBO {

	private String itemId;

	private String sku;

	private String itemCode;

	private String specsTpId;

	private Integer conversion;

	private String unit;

	private double exciseTax;

	private double incrementTax;

	private String carton;

	private int weight;

	private Integer supplierId;

	private String supplierName;

	private double itemPrice;

	public double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public double getExciseTax() {
		return exciseTax;
	}

	public void setExciseTax(double exciseTax) {
		this.exciseTax = exciseTax;
	}

	public double getIncrementTax() {
		return incrementTax;
	}

	public void setIncrementTax(double incrementTax) {
		this.incrementTax = incrementTax;
	}

	public String getSpecsTpId() {
		return specsTpId;
	}

	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getConversion() {
		return conversion;
	}

	public void setConversion(Integer conversion) {
		this.conversion = conversion;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}
}