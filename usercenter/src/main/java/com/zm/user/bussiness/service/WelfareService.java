package com.zm.user.bussiness.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.user.common.ResultModel;
import com.zm.user.pojo.InviterEntity;

public interface WelfareService {

	ResultModel ImportInviterList(List<InviterEntity> importList);
	
	Page<InviterEntity> queryForPage(InviterEntity entity);
	
	ResultModel updateInviter(InviterEntity entity);
	
	ResultModel produceCode(InviterEntity entity);
	
	ResultModel sendProduceCode(InviterEntity entity);

	ResultModel inviterStatistic(Integer gradeId);

	ResultModel applyCode(Integer gradeId, String phone);
}
