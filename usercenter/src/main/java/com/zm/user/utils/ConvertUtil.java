package com.zm.user.utils;

import com.zm.user.pojo.Grade;
import com.zm.user.pojo.bo.ButtjointUserBO;
import com.zm.user.pojo.bo.GradeBO;

public class ConvertUtil {

	public static ButtjointUserBO converToButtjoinUserBO(Grade grade) {
		ButtjointUserBO bo = new ButtjointUserBO();
		bo.setAppKey(grade.getAppKey());
		bo.setAppSecret(grade.getAppSecret());
		bo.setUrl(grade.getRedirectUrl() == null ? "" : grade.getRedirectUrl());
		return bo;
	}
	
	public static GradeBO converToGradeBO(Grade grade){
		GradeBO gradeBO = new GradeBO();
		gradeBO.setId(grade.getId());
		gradeBO.setGradeType(grade.getGradeType());
		gradeBO.setParentId(grade.getParentId());
		gradeBO.setName(grade.getGradeName());
		gradeBO.setCompany(grade.getCompany());
		gradeBO.setGradeTypeName(grade.getGradeTypeName());
		gradeBO.setType(grade.getType());
		gradeBO.setWelfareRebate(grade.getWelfareRebate());
		return gradeBO;
	}

}
