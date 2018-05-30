package com.zm.user.pojo.dto;

import java.util.List;
/**
 * @fun 数据传输对象
 * @author user
 *
 */
public class GradeTypeDTO {

	private Integer id;
	private Integer parentId;
	private String name;
	private List<GradeTypeDTO> childern;
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<GradeTypeDTO> getChildern() {
		return childern;
	}
	public void setChildern(List<GradeTypeDTO> childern) {
		this.childern = childern;
	}
}
