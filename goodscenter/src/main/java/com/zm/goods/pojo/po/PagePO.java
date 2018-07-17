package com.zm.goods.pojo.po;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.zm.goods.seo.model.SEOModel;

@JsonInclude(Include.NON_NULL)
public class PagePO {

	private Integer id;
	private String name;
	private Integer type;
	private String path;
	private Integer status;
	private String file;
	private String client;
	private Integer gradeId;
	private String page;
	private String description;
	private List<ComponentPagePO> module;
	private SEOModel sEOModel;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Integer getGradeId() {
		return gradeId;
	}
	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ComponentPagePO> getModule() {
		return module;
	}
	public void setModule(List<ComponentPagePO> module) {
		this.module = module;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public SEOModel getsEOModel() {
		return sEOModel;
	}
	public void setsEOModel(SEOModel sEOModel) {
		this.sEOModel = sEOModel;
	}
	
	
	
}
