/**  
 * Project Name:goodscenter  
 * File Name:GoodsStockEntity.java  
 * Package Name:com.zm.goods.pojo  
 * Date:Jan 1, 20182:45:53 AM  
 *  
 */
package com.zm.goods.pojo;

/**
 * ClassName: GoodsTagBindEntity <br/>
 * Function: 商品标签绑定信息. <br/>
 * date: Jan 1, 2018 2:45:53 AM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class GoodsTagBindEntity {

	private int id;
	private String specsTpId;// 商品ID
	private int tagId;//标签ID
	private String createTime;// 注册时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSpecsTpId() {
		return specsTpId;
	}
	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
	}
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
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
}
