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
	private String opt;
	private List<ExpressFee> expressList;
	private List<ExpressRuleBind> ruleBindList;
	private List<ExpressRule> ruleList;
	private String ruleName;
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public List<ExpressRule> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<ExpressRule> ruleList) {
		this.ruleList = ruleList;
	}
	public List<ExpressRuleBind> getRuleBindList() {
		return ruleBindList;
	}
	public void setRuleBindList(List<ExpressRuleBind> ruleBindList) {
		this.ruleBindList = ruleBindList;
	}
	public List<ExpressFee> getExpressList() {
		return expressList;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
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
