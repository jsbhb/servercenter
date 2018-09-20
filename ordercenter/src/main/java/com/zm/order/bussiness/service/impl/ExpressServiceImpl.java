package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.order.bussiness.component.expressrule.ExpressRuleStrategy;
import com.zm.order.bussiness.dao.ExpressMapper;
import com.zm.order.bussiness.service.ExpressService;
import com.zm.order.constants.Constants;
import com.zm.order.exception.ParameterException;
import com.zm.order.pojo.ExpressFee;
import com.zm.order.pojo.bo.ExpressRule;
import com.zm.order.pojo.bo.ExpressRuleBind;
import com.zm.order.pojo.bo.ExpressTemplateBO;
import com.zm.order.pojo.po.ExpressRulePO;
import com.zm.order.pojo.po.RuleParameterPO;
import com.zm.order.utils.JSONUtil;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ExpressServiceImpl implements ExpressService {

	@Resource
	ExpressMapper expressMapper;

	@Resource
	RedisTemplate<String, String> template;

	private static final Integer ENABLE = 1;

	@Override
	public Page<ExpressTemplateBO> queryForPage(ExpressTemplateBO expressTemplate) {
		PageHelper.startPage(expressTemplate.getCurrentPage(), expressTemplate.getNumPerPage(), true);
		return expressMapper.selectExpressForPage(expressTemplate);
	}

	@Override
	public void enable(Integer id) {
		ExpressTemplateBO expressTemplate = expressMapper.getExpressTemplateById(id);
		expressMapper.updateDisableBySupplierId(expressTemplate.getSupplierId());
		expressMapper.updateEnable(id);
		setPostTaxRedis(expressTemplate);
	}

	@Override
	public void saveExpressTemplate(ExpressTemplateBO expressTemplate) {
		if (expressTemplate.getEnable() == ENABLE) {
			expressMapper.updateDisableBySupplierId(expressTemplate.getSupplierId());
		}
		expressMapper.saveExpressTemplate(expressTemplate);
		if (expressTemplate.getExpressList() != null && expressTemplate.getExpressList().size() > 0) {
			for (ExpressFee model : expressTemplate.getExpressList()) {
				model.setTemplateId(expressTemplate.getId());
			}
			expressMapper.saveExpressBatch(expressTemplate.getExpressList());
		}
		if (expressTemplate.getRuleBindList() != null && expressTemplate.getRuleBindList().size() > 0) {
			for (ExpressRuleBind model : expressTemplate.getRuleBindList()) {
				model.setTemplateId(expressTemplate.getId());
			}
			expressMapper.saveRuleBindBatch(expressTemplate.getRuleBindList());
		}
		if (expressTemplate.getEnable() == ENABLE) {
			setPostTaxRedis(expressTemplate);
		}
	}

	@Override
	public void updateExpressTemplate(ExpressTemplateBO expressTemplate) {
		if (expressTemplate.getEnable() == ENABLE) {
			expressMapper.updateDisableBySupplierId(expressTemplate.getSupplierId());
		}
		expressMapper.update(expressTemplate);
		// 更新运费
		if (expressTemplate.getExpressList() != null && expressTemplate.getExpressList().size() > 0) {
			List<ExpressFee> updateList = new ArrayList<ExpressFee>();
			List<ExpressFee> saveList = new ArrayList<ExpressFee>();
			for (ExpressFee express : expressTemplate.getExpressList()) {
				if (express.getId() != null) {
					updateList.add(express);
				} else {
					saveList.add(express);
				}
			}
			if (updateList.size() > 0) {
				expressMapper.updateExpressBatch(updateList);
			}
			if (saveList.size() > 0) {
				expressMapper.saveExpressBatch(saveList);
			}
		}
		// 更新规则
		if (expressTemplate.getRuleBindList() != null && expressTemplate.getRuleBindList().size() > 0) {
			List<ExpressRuleBind> updateList = new ArrayList<ExpressRuleBind>();
			List<ExpressRuleBind> saveList = new ArrayList<ExpressRuleBind>();
			for (ExpressRuleBind express : expressTemplate.getRuleBindList()) {
				if (express.getId() != null) {
					updateList.add(express);
				} else {
					saveList.add(express);
				}
			}
			if (updateList.size() > 0) {
				expressMapper.updateRuleBindBatch(updateList);
			}
			if (saveList.size() > 0) {
				expressMapper.saveRuleBindBatch(saveList);
			}
		}
		if (expressTemplate.getEnable() == ENABLE) {
			setPostTaxRedis(expressTemplate);
		}
	}

	private void setPostTaxRedis(ExpressTemplateBO expressTemplate) {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("post", expressTemplate.getFreePost() + "");
		tempMap.put("tax", expressTemplate.getFreeTax() + "");
		if (expressTemplate.getRuleBindList() != null && expressTemplate.getRuleBindList().size() > 0) {
			tempMap.put("rule", JSONUtil.toJson(expressTemplate.getRuleBindList()));
		}
		template.opsForHash().putAll(Constants.POST_TAX + expressTemplate.getSupplierId(), tempMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpressTemplateBO getExpressTemplate(Integer id) {
		ExpressTemplateBO bo = expressMapper.getExpressTemplateById(id);
		if (bo.getRuleBindList() != null && bo.getRuleBindList().size() > 0) {
			Map<String, String> paramKeyMap = null;
			Map<String, String> paramValueMap = null;
			StringBuilder sb = null;
			for (ExpressRuleBind ruleBind : bo.getRuleBindList()) {
				sb = new StringBuilder();
				paramKeyMap = JSONUtil.parse(ruleBind.getParamKey(), Map.class);
				paramValueMap = JSONUtil.parse(ruleBind.getParam(), Map.class);
				for (Map.Entry<String, String> entry : paramKeyMap.entrySet()) {
					for (Map.Entry<String, String> entryTmp : paramValueMap.entrySet()) {
						if (entry.getValue().equals(entryTmp.getKey())) {
							sb.append(entry.getKey() + ":" + entryTmp.getValue() + ";");
						}
					}
				}
				ruleBind.setParam(sb.substring(0, sb.length() - 1));
			}
		}
		return bo;
	}

	@Override
	public void delExpressFee(Integer id) {
		expressMapper.delExpressFee(id);

	}

	@Override
	public void delRuleBind(Integer id) {

		expressMapper.delRuleBind(id);
	}

	@Override
	public List<ExpressRulePO> listRule() {
		
		return expressMapper.listRule();
	}

	@Override
	public List<RuleParameterPO> listRuleParam(Integer id) {
		
		return expressMapper.listRuleParam(id);
	}

	@Override
	public void addRuleParam(RuleParameterPO po) throws ParameterException {
		List<ExpressRule> ruleList = new ArrayList<ExpressRule>();
		ExpressRule rule = new ExpressRule();
		rule.setJson(po.getParam());
		rule.setType(po.getRuleId());
		ruleList.add(rule);
		ExpressRuleStrategy strategy = new ExpressRuleStrategy(ruleList);
		strategy.checkExpressRuleParam();
		expressMapper.addRuleParam(po);
	}

}
