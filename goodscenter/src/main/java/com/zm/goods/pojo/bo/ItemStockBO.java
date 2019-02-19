package com.zm.goods.pojo.bo;

/**
 * @fun 库存业务实体类
 * @author user
 *
 */
public class ItemStockBO {

	private String itemId;
	private Integer stock;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
}
