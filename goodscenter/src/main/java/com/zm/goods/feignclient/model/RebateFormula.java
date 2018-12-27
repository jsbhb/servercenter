package com.zm.goods.feignclient.model;

/**
 * @fun 数据库对象
 * @author user
 *
 */
public class RebateFormula{

	private Integer id;
	private Integer gradeTypeId;
	private String gradeTypeName;
	private String formula;
	private String createTime;
	private String updateTime;
	private String backup;
	private String remark;
	private String opt;
	private Integer status;
	public String getGradeTypeName() {
		return gradeTypeName;
	}
	public void setGradeTypeName(String gradeTypeName) {
		this.gradeTypeName = gradeTypeName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGradeTypeId() {
		return gradeTypeId;
	}
	public void setGradeTypeId(Integer gradeTypeId) {
		this.gradeTypeId = gradeTypeId;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
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
	public String getBackup() {
		return backup;
	}
	public void setBackup(String backup) {
		this.backup = backup;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
