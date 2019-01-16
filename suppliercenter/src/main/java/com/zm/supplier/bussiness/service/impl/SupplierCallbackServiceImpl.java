package com.zm.supplier.bussiness.service.impl;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.supplier.bussiness.service.SupplierCallbackService;
import com.zm.supplier.common.ResultModel;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.feignclient.OrderFeignClient;
import com.zm.supplier.log.LogUtil;
import com.zm.supplier.pojo.ErrorCodeEnum;
import com.zm.supplier.pojo.SupplierInterface;
import com.zm.supplier.pojo.callback.OrderStatusCallBack;
import com.zm.supplier.util.JSONUtil;
import com.zm.supplier.util.SignUtil;

@Service
public class SupplierCallbackServiceImpl implements SupplierCallbackService {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	OrderFeignClient orderFeignClient;
	
	@Override
	public ResultModel orderStatusCallBack(OrderStatusCallBack statusCallBack) {
		statusCallBack.decodeExpressName();
		LogUtil.writeMessage(statusCallBack.toString());
		if (!statusCallBack.checkParam()) {// 判断参数全不全
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		if (!statusCallBack.checkOrderStatus()) {
			return new ResultModel(false, ErrorCodeEnum.ORDER_STATUS_ERROR.getErrorCode(),
					ErrorCodeEnum.ORDER_STATUS_ERROR.getErrorMsg());
		}
		// 获取对应的appKey和appSecret
		Set<String> keys = template.keys(Constants.SUPPLIER_INTERFACE + "*");
		if (keys == null) {
			return new ResultModel(false, ErrorCodeEnum.PARAM_ERROR.getErrorCode(),
					ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
		}
		SupplierInterface inf = null;
		for (String key : keys) {
			inf = (SupplierInterface) template.opsForValue().get(key);
			if (inf.getAppKey().equals(statusCallBack.getAppKey())) {
				break;
			}
		}
		if (inf == null) {
			return new ResultModel(false, ErrorCodeEnum.SIGN_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.SIGN_VALIDATE_ERROR.getErrorMsg());
		}
		// 判断签名对不对
		String sign = SignUtil.callBackSign(statusCallBack, inf.getAppSecret());
		if (!statusCallBack.getSign().equals(sign)) {
			return new ResultModel(false, ErrorCodeEnum.SIGN_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.SIGN_VALIDATE_ERROR.getErrorMsg());
		}
		// 更新订单状态
		ResultModel model = orderFeignClient.orderStatusCallBack(Constants.FIRST_VERSION, statusCallBack);
		return model;
	}
	
	public static void main(String[] args) {
		OrderStatusCallBack statusCallBack = new OrderStatusCallBack();
		statusCallBack.setAppKey("kabrita_gongxiaohaiwaigou");
		statusCallBack.setNonceStr("20190115163023");
		statusCallBack.setOrderId("GX0190115094403924015");
		statusCallBack.setType(1);
		statusCallBack.setStatus(6);
		statusCallBack.setExpressName("顺丰");
		statusCallBack.setExpressKey("SF");
		statusCallBack.setExpressId("245749463346");
		String appsecret = "A255BE77577E49CABC357C76D6AB9BC789YYE5WWK7PP";
		System.out.println("sign before:"+JSONUtil.toJson(statusCallBack));
		String sign = SignUtil.callBackSign(statusCallBack, appsecret);
		System.out.println("sign:"+sign);
	}
}
