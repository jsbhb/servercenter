package com.zm.thirdcenter.expressinf.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

import com.zm.thirdcenter.bussiness.express.model.YunDaResultModel;
import com.zm.thirdcenter.expressinf.AbstractExpressButtJoint;
import com.zm.thirdcenter.pojo.ExpressInfoResult;
import com.zm.thirdcenter.pojo.OrderInfo;
import com.zm.thirdcenter.utils.ButtJointMessageUtils;
import com.zm.thirdcenter.utils.HttpClientUtil;
import com.zm.thirdcenter.utils.LogUtil;
import com.zm.thirdcenter.utils.SignUtil;

@Component
public class YunDaButtjoint extends AbstractExpressButtJoint {

	@Override
	public Set<ExpressInfoResult> createExpressInfo(List<OrderInfo> infoList) {
		Set<ExpressInfoResult> resultSet = null;
		for (OrderInfo oi : infoList) {
			String strXmlInfo = ButtJointMessageUtils.getYunDaCreateOrderMsg(oi);
			String strPostParams = getPostParamStr(packageParams(strXmlInfo, "global_order_create"));
			Map<String, String> postParam = new HashMap<String, String>();
			try {
				String result = HttpClientUtil.post(url + "?" + strPostParams, postParam);
				LogUtil.writeMessage("返回======" + result);
				Set<YunDaResultModel> tmpSet = renderResult(result, "XML", YunDaResultModel.class);
				if (tmpSet != null && tmpSet.size() > 0) {
					resultSet = new HashSet<ExpressInfoResult>();
					for (YunDaResultModel eir : tmpSet) {
						ExpressInfoResult SuccessResult = new ExpressInfoResult();
						SuccessResult.setOrderId(eir.getHawbno());
						SuccessResult.setExpressNo(eir.getMail_no());
						resultSet.add(SuccessResult);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultSet;
	}

	@Override
	public Set<ExpressInfoResult> updateExpressInfo(List<OrderInfo> infoList) {
		return null;
	}

	@Override
	public Set<ExpressInfoResult> deleteExpressInfo(List<OrderInfo> infoList) {
		return null;
	}

	@Override
	public Set<ExpressInfoResult> retrieveExpressInfo(List<OrderInfo> infoList) {
		return null;
	}

	private Map<String, Object> packageParams(String data, String method) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("app_key", appKey);
		params.put("buz_type", buzType);
		params.put("data", data);
		params.put("format", format);
		params.put("method", method);
		params.put("tradeId", tradeId);
		params.put("version", version);

		return params;
	}

	private String getPostParamStr(Map<String, Object> params) {
		String postParamsStr = "";
		try {
			String toSignStr = SignUtil.sortAndConvertString(params, false, false);
			toSignStr += appSecret;
			String signStr = SignUtil.strToBASE64Sign(SignUtil.strToMD5Sign(toSignStr, false));
			postParamsStr = SignUtil.sortAndConvertString(params, true, true);
			postParamsStr += "sign=" + URLEncoder.encode(signStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return postParamsStr;
	}
}
