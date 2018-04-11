package com.zm.order.bussiness.service;

import com.zm.order.pojo.bo.GradeBO;

public interface OrderFeignService {

	void syncGrade(GradeBO grade);
}
