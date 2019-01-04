package com.zm.user.pojo.bo;

import java.util.List;


public class GradeBO {

	private Integer id;
	private Integer parentId;
	private Integer gradeType;
	private String name;
	private String company;
	private String gradeTypeName;
	private Integer type;
	private Integer welfareType;
	private Double welfareRebate;
	private List<GradeBO> children;
	
	public Integer getWelfareType() {
		return welfareType;
	}
	public void setWelfareType(Integer welfareType) {
		this.welfareType = welfareType;
	}
	public Double getWelfareRebate() {
		return welfareRebate;
	}
	public void setWelfareRebate(Double welfareRebate) {
		this.welfareRebate = welfareRebate;
	}
	public String getGradeTypeName() {
		return gradeTypeName;
	}
	public void setGradeTypeName(String gradeTypeName) {
		this.gradeTypeName = gradeTypeName;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getGradeType() {
		return gradeType;
	}
	public void setGradeType(Integer gradeType) {
		this.gradeType = gradeType;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<GradeBO> getChildren() {
		return children;
	}
	public void setChildren(List<GradeBO> children) {
		this.children = children;
	}
	
}
