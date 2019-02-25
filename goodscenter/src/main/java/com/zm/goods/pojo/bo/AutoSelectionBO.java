package com.zm.goods.pojo.bo;

/**
 * @fun 跨境商品单个上架/跨境批量上架自动选择itemId/订单自动选择itemId  业务类
 * @author user
 *
 */
public class AutoSelectionBO {

	private String specsTpId;
	
	private String itemId;

	public String getSpecsTpId() {
		return specsTpId;
	}

	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
}
