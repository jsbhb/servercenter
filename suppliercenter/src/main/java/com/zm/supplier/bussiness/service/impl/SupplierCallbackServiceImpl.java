package com.zm.supplier.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.supplier.bussiness.component.CustomThreadPool;
import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.bussiness.service.SupplierCallbackService;
import com.zm.supplier.common.ResultModel;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.custominf.model.CustomConfig;
import com.zm.supplier.feignclient.OrderFeignClient;
import com.zm.supplier.log.LogUtil;
import com.zm.supplier.pojo.ErrorCodeEnum;
import com.zm.supplier.pojo.SupplierInterface;
import com.zm.supplier.pojo.ThirdOrderInfo;
import com.zm.supplier.pojo.bo.CustomOrderExpress;
import com.zm.supplier.pojo.callback.DolphinCallBack;
import com.zm.supplier.pojo.callback.OrderStatusCallBack;
import com.zm.supplier.pojo.callback.ReceiveLogisticsCompany;
import com.zm.supplier.util.AESUtil;
import com.zm.supplier.util.EncryptUtil;
import com.zm.supplier.util.JSONUtil;
import com.zm.supplier.util.RSAUtil;
import com.zm.supplier.util.SignUtil;
import com.zm.supplier.util.XmlUtil;

@Service
public class SupplierCallbackServiceImpl implements SupplierCallbackService {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	SupplierMapper supplierMapper;

	@Resource
	CustomThreadPool customThreadPool;

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

	@Override
	public String dolphinCallBack(Map<String, String> getParam, Map<String, String> postParam) {
		DolphinCallBack callback = null;
		SupplierInterface inf = supplierMapper.getSupplierInterface(50);
		String md5 = getParam.get("md5");
		String data = postParam.get("data");
		String debug = getParam.get("debug");
		String type = getParam.get("type");
		String sign = SignUtil.getDolphinSign(getParam, inf.getAppSecret(), data);
		if (!sign.equals(md5)) {// 验签不通过返回
			callback = new DolphinCallBack("1004", "validation failed");
			return JSONUtil.toJson(callback);
		}
		if (!"on".equals(debug)) {
			// 解密
			data = EncryptUtil.decry_RC4(Base64.decodeBase64(data.getBytes()), inf.getAppSecret());
		}
		switch (type) {
		case "receiveLogisticsCompany":// 接收发货的物流公司信息接口
			return receiveLogisticsCompany(data);
		default:
			callback = new DolphinCallBack("20000", "");
			return JSONUtil.toJson(callback);
		}
	}

	// 接收发货的物流公司信息接口
	private String receiveLogisticsCompany(String data) {
		ReceiveLogisticsCompany receiveLogisticsCompany = JSONUtil.parse(data, ReceiveLogisticsCompany.class);
		CustomOrderExpress orderExpress = new CustomOrderExpress();
		orderExpress.setJsonStr(data);
		orderExpress.setOrderId(receiveLogisticsCompany.getOrderNo());
		orderFeignClient.saveCustomOrderExpress(Constants.FIRST_VERSION, orderExpress);
		// 订单申报
		customThreadPool.customOrder(receiveLogisticsCompany);
		DolphinCallBack callback = new DolphinCallBack("20000", "");
		return JSONUtil.toJson(callback);
	}

	@Override
	public String hzCustomsCallback(String content, String msgType, String dataDigest) {
		CustomConfig config = supplierMapper.getCustomConfig(1);
		try {
			byte[] inputContent = Base64.decodeBase64(content.getBytes("utf-8"));
			byte[] aesKey = Base64.decodeBase64(config.getCustomAesKey().getBytes("utf-8"));
			String originalContent = new String(AESUtil.decrypt(inputContent, aesKey), "utf-8");
			LogUtil.writeLog(
					"解密后content=" + originalContent + "******msgType=" + msgType + "*****dataDigest=" + dataDigest);
			byte[] inputData = originalContent.getBytes("utf-8");
			byte[] publicKey = Base64.decodeBase64(config.getCustomPublicKey());
			byte[] sign = Base64.decodeBase64(dataDigest);
			Boolean isOk = RSAUtil.verify(inputData, publicKey, sign);
			if (!isOk) {
				return buildHzCustomsResult(false, "S02", "非法的数字签名");
			}

			if ("CUSTOMS_DECLARE_RESULT_CALLBACK".equalsIgnoreCase(msgType)) {
				handleHzCustomCallback(originalContent);
			}
			if ("CUSTOMS_CEB_CALLBACK".equalsIgnoreCase(msgType)) {
				handleZongShuCallback(originalContent);
			}

		} catch (Exception e) {
			LogUtil.writeErrorLog("处理杭州海关回执失败", e);
			return buildHzCustomsResult(false, "S07", "系统异常，请重试");
		}
		return buildHzCustomsResult(true, null, null);
	}

	/**
	 * @fun 处理总署异步回执
	 * @param resultMap
	 * @throws DocumentException
	 */
	private void handleZongShuCallback(String xml) throws DocumentException {
		Map<String, String> resultMap = new HashMap<String, String>();
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		if (root.getName().contains("CEB312Message")) {// 电子订单总署回执
			XmlUtil.parseNode(resultMap, root);
			String returnStatus = resultMap.get("returnStatus");
			if (!zongshuVerify(returnStatus)) {
				String orderId = resultMap.get("orderNo");
				ThirdOrderInfo thirdOrder = new ThirdOrderInfo();
				thirdOrder.setOrderId(orderId);
				thirdOrder.setThirdOrderId(orderId);
				thirdOrder.setStatus(resultMap.get("returnInfo"));
				thirdOrder.setOrderStatus(99);
				List<ThirdOrderInfo> list = new ArrayList<>();
				list.add(thirdOrder);
				orderFeignClient.changeOrderStatusByThirdWarehouse(Constants.FIRST_VERSION, list);
			}
		}
		// 保存回执
		CustomOrderExpress tmp = new CustomOrderExpress();
		tmp.setJsonStr(xml);
		tmp.setOrderId(resultMap.get("orderNo") == null ? "error" : resultMap.get("orderNo"));
		supplierMapper.saveCustomOrderReturn(tmp);
	}

	/**
	 * @fun 判断总署申报是否成功(包括重复推送的信息)
	 * @param returnStatus
	 * @return
	 */
	private boolean zongshuVerify(String returnStatus) {

		return "2".equals(returnStatus) || "3".equals(returnStatus) || "120".equals(returnStatus)
				|| "-301010".equals(returnStatus) || "-301014".equals(returnStatus);
	}

	/**
	 * @fun 处理杭州海关异步回执
	 * @param resultMap
	 * @throws DocumentException
	 */
	private void handleHzCustomCallback(String xml) throws DocumentException {
		Map<String, String> resultMap = XmlUtil.xmlToMap(xml);
		String chkMark = resultMap.get("chkMark");
		if (!"1".equals(chkMark)) {
			if ("IMPORTORDER".equalsIgnoreCase(resultMap.get("businessType"))) {
				ThirdOrderInfo thirdOrder = new ThirdOrderInfo();
				thirdOrder.setOrderId(resultMap.get("businessNo"));
				thirdOrder.setThirdOrderId(resultMap.get("businessNo"));
				thirdOrder.setStatus(resultMap.get("resultInfo"));
				thirdOrder.setOrderStatus(99);
				List<ThirdOrderInfo> list = new ArrayList<>();
				list.add(thirdOrder);
				orderFeignClient.changeOrderStatusByThirdWarehouse(Constants.FIRST_VERSION, list);
			}
		}
		// 保存回执
		CustomOrderExpress tmp = new CustomOrderExpress();
		tmp.setJsonStr(xml);
		tmp.setOrderId(resultMap.get("businessNo") == null ? "error" : resultMap.get("businessNo"));
		supplierMapper.saveCustomOrderReturn(tmp);
	}

	private String buildHzCustomsResult(boolean b, String errorCode, String errorMsg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<response>");
		sb.append("<success>" + b + "</success>");
		if (!b) {
			sb.append("<errorCode>" + errorCode + "</errorCode>");
			sb.append("<errorMsg>" + errorMsg + "</errorMsg>");
		}
		sb.append("</response>");
		return sb.toString();
	}
}
