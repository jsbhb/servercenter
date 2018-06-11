package com.zm.order.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.order.pojo.ExpressFee;
import com.zm.order.pojo.bo.ExpressTemplateBO;

public interface ExpressMapper {

	Page<ExpressTemplateBO> selectExpressForPage(ExpressTemplateBO template);

	void updateDisableBySupplierId(Integer id);

	void updateEnable(Integer id);
	
	void saveExpressTemplate(ExpressTemplateBO template);
	
	void saveExpressBatch(List<ExpressFee> list);

	ExpressTemplateBO getExpressTemplateById(Integer id);

	void update(ExpressTemplateBO expressTemplate);

	void updateExpressBatch(List<ExpressFee> expressList);
}
