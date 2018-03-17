package com.zm.finance.pojo.capitalpool;

/**
 * @fun 资金池实体类(PO持久层)
 * @author user
 *
 */
public class CapitalPool {

	private Integer id;
	private Integer centerId;//区域中心ID
	private Double money;//可用金额
	private Double frozenMoney;//冻结金额
	private Double preferential;//优惠金额
	private Double frozenPreferential;//冻结优惠
	private Double useMoney;//使用金额
	private Double usePreferential;//使用优惠
	private Double countMoney;//累计金额
	private Double countPreferential;//累计优惠
	private Integer level;//星级
	private Integer status;//状态0停用，1启用
	private String remark;
	private String createTime;
	private String updateTime;
	private String opt;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCenterId() {
		return centerId;
	}
	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Double getFrozenMoney() {
		return frozenMoney;
	}
	public void setFrozenMoney(Double frozenMoney) {
		this.frozenMoney = frozenMoney;
	}
	public Double getPreferential() {
		return preferential;
	}
	public void setPreferential(Double preferential) {
		this.preferential = preferential;
	}
	public Double getFrozenPreferential() {
		return frozenPreferential;
	}
	public void setFrozenPreferential(Double frozenPreferential) {
		this.frozenPreferential = frozenPreferential;
	}
	public Double getUseMoney() {
		return useMoney;
	}
	public void setUseMoney(Double useMoney) {
		this.useMoney = useMoney;
	}
	public Double getUsePreferential() {
		return usePreferential;
	}
	public void setUsePreferential(Double usePreferential) {
		this.usePreferential = usePreferential;
	}
	public Double getCountMoney() {
		return countMoney;
	}
	public void setCountMoney(Double countMoney) {
		this.countMoney = countMoney;
	}
	public Double getCountPreferential() {
		return countPreferential;
	}
	public void setCountPreferential(Double countPreferential) {
		this.countPreferential = countPreferential;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	@Override
	public String toString() {
		return "CapitalPool [id=" + id + ", centerId=" + centerId + ", money=" + money + ", frozenMoney=" + frozenMoney
				+ ", preferential=" + preferential + ", frozenPreferential=" + frozenPreferential + ", useMoney="
				+ useMoney + ", usePreferential=" + usePreferential + ", countMoney=" + countMoney
				+ ", countPreferential=" + countPreferential + ", level=" + level + ", status=" + status + ", remark="
				+ remark + ", createTime=" + createTime + ", updateTime=" + updateTime + ", opt=" + opt + "]";
	}
	
}
