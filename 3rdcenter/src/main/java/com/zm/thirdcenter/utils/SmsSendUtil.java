package com.zm.thirdcenter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.NotifyMsg;
import com.zm.thirdcenter.pojo.ResultModel;

public class SmsSendUtil {

	private static Logger logger = LoggerFactory.getLogger(SmsSendUtil.class);

	/**
	 * sendMessage:阿里云短信发送. <br/>
	 * 
	 * @author wqy
	 * @param code
	 * @param phone
	 * @return
	 * @since JDK 1.7
	 */
	public static ResultModel sendMessage(NotifyMsg notifyMsg) {

		ResultModel result = new ResultModel();

		// 初始化ascClient需要的几个参数
		final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
		// 替换成你的AK
		final String accessKeyId = Constants.ACCESS_KEY_ID;// 你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = Constants.ACCESS_KEY_SECRET;// 你的accessKeySecret，参考本文档步骤2
		// 初始化ascClient,暂时不支持多region
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e1) {
			e1.printStackTrace();
			result.setErrorMsg("发送业务出错");
			result.setSuccess(false);
			return result;
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);
		SendSmsRequest request = packageRequest(notifyMsg);
		// 请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = null;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			result.setSuccess(true);
			return result;
		}
		result.setSuccess(false);
		result.setErrorMsg(sendSmsResponse.getMessage());
		return result;
	}

	private static SendSmsRequest packageRequest(NotifyMsg notifyMsg) {
		switch (notifyMsg.getType()) {
		case CODE:
			return packageCodeRequest(notifyMsg);
		case AUDIT:
			return packageAuditRequest(notifyMsg);
		case REPAYING:
			return packageRepayingRequest(notifyMsg);
		case INVITATION_CODE:
			return packageInvitationCodeRequest(notifyMsg);
		default:
			logger.error("短信模板类型出错====================");
			return null;
		}

	}

	private static SendSmsRequest packageInvitationCodeRequest(NotifyMsg notifyMsg) {
		SendSmsRequest request = new SendSmsRequest();
		request.setMethod(MethodType.POST);
		request.setPhoneNumbers(notifyMsg.getPhone());
		request.setSignName(Constants.SMS_SIGN);
		request.setTemplateCode(Constants.INVITATION_CODE);
		String json = "{\"name\":\"" + notifyMsg.getName() + "\",\"store\":\"" + notifyMsg.getShopName()
				+ "\",\"code\":\"" + notifyMsg.getMsg() + "\"}";
		logger.info("json===========" + json);
		request.setTemplateParam(json);
		return request;
	}

	private static SendSmsRequest packageRepayingRequest(NotifyMsg notifyMsg) {
		SendSmsRequest request = new SendSmsRequest();
		request.setMethod(MethodType.POST);
		request.setPhoneNumbers(notifyMsg.getPhone());
		request.setSignName(Constants.SMS_SIGN);
		request.setTemplateCode(Constants.PUSH_USER_REPAYING);
		String json = "{\"name\":\"" + notifyMsg.getName() + "\",\"store\":\"(" + notifyMsg.getShopName() + ")\"}";
		logger.info("json===========" + json);
		request.setTemplateParam(json);
		return request;
	}

	private static SendSmsRequest packageAuditRequest(NotifyMsg notifyMsg) {
		SendSmsRequest request = new SendSmsRequest();
		request.setMethod(MethodType.POST);
		request.setPhoneNumbers(notifyMsg.getPhone());
		request.setSignName(Constants.SMS_SIGN);
		request.setTemplateCode(Constants.PUSH_USER_AUDIT);
		String json = "{\"name\":\"" + notifyMsg.getName() + "\",\"store\":\"(" + notifyMsg.getShopName()
				+ ")\",\"time\":\"" + notifyMsg.getTime() + "\",\"message\":\"" + notifyMsg.getMsg() + "\"}";
		logger.info("json===========" + json);
		request.setTemplateParam(json);
		return request;
	}

	private static SendSmsRequest packageCodeRequest(NotifyMsg notifyMsg) {
		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();
		// 使用post提交
		request.setMethod(MethodType.POST);
		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		request.setPhoneNumbers(notifyMsg.getPhone());
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(Constants.SMS_SIGN);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(Constants.SMS_VERIFY_ID);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{\"code\":\"" + notifyMsg.getMsg() + "\"}");
		// 可选-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("yourOutId");
		return request;
	}
}
