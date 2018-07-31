package com.zm.user.utils;

import com.zm.user.pojo.Grade;
import com.zm.user.pojo.bo.ButtjointUserBO;

public class ConvertUtil {

	public static ButtjointUserBO converToButtjoinUserBO(Grade grade) {
		ButtjointUserBO bo = new ButtjointUserBO();
		bo.setAppKey(grade.getAppKey());
		bo.setAppSecret(grade.getAppSecret());
		bo.setUrl(grade.getRedirectUrl());
		return bo;
	}

}
