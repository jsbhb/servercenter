package com.zm.supplier.supplierinf.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderDetail;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.UserDetail;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.supplierinf.AccessTokenCacheMap;
import com.zm.supplier.supplierinf.model.AccessToken;
import com.zm.supplier.supplierinf.model.TokenResult;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.EncryptUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.JSONUtil;
import com.zm.supplier.util.SignUtil;

public class LiangYouButtJoint extends AbstractSupplierButtJoint {

	private final String URL = "http://www.cnbuyers.cn/index.php?app=webService";

	private final String NEED_APP_ID_URL = URL + "&act={action}&app_id={appKey}&v=2.0&format=xml";

	private final String NEED_ACCESS_TOKEN_URL = URL + "&act={action}&access_token={token}&v=2.0&format=json";

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user) {
		String msg = ButtJointMessageUtils.getLiangYouOrderMsg(info, user);
		String url = NEED_APP_ID_URL.replace("{action}", "addOutOrder").replace("{appKey}", appKey);
		return sendLiangYouWarehouse(url, msg, SendOrderResult.class);
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		
		return null;
	}
	
	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		// TODO Auto-generated method stub
		return null;
	}

	private AccessToken getAccessToken(String url) {
		AccessToken token = AccessTokenCacheMap.getInstance().get(appKey);
		if (token != null && token.isAvailable()) {
			return token;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("app_secret", appSecret);
		params.put("app_id", appKey);
		params.put("v", "2.0");
		params.put("state", "xinhai");
		String sign = SignUtil.liangYouSign(params);
		String param = "act=access_token&app_id=" + appKey + "&v=2.0&sig=" + sign + "&state=xinhai";
		String result = HttpClientUtil.get(url, param);
		TokenResult tokenResult = JSONUtil.parse(result, TokenResult.class);
		tokenResult.getToken().setExpiresTime();
		AccessTokenCacheMap.getInstance().set(appKey, tokenResult.getToken());

		return tokenResult.getToken();
	}

	private <T> Set<T> sendLiangYouWarehouse(String url, String msg, Class<T> clazz) {
		msg = EncryptUtil.encrypt(msg, appSecret);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("paramjson", msg);
		
		logger.info("发送报文：" + msg);
		String result = HttpClientUtil.post(url, params);
		logger.info("返回：" + result);
		
		try {
			return renderResult(result, "JSON", clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		LiangYouButtJoint j = new LiangYouButtJoint();
		j.setAppKey("NSfAfv23540");
		j.setAppSecret("5e4f33959123b2d1");
		OrderInfo info = new OrderInfo();
		OrderDetail detail = new OrderDetail();
		info.setOrderId("GX1234GXG");
		detail.setReceiveAddress("北仑区");
		detail.setReceiveArea("北仑区");
		detail.setReceiveProvince("浙江省");
		detail.setReceiveCity("宁波市");
		detail.setReceiveName("test");
		detail.setReceivePhone("12345678901");
		info.setOrderDetail(detail);
		UserInfo user = new UserInfo();
		UserDetail d = new UserDetail();
		d.setName("test");
		d.setIdNum("12345678901234567");
		user.setUserDetail(d);
		OrderGoods goods = new OrderGoods();
		goods.setItemName("BananaBaby/香蕉宝宝 美妆 口腔护理 婴儿乳牙刷磨牙棒（章鱼款）适合0-2岁");
		goods.setSku("310516625460002238");
		goods.setItemQuantity(1);
		OrderGoods goods1 = new OrderGoods();
		goods1.setItemName("AHC 美妆 面膜 玻尿酸面膜（5片装）");
		goods1.setSku("310517625460000056");
		goods1.setItemQuantity(1);
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		list.add(goods1);
		list.add(goods);
		info.setOrderGoodsList(list);
		System.out.println(j.sendOrder(info, user));
		// System.out.println(j.getAccessToken("http://www.cnbuyers.cn/index.php?app=webService"));
	}

}
