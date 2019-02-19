package com.zm.goods.pojo.dto;
/**
 * @fun 生成分享图片数据传输实体类
 * @author user
 *
 */
public class GoodsBillboardDTO {

	private String goodsId;
	private Integer id;
	private Integer shopId;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
}
