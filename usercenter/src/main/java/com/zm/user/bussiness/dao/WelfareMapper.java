package com.zm.user.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.zm.user.pojo.InviterEntity;
import com.zm.user.pojo.bo.WelfareMembeStatistic;

public interface WelfareMapper {

	void insertInviterInfo(List<InviterEntity> importList);
	
	Page<InviterEntity> selectForPage(InviterEntity entity);

	void updateInviterInfo(List<InviterEntity> list);
	
	List<InviterEntity> selectInviterListByParam(Map<String,Object> param);

	void updateInviterStatus(List<InviterEntity> list);

	List<WelfareMembeStatistic> inviterStatistic(Map<String,Object> param);

	InviterEntity getInviterByParam(Map<String, Object> param);
}
