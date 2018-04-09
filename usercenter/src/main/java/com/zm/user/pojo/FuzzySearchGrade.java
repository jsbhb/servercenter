package com.zm.user.pojo;

/**
 * 模糊搜索接收前端参数类
 * @author user
 *
 */
public class FuzzySearchGrade {

	private Integer id;
	private String name;
	private Integer level;
	private Integer centerId;
	
	public Integer getCenterId() {
		return centerId;
	}
	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
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
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
}
