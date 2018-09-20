package com.zm.order.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.order.pojo.ExpressFee;
import com.zm.order.pojo.bo.ExpressRule;
import com.zm.order.pojo.bo.ExpressRuleBind;
import com.zm.order.pojo.bo.ExpressTemplateBO;
import com.zm.order.pojo.po.ExpressRulePO;
import com.zm.order.pojo.po.RuleParameterPO;

public interface ExpressMapper {

	Page<ExpressTemplateBO> selectExpressForPage(ExpressTemplateBO template);

	void updateDisableBySupplierId(Integer id);

	void updateEnable(Integer id);
	
	void saveExpressTemplate(ExpressTemplateBO template);
	
	void saveExpressBatch(List<ExpressFee> list);

	ExpressTemplateBO getExpressTemplateById(Integer id);

	void update(ExpressTemplateBO expressTemplate);

	void updateExpressBatch(List<ExpressFee> expressList);

	void delExpressFee(Integer id);

	void saveRuleBindBatch(List<ExpressRuleBind> ruleList);

	void updateRuleBindBatch(List<ExpressRuleBind> updateList);

	void delRuleBind(Integer id);

	List<ExpressRulePO> listRule();

	List<RuleParameterPO> listRuleParam(Integer id);

	void addRuleParam(RuleParameterPO po);

	List<ExpressRule> listExpressRule(List<Integer> paramIdList);
}
