package com.zm.goods.processWarehouse.model;

public class WarehouseModel {
	
	private Integer id;

	private String itemId;
	
	private String specsTpId;
	
	private int fxqty;
	
	private int frozenqty;
	
	private int lockedqty;

	public String getSpecsTpId() {
		return specsTpId;
	}

	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
	}

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

	public int getFxqty() {
		return fxqty;
	}

	public void setFxqty(int fxqty) {
		this.fxqty = fxqty;
	}

	public int getFrozenqty() {
		return frozenqty;
	}

	public void setFrozenqty(int frozenqty) {
		this.frozenqty = frozenqty;
	}

	public int getLockedqty() {
		return lockedqty;
	}

	public void setLockedqty(int lockedqty) {
		this.lockedqty = lockedqty;
	}

	@Override
	public String toString() {
		return "WarehouseModel [id=" + id + ", itemId=" + itemId + ", fxqty=" + fxqty + ", frozenqty=" + frozenqty
				+ ", lockedqty=" + lockedqty + "]";
	}
	
}
