/**  
 * Project Name:goodscenter  
 * File Name:GoodsStockEntity.java  
 * Package Name:com.zm.goods.pojo  
 * Date:Jan 1, 20182:45:53 AM  
 *  
 */
package com.zm.goods.pojo;

/**
 * ClassName: GoodsTagEntity <br/>
 * Function: 商品标签信息. <br/>
 * date: Jan 1, 2018 2:45:53 AM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class GoodsTagEntity {

	private int id;
	private String tagName;// 标签名称
	private String description;// 标签描述
	private String createTime;// 注册时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
