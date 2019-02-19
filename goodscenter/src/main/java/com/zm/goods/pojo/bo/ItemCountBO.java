package com.zm.goods.pojo.bo;

/**
 * @fun 商品是否全部上下架实体类
 * @author user
 *
 */
public class ItemCountBO {

	private String itemId;
	private int count;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
