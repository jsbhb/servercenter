package com.zm.goods.seo.publish;

import com.zm.goods.enummodel.PublishType;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.utils.HttpClientUtil;
import com.zm.goods.utils.JSONUtil;

public class PublishComponent {

	public static ResultModel publish(String jsonStr, PublishType type) {
		LogUtil.writeLog(jsonStr);
		String result = HttpClientUtil.post(type.getUrl(), jsonStr, type.getMethod());
		LogUtil.writeLog(result);
		if(result == null){
			return new ResultModel(false, "", "写入失败");
		}
		return JSONUtil.parse(result, ResultModel.class);
	}
}
