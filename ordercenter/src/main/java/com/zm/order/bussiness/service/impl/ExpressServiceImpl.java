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
import com.zm.order.bussiness.dao.ExpressMapper;
import com.zm.order.bussiness.service.ExpressService;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.ExpressFee;
import com.zm.order.pojo.bo.ExpressTemplateBO;

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
		if (expressTemplate.getExpressList() != null && expressTemplate.getExpressList().size() > 0) {
			List<ExpressFee> updateList = new ArrayList<ExpressFee>();
			List<ExpressFee> saveList = new ArrayList<ExpressFee>();
			for(ExpressFee express : expressTemplate.getExpressList()){
				if(express.getId() != null){
					updateList.add(express);
				} else {
					saveList.add(express);
				}
			}
			if(updateList.size() > 0){
				expressMapper.updateExpressBatch(updateList);
			}
			if(saveList.size() > 0){
				expressMapper.saveExpressBatch(saveList);
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
		template.opsForHash().putAll(Constants.POST_TAX + expressTemplate.getSupplierId(), tempMap);
	}

	@Override
	public ExpressTemplateBO getExpressTemplate(Integer id) {
		return expressMapper.getExpressTemplateById(id);
	}

	@Override
	public void delExpressFee(Integer id) {
		expressMapper.delExpressFee(id);
		
	}

}
