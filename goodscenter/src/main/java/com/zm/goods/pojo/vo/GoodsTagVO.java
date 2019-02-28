package com.zm.goods.pojo.vo;
/**
 * @fun 前端标签展示
 * @author user
 *
 */
public class GoodsTagVO {

	private String specsTpId;
	private String tagName;
	private int tagFun;
	public int getTagFun() {
		return tagFun;
	}
	public void setTagFun(int tagFun) {
		this.tagFun = tagFun;
	}
	public String getSpecsTpId() {
		return specsTpId;
	}
	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
}
