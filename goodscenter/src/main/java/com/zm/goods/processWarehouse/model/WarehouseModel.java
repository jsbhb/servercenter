package com.zm.goods.processWarehouse.model;

public class WarehouseModel {
	
	private Integer id;

	private String itemId;
	
	private Integer fxqty;
	
	private Integer frozenqty;
	
	private Integer lockedqty;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getFxqty() {
		return fxqty;
	}

	public void setFxqty(Integer fxqty) {
		this.fxqty = fxqty;
	}

	public Integer getFrozenqty() {
		return frozenqty;
	}

	public void setFrozenqty(Integer frozenqty) {
		this.frozenqty = frozenqty;
	}

	public Integer getLockedqty() {
		return lockedqty;
	}

	public void setLockedqty(Integer lockedqty) {
		this.lockedqty = lockedqty;
	}
	
}
