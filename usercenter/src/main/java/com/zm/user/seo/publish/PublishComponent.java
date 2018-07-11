package com.zm.user.seo.publish;

import com.zm.user.common.ResultModel;
import com.zm.user.enummodel.PublishType;
import com.zm.user.log.LogUtil;
import com.zm.user.pojo.bo.CreateAreaCenterSEO;
import com.zm.user.utils.HttpClientUtil;
import com.zm.user.utils.JSONUtil;

public class PublishComponent {
	
	public static ResultModel publish(String jsonStr,PublishType type){
		String result = HttpClientUtil.post(type.getUrl(), jsonStr, type.getMethod());
		LogUtil.writeLog(result);
		return JSONUtil.parse(result, ResultModel.class);
	}
	
	public static void main(String[] args) {
		CreateAreaCenterSEO createAreaCenterSEO = new CreateAreaCenterSEO(2, "test.cncoopbuy.com",
				"mtest.cncoopbuy.com");
		ResultModel result = PublishComponent.publish(JSONUtil.toJson(createAreaCenterSEO),
				PublishType.REGION_CREATE);
		System.out.println(result);
	}
}
