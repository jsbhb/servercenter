package com.zm.order.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.order.pojo.bo.ExpressTemplateBO;

public interface ExpressService {

	Page<ExpressTemplateBO> queryForPage(ExpressTemplateBO template);

	void enable(Integer id);

	void saveExpressTemplate(ExpressTemplateBO template);

	void updateExpressTemplate(ExpressTemplateBO template);

	ExpressTemplateBO getExpressTemplate(Integer id);

	void delExpressFee(Integer id);
}
