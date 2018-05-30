package com.zm.order.pojo.bo;

import java.util.List;

public class GradeBO implements Comparable<GradeBO>{

	private Integer id;
	private Integer parentId;
	private Integer gradeType;
	private List<GradeBO> children;
	public List<GradeBO> getChildren() {
		return children;
	}
	public void setChildren(List<GradeBO> children) {
		this.children = children;
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
	@Override
	public int compareTo(GradeBO o) {
		if(id > o.getId()){
			return 1;
		}
		return 0;
	}
	@Override
	public String toString() {
		return "GradeBO [id=" + id + ", parentId=" + parentId + ", gradeType=" + gradeType + "]";
	}
	
}
