package com.zm.thirdcenter.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.thirdcenter.pojo.PageViewBO;

public interface PageViewMapper {

	void savePageView(List<PageViewBO> list);
	
	void saveUniqueVisitor(Map<String,String> param);
}
