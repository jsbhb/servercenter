package com.zm.order.pojo.bo;

import java.util.List;

import com.zm.order.common.Pagination;
import com.zm.order.pojo.ExpressFee;

public class ExpressTemplateBO extends Pagination{

	private Integer id;
	private Integer supplierId;
	private String templateName;
	private Integer freePost;
	private Integer freeTax;
	private Integer enable;
	private List<ExpressFee> expressList;
	public List<ExpressFee> getExpressList() {
		return expressList;
	}
	public void setExpressList(List<ExpressFee> expressList) {
		this.expressList = expressList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public Integer getFreePost() {
		return freePost;
	}
	public void setFreePost(Integer freePost) {
		this.freePost = freePost;
	}
	public Integer getFreeTax() {
		return freeTax;
	}
	public void setFreeTax(Integer freeTax) {
		this.freeTax = freeTax;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
}
