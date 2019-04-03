package com.zm.thirdcenter.bussiness.customs.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zm.thirdcenter.bussiness.customs.model.CustomRequest;
import com.zm.thirdcenter.bussiness.customs.model.CustomsRealTimeDataUp;
import com.zm.thirdcenter.bussiness.customs.model.GoodsInfo;
import com.zm.thirdcenter.bussiness.customs.model.PayExchangeInfo;
import com.zm.thirdcenter.bussiness.customs.model.PayExchangeInfoHead;
import com.zm.thirdcenter.bussiness.customs.model.PayOriginData;
import com.zm.thirdcenter.bussiness.customs.service.CustomsService;
import com.zm.thirdcenter.bussiness.dao.CustomsMapper;
import com.zm.thirdcenter.feignclient.OrderFeignClient;
import com.zm.thirdcenter.feignclient.PayClient;
import com.zm.thirdcenter.feignclient.SupplierFeignClient;
import com.zm.thirdcenter.utils.DateUtil;
import com.zm.thirdcenter.utils.HttpClientUtil;
import com.zm.thirdcenter.utils.JSONUtil;
import com.zm.thirdcenter.utils.LogUtil;
import com.zm.thirdcenter.utils.XmlUtil;

@Service
public class CustomsServiceImpl implements CustomsService {

	@Resource
	CustomsMapper customsMapper;

	@Resource
	PayClient payClient;

	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	SupplierFeignClient supplierFeignClient;

	@Value("${customDataUpUrl}")
	private String customDataUpUrl;

	@Override
	public String platDataOpen(CustomRequest customs) {
		// 异步处理并返回实时信息
		realTimeDataUpSync(customs);
		return "{\"code\":\"10000\",\"message\":\"\",\"serviceTime\":" + System.currentTimeMillis() + "}";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void realTimeDataUp(CustomRequest customs) {
		try {
			// 获取支付原始信息
			String dataStr = payClient.listPayOriginData(customs.getOrderNo());
			List<PayOriginData> originList = JSONUtil.parse(dataStr, new TypeReference<List<PayOriginData>>() {
			});
			int payType = originList.get(0).getPayType();
			String payCode = "";
			String recpAccount = "";
			if (payType == 1) {
				payCode = "4403169D3W";
				recpAccount = "379274465143";
			}
			if (payType == 2) {
				payCode = "ZF14021901";
				recpAccount = "379274465143";
			}
			Map<Integer, List<PayOriginData>> map = originList.stream()
					.collect(Collectors.groupingBy(PayOriginData::getType));
			// 获取订单商品信息
			String goodsStr = orderFeignClient.listOrderGoodsInfo(customs.getOrderNo());
			List<GoodsInfo> infoList = JSONUtil.parse(goodsStr, new TypeReference<List<GoodsInfo>>() {
			});
			// 获取订单详情
			String detail = orderFeignClient.getOrderDetail(customs.getOrderNo());
			Map<String, Object> detailMap = JSONUtil.parse(detail, Map.class);
			// 封装返回body
			PayExchangeInfo payExchangeInfo = new PayExchangeInfo();
			payExchangeInfo.setGoodsInfo(infoList);
			payExchangeInfo.setOrderNo(customs.getOrderNo());
			payExchangeInfo.setRecpAccount(recpAccount);
			payExchangeInfo.setRecpCode("");
			payExchangeInfo.setRecpName("宁波鑫海通达跨境电子商务有限公司");
			// end
			// 封装返回头部
			PayExchangeInfoHead payExchangeInfoHead = new PayExchangeInfoHead();
			payExchangeInfoHead.setCurrency("142");
			payExchangeInfoHead.setEbpCode("3302462946");
			payExchangeInfoHead.setGuid(UUID.randomUUID().toString().toUpperCase());
			payExchangeInfoHead.setInitalRequest(map.get(1).get(0).getContent());// 支付请求报文
			payExchangeInfoHead.setInitalResponse(map.get(2).get(0).getContent());// 支付返回报文
			payExchangeInfoHead.setPayCode(payCode);
			payExchangeInfoHead.setPayTransactionId(detailMap.get("pay_no") + "");
			payExchangeInfoHead.setPayType("3");
			payExchangeInfoHead.setTotalAmount(Double.valueOf(detailMap.get("payment") + ""));
			String tradingTime = map.get(2).get(0).getCreateTime();
			payExchangeInfoHead
					.setTradingTime(DateUtil.dataformat(tradingTime, "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
			payExchangeInfoHead.setVerDept("3");
			payExchangeInfoHead.setNote("");
			// end
			// 封装返回数据
			CustomsRealTimeDataUp dataUp = new CustomsRealTimeDataUp();
			dataUp.setPayExchangeInfoHead(payExchangeInfoHead);
			List<PayExchangeInfo> payExchangeInfoLists = new ArrayList<>();
			payExchangeInfoLists.add(payExchangeInfo);
			dataUp.setPayExchangeInfoLists(payExchangeInfoLists);
			dataUp.setServiceTime(System.currentTimeMillis() + "");
			dataUp.setSessionID(customs.getSessionID());
			// 封装签名数据
			StringBuilder sb = new StringBuilder();
			sb.append("\"sessionID\":\"" + dataUp.getSessionID() + "\"||");
			sb.append("\"payExchangeInfoHead\":\"" + JSONUtil.toJson(payExchangeInfoHead) + "\"||");
			sb.append("\"payExchangeInfoLists\":\"" + JSONUtil.toJson(payExchangeInfoLists) + "\"||");
			sb.append("\"serviceTime\":\"" + dataUp.getServiceTime() + "\"");
			// 调用签名
			String result = supplierFeignClient.getSign(sb.toString());
			Map<String, String> resultMap = XmlUtil.xmlToMap(result);
			if (!"T".equalsIgnoreCase(resultMap.get("Result"))) {
				LogUtil.writeErrorLog(resultMap.get("Remark"));
				customs.setStatus(0);
				customs.setRemark("获取签名出错");
				customsMapper.saveCustomRequest(customs);
				return;
			}
			dataUp.setCertNo(resultMap.get("CertNo"));
			String sign = new String(Base64.decodeBase64(resultMap.get("ResultMsg")));
			dataUp.setSignValue(sign);
			String data = "payExInfoStr=" + URLEncoder.encode(JSONUtil.toJson(dataUp), "utf-8");
			result = HttpClientUtil.post(customDataUpUrl, data, "application/x-www-form-urlencoded", true);
			resultMap = JSONUtil.parse(result, Map.class);
			if ("10000".equalsIgnoreCase(resultMap.get("code"))) {
				customs.setStatus(1);
			} else {
				customs.setStatus(2);
				customs.setRemark(resultMap.get("message"));
			}
			customsMapper.saveCustomRequest(customs);
		} catch (Exception e) {
			LogUtil.writeErrorLog("实时上传支付信息出错：", e);
			customs.setStatus(0);
			customs.setRemark("返回数据出错" + e.getMessage());
			customsMapper.saveCustomRequest(customs);
		}
	}

	/**
	 * @fun 异步返回数据
	 * @param customs
	 */
	@Async("myAsync")
	public void realTimeDataUpSync(CustomRequest customs) {
		realTimeDataUp(customs);
	}
}
