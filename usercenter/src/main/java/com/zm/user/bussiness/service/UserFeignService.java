package com.zm.user.bussiness.service;

import java.util.List;

import com.zm.user.pojo.bo.GradeBO;

public interface UserFeignService {

	List<GradeBO> listGradeBO();

	List<Integer> listChildrenGrade(Integer gradeId);

	boolean initButtjoint();
}
