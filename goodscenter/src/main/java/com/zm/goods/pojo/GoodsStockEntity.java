/**  
 * Project Name:goodscenter  
 * File Name:GoodsStockEntity.java  
 * Package Name:com.zm.goods.pojo  
 * Date:Jan 1, 20182:45:53 AM  
 *  
 */
package com.zm.goods.pojo;

/**
 * ClassName: GoodsStockEntity <br/>
 * Function: 明细库存信息. <br/>
 * date: Jan 1, 2018 2:45:53 AM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class GoodsStockEntity {

	private int id;
	private String itemId;
	private int qpQty;// 良品数量
	private int fxQty;// 分销数量
	private int defQty;// 次品数量
	private int frozenQty;// 冻结数量
	private int lockedQty;// 锁定数量
	private String createTime;// 注册时间
	private String updateTime;// 更新时间
	private String opt;// 操作人

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getQpQty() {
		return qpQty;
	}

	public void setQpQty(int qpQty) {
		this.qpQty = qpQty;
	}

	public int getFxQty() {
		return fxQty;
	}

	public void setFxQty(int fxQty) {
		this.fxQty = fxQty;
	}

	public int getDefQty() {
		return defQty;
	}

	public void setDefQty(int defQty) {
		this.defQty = defQty;
	}

	public int getFrozenQty() {
		return frozenQty;
	}

	public void setFrozenQty(int frozenQty) {
		this.frozenQty = frozenQty;
	}

	public int getLockedQty() {
		return lockedQty;
	}

	public void setLockedQty(int lockedQty) {
		this.lockedQty = lockedQty;
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
