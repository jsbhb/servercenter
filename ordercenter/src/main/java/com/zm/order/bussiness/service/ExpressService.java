package com.zm.order.bussiness.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.order.exception.ParameterException;
import com.zm.order.pojo.bo.ExpressTemplateBO;
import com.zm.order.pojo.po.ExpressRulePO;
import com.zm.order.pojo.po.RuleParameterPO;

public interface ExpressService {

	Page<ExpressTemplateBO> queryForPage(ExpressTemplateBO template);

	void enable(Integer id);

	void saveExpressTemplate(ExpressTemplateBO template);

	void updateExpressTemplate(ExpressTemplateBO template);

	ExpressTemplateBO getExpressTemplate(Integer id);

	void delExpressFee(Integer id);

	void delRuleBind(Integer id);

	List<ExpressRulePO> listRule();

	List<RuleParameterPO> listRuleParam(Integer id);

	void addRuleParam(RuleParameterPO po) throws ParameterException;
}
