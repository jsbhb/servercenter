package com.zm.user.bussiness.service;

import java.util.List;

import com.zm.user.pojo.bo.GradeBO;
import com.zm.user.pojo.bo.UserBO;

public interface UserFeignService {

	List<GradeBO> listGradeBO();

	List<Integer> listChildrenGrade(Integer gradeId);

	boolean initButtjoint();

	List<UserBO> listUserByUserId(List<Integer> userIdList);

	boolean verifyUserId(Integer userId);

	int getGradeTypeIdByGradeId(Integer gradeId);
}
