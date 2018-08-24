/**  
 * Project Name:goodscenter  
 * File Name:GoodsStockEntity.java  
 * Package Name:com.zm.goods.pojo  
 * Date:Jan 1, 20182:45:53 AM  
 *  
 */
package com.zm.goods.pojo;

import com.zm.goods.common.Pagination;

/**
 * ClassName: GoodsTagEntity <br/>
 * Function: 商品标签信息. <br/>
 * date: Jan 1, 2018 2:45:53 AM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class ERPGoodsTagEntity extends Pagination {

	private Integer id;
	private String tagName;// 标签名称
	private Integer tagFunId;//标签功能ID
	private Integer priority;//优先级
	private String description;// 标签描述
	private String createTime;// 注册时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	private Integer tagRatio;//标签权重
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
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
	public Integer getTagFunId() {
		return tagFunId;
	}
	public void setTagFunId(Integer tagFunId) {
		this.tagFunId = tagFunId;
	}
	public Integer getTagRatio() {
		return tagRatio;
	}
	public void setTagRatio(Integer tagRatio) {
		this.tagRatio = tagRatio;
	}
	@Override
	public String toString() {
		return "ERPGoodsTagEntity [id=" + id + ", tagName=" + tagName + ", tagRatio=" + tagRatio + "]";
	}
	
}
