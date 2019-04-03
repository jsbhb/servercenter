package com.zm.supplier.supplierinf.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.supplier.common.ResultModel;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.feignclient.ThirdFeignClient;
import com.zm.supplier.log.LogUtil;
import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.DateUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.SignUtil;
import com.zm.supplier.util.XmlUtil;

@Component("kjb2cButtJoint")
public class KJB2CButtJoint extends AbstractSupplierButtJoint {

	@Resource
	ThirdFeignClient thirdFeignClient;

	@Resource
	RedisTemplate<String, Object> template;

	private final String order = "cnec_jh_order";
	private final String order_status = "cnec_jh_decl_byorder";

	@SuppressWarnings("unchecked")
	@Override
	public Set<SendOrderResult> sendOrder(List<OrderInfo> infoList) {
		String unionPayMerId = "";
		Object obj = template.opsForValue()
				.get(Constants.PAY + infoList.get(0).getCenterId() + Constants.UNION_PAY_MER_ID);
		if (obj != null) {
			unionPayMerId = obj.toString();
		}
		// 获取快递单号
		ResultModel result = thirdFeignClient.getExpressInfo(Constants.FIRST_VERSION, infoList, "YUNDA");
		if (!result.isSuccess()) {
			LogUtil.writeMessage("获取运单号出错:" + infoList.get(0).getOrderId());
			return null;
		}
		Map<String, Object> expressMap = (Map<String, Object>) result.getObj();
		String expressId = expressMap.get(infoList.get(0).getOrderId()) + "";
		String msg = ButtJointMessageUtils.getKJB2COrderMsg(infoList.get(0), unionPayMerId, expressId);// 报文
		Set<SendOrderResult> set = sendKJB2C(url, msg, SendOrderResult.class, infoList.get(0).getOrderId(), order);
		if (set != null) {
			for (SendOrderResult order : set) {
				order.setOrderId(infoList.get(0).getOrderId());
				order.setSupplierId(infoList.get(0).getSupplierId());
			}
		}
		return set;
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<OrderIdAndSupplierId> orderList) {
		String msg = ButtJointMessageUtils.getKJB2COrderStatus(orderList);
		Set<OrderStatus> set = sendKJB2C(url, msg, OrderStatus.class, orderList.get(0).getOrderId(), order_status);
		if (set != null) {
			Iterator<OrderStatus> it = set.iterator();
			while (it.hasNext()) {
				OrderStatus status = it.next();
				if (status.getOrderId() == null && status.getThirdOrderId() == null) {
					it.remove();
				}
			}
		}
		return set;
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OrderCancelResult> orderCancel(OrderInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	private <T> Set<T> sendKJB2C(String url, String msg, Class<T> clazz, String param, String msgType) {
		String date = DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
		String sign = SignUtil.getKjbSign(appKey, appSecret, date);// 签名
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("timestamp", date);
		paramMap.put("xmlstr", msg);
		paramMap.put("userid", appKey);
		paramMap.put("sign", sign);
		paramMap.put("customs", "3105");
		paramMap.put("msgtype", msgType);

		logger.info("发送报文：" + msg + ",签名：" + sign);
		String result = HttpClientUtil.post(url, paramMap, "", false);
		//保存推送订单回执
		if(order.equals(msgType)){
			saveResponse(param, result);
		}
		logger.info("返回：" + param + "====" + result);

		try {
			return renderResult(result, "XML", clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String s = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Message><Header><Result>T</Result><ResultMsg>成功</ResultMsg></Header><Body><Mft><MftNo>31052019I166246823</MftNo><ManifestId/><OrderNo>GX0190123160027388012</OrderNo><LogisticsNo>201901231617</LogisticsNo><PaySource/><LogisticsName>韵达速递</LogisticsName><CheckFlg>0</CheckFlg><CheckMsg>预校验不通过。1.没有对应支付单。</CheckMsg><Result/><Status>99</Status><Unusual/><MftInfos><MftInfo><Status>99</Status><Result>订单已取消</Result><CreateTime>2019-01-2514:27:43</CreateTime></MftInfo></MftInfos></Mft></Body></Message>";
		Set<OrderStatus> set = XmlUtil.parseXml(s, OrderStatus.class);
		System.out.println(set);
	}

}
