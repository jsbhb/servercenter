package com.zm.pay.bussiness.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.wxpay.sdk.WXPay;
import com.zm.pay.bussiness.dao.PayMapper;
import com.zm.pay.bussiness.service.PayService;
import com.zm.pay.constants.Constants;
import com.zm.pay.exception.PayUtilException;
import com.zm.pay.feignclient.GoodsFeignClient;
import com.zm.pay.feignclient.OrderFeignClient;
import com.zm.pay.feignclient.UserFeignClient;
import com.zm.pay.feignclient.model.OrderBussinessModel;
import com.zm.pay.feignclient.model.OrderDetail;
import com.zm.pay.feignclient.model.OrderGoods;
import com.zm.pay.feignclient.model.OrderInfo;
import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.CustomModel;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.RefundPayModel;
import com.zm.pay.pojo.ResultModel;
import com.zm.pay.pojo.UnionPayConfig;
import com.zm.pay.pojo.WeixinPayConfig;
import com.zm.pay.pojo.YopConfigModel;
import com.zm.pay.utils.CalculationUtils;
import com.zm.pay.utils.DateUtils;
import com.zm.pay.utils.ali.AliPayUtils;
import com.zm.pay.utils.unionpay.UnionPayUtil;
import com.zm.pay.utils.wx.WxPayUtils;
import com.zm.pay.utils.yop.YeepayService;

/**
 * ClassName: PayServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 11, 2017 3:45:10 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class PayServiceImpl implements PayService {

	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	PayMapper payMapper;

	private Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

	@Override
	public Map<String, String> weiXinPay(Integer clientId, String type, PayModel model) throws Exception {
		WeixinPayConfig config = (WeixinPayConfig) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.WX_PAY);

		if (config == null) {
			config = payMapper.getWeixinPayConfig(clientId);
			if (config == null) {
				Map<String, String> resp = new HashMap<String, String>();
				resp.put("error", "没有支付配置信息");
				return resp;
			}
			template.opsForValue().set(Constants.PAY + clientId + Constants.WX_PAY, config);
		}

		config.setHttpConnectTimeoutMs(5000);
		config.setHttpReadTimeoutMs(5000);

		Map<String, String> resp = WxPayUtils.unifiedOrder(type, config, model);

		String return_code = (String) resp.get("return_code");
		String result_code = (String) resp.get("result_code");
		Map<String, String> result = new HashMap<String, String>();

		if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {

			WxPayUtils.packageReturnParameter(clientId, type, model, config, resp, result);
		} else {
			result.put("success", "false");
			if ("SUCCESS".equals(return_code)) {
				result.put("errorMsg", (String) resp.get("err_code_des"));
			}
		}
		return result;
	}

	@Override
	public boolean payCustom(CustomModel model) throws Exception {
		if (Constants.WX_PAY.equals(model.getPayType())) {// 微信支付单报关
			return wxPayCustom(model);
		}
		if (Constants.ALI_PAY.equals(model.getPayType())) {// 支付宝支付单报关
			return aliPayCustom(model);
		}
		if (Constants.UNION_PAY.equals(model.getPayType())) {// 银联支付单报关由国际物流操作
			return true;
		}

		return false;
	}

	private boolean wxPayCustom(CustomModel model) throws Exception {
		WeixinPayConfig config = (WeixinPayConfig) template.opsForValue()
				.get(Constants.PAY + model.getCenterId() + Constants.WX_PAY);

		if (config == null) {
			config = payMapper.getWeixinPayConfig(model.getCenterId());
			if (config == null) {
				return false;
			}
			template.opsForValue().set(Constants.PAY + model.getCenterId() + Constants.WX_PAY, config);
		}

		config.setHttpConnectTimeoutMs(5000);
		config.setHttpReadTimeoutMs(5000);
		Map<String, String> result = WxPayUtils.acquireCustom(config, model);
		logger.info("微信支付报关:" + model.getOutRequestNo() + "====" + result);
		if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code"))) {
			String status = result.get("state");
			if ("SUBMITTED".equals(status) || "PROCESSING".equals(status) || "SUCCESS".equals(status)) {
				return true;
			}
		}
		return false;
	}

	private boolean aliPayCustom(CustomModel model) throws Exception {
		AliPayConfigModel config = (AliPayConfigModel) template.opsForValue()
				.get(Constants.PAY + model.getCenterId() + Constants.ALI_PAY);

		if (config == null) {
			config = payMapper.getAliPayConfig(model.getCenterId());
			if (config == null) {
				return false;
			}
			template.opsForValue().set(Constants.PAY + model.getCenterId() + Constants.WX_PAY, config);
		}

		Map<String, String> result = AliPayUtils.acquireCustom(config, model);
		logger.info("支付宝报关：" + model.getOutRequestNo() + "====" + result);
		if ("T".equals(result.get("is_success")) && "SUCCESS".equals(result.get("result_code"))) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> aliPay(Integer clientId, String type, PayModel model) throws Exception {
		AliPayConfigModel config = (AliPayConfigModel) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.ALI_PAY);

		Map<String, Object> result = new HashMap<String, Object>();
		if (config == null) {
			config = payMapper.getAliPayConfig(clientId);
			if (config == null) {
				result.put("error", "没有支付配置信息");
				return result;
			}
			template.opsForValue().set(Constants.PAY + clientId + Constants.ALI_PAY, config);
		}

		if (config.getUrl() == null) {
			String url = userFeignClient.getClientUrl(clientId, Constants.FIRST_VERSION);
			config.setUrl(url);
			template.opsForValue().set(Constants.PAY + clientId + Constants.ALI_PAY, config);
		}

		if (Constants.SCAN_CODE.equals(type)) {
			String htmlStr = AliPayUtils.aliPay(type, config, model);
			// AlipayTradePrecreateResponse response =
			// AliPayUtils.precreate(config, model);
			// if (response.isSuccess()) {
			result.put("success", true);
			result.put("htmlStr", htmlStr);
			// result.put("urlCode", response.getQrCode());
			// } else {
			// logger.info(response.getCode() + "=====" + response.getMsg() +
			// "," + response.getSubCode() + "====="
			// + response.getSubMsg());
			// result.put("success", false);
			// result.put("errorMsg", response.getMsg());
			// result.put("errorSubMsg", response.getSubMsg());
			// }
		}

		return result;
	}

	@Override
	public Map<String, Object> aliRefundPay(Integer clientId, RefundPayModel model) throws AlipayApiException {
		AliPayConfigModel config = (AliPayConfigModel) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.ALI_PAY);

		if (config == null) {
			config = payMapper.getAliPayConfig(clientId);
			if (config == null) {
				Map<String, Object> resp = new HashMap<String, Object>();
				resp.put("error", "没有支付配置信息");
				return resp;
			}
			template.opsForValue().set(Constants.PAY + clientId + Constants.WX_PAY, config);
		}

		AlipayTradeRefundResponse response = AliPayUtils.aliRefundPay(config, model);
		Map<String, Object> result = new HashMap<String, Object>();

		if (response.isSuccess()) {
			result.put("success", true);
			result.put("returnPayNo", response.getTradeNo());
		} else {
			logger.info(response.getCode() + "=====" + response.getMsg() + "," + response.getSubCode() + "====="
					+ response.getSubMsg());
			result.put("success", false);
			result.put("errorMsg", response.getMsg());
			result.put("errorSubMsg", response.getSubMsg());
		}

		return result;
	}

	@Override
	public Map<String, Object> wxRefundPay(Integer clientId, RefundPayModel model) throws Exception {
		WeixinPayConfig config = (WeixinPayConfig) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.WX_PAY);

		if (config == null) {
			config = payMapper.getWeixinPayConfig(clientId);
			if (config == null) {
				Map<String, Object> resp = new HashMap<String, Object>();
				resp.put("error", "没有支付配置信息");
				return resp;
			}
			template.opsForValue().set(Constants.PAY + clientId + Constants.WX_PAY, config);
		}

		config.setHttpConnectTimeoutMs(5000);
		config.setHttpReadTimeoutMs(5000);

		Map<String, String> resp = WxPayUtils.wxRefundPay(config, model);

		String return_code = (String) resp.get("return_code");
		String result_code = (String) resp.get("result_code");
		Map<String, Object> result = new HashMap<String, Object>();

		if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
			result.put("success", true);
			result.put("returnPayNo", resp.get("refund_id"));
		} else {
			result.put("success", false);
			if ("SUCCESS".equals(return_code)) {
				result.put("errorMsg", (String) resp.get("err_code_des"));
			}
		}

		return result;
	}

	private static final Long time = Constants.PAY_EFFECTIVE_TIME_HOUR * 3600000L;// 支付有效期1小时
	private static final Integer O2O_ORDER = 0;
	private static final Integer OWN_SUPPLIER = 1;
	private static final Integer GZ_SUPPLIER = 5;

	@Override
	public ResultModel pay(Double version, String type, Integer payType, HttpServletRequest req, String orderId)
			throws Exception {

		OrderInfo info = orderFeignClient.getOrderByOrderIdForPay(version, orderId);
		if (info == null) {
			return new ResultModel(false, "没有该订单");
		}
		if (Constants.ORDER_CLOSE.equals(info.getStatus())) {
			return new ResultModel(false, "该订单已经超时关闭");
		}
		if (Constants.ORDER_CANCEL.equals(info.getStatus())) {
			return new ResultModel(false, "该订单已经退单");
		}
		// 判断订单是否超时
		if (DateUtils.judgeDate(info.getCreateTime(), time)) {
			orderFeignClient.closeOrder(version, orderId, info.getUserId());
			return new ResultModel(false, "该订单已经超时关闭");
		}

		// 如果是跨境第三方仓库的，需要同步库存并判断库存
		if (O2O_ORDER.equals(info.getOrderFlag()) && !OWN_SUPPLIER.equals(info.getSupplierId())
				&& !GZ_SUPPLIER.equals(info.getSupplierId())) {
			OrderBussinessModel model = null;
			List<OrderBussinessModel> list = new ArrayList<OrderBussinessModel>();
			for (OrderGoods goods : info.getOrderGoodsList()) {
				model = new OrderBussinessModel();
				model.setItemId(goods.getItemId());
				model.setQuantity(goods.getItemQuantity());
				model.setOrderId(info.getOrderId());
				model.setSku(goods.getSku());
				model.setDeliveryPlace(info.getOrderDetail().getDeliveryPlace());
				list.add(model);
			}
			ResultModel resultModel = goodsFeignClient.stockJudge(Constants.FIRST_VERSION, info.getSupplierId(),
					info.getOrderFlag(), list);
			if (resultModel == null || !resultModel.isSuccess()) {
				return resultModel;
			}
		}

		// 封装支付信息
		PayModel model = new PayModel();
		model.setBody("购物订单");
		model.setOrderId(info.getOrderId());
		model.setTotalAmount((int) CalculationUtils.mul(info.getOrderDetail().getPayment(), 100) + "");
		StringBuilder sb = new StringBuilder();
		for (OrderGoods goods : info.getOrderGoodsList()) {
			sb.append(goods.getItemName() + "*" + goods.getItemQuantity() + ";");
		}
		if (sb.length() > 60) {// 支付宝描述过长会报错
			model.setDetail(sb.substring(0, 60) + "...");
		} else {
			model.setDetail(sb.toString().substring(0, sb.toString().length() - 1));
		}
		// end
		if (Constants.ALI_PAY.equals(payType)) {
			if (!Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {
				OrderDetail detail = new OrderDetail();
				detail.setPayType(Constants.ALI_PAY);
				detail.setOrderId(info.getOrderId());
				orderFeignClient.updateOrderPayType(version, detail);
			}
			return new ResultModel(aliPay(info.getCenterId(), type, model));
		}
		// 银联支付
		if (Constants.UNION_PAY.equals(payType)) {
			if (!Constants.UNION_PAY.equals(info.getOrderDetail().getPayType())) {
				OrderDetail detail = new OrderDetail();
				detail.setPayType(Constants.UNION_PAY);
				detail.setOrderId(info.getOrderId());
				orderFeignClient.updateOrderPayType(version, detail);
			}
			return new ResultModel(unionPay(info.getCenterId(), model, type));
		}

		// 微信支付
		if (Constants.WX_PAY.equals(payType)) {
			if (Constants.JSAPI.equals(type)) {
				if (req.getParameter("openId") == null || "".equals(req.getParameter("openId"))) {
					return new ResultModel(false, "请先用微信授权登录");
				}
			}
			// 微信特定参数
			model.setIP(req.getRemoteAddr());
			model.setOpenId(req.getParameter("openId"));
			Map<String, String> result = weiXinPay(info.getCenterId(), type, model);

			if (!Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
				OrderDetail detail = new OrderDetail();
				detail.setPayType(Constants.WX_PAY);
				detail.setOrderId(info.getOrderId());
				orderFeignClient.updateOrderPayType(version, detail);
			}
			if ("false".equals(result.get("success"))) {
				return new ResultModel(false, "微信调用失败:" + result.get("errorMsg"));
			} else {
				return new ResultModel(result);
			}
		}
		// 易宝支付
		if (Constants.YOP_PAY.equals(payType)) {
			if (!Constants.YOP_PAY.equals(info.getOrderDetail().getPayType())) {
				OrderDetail detail = new OrderDetail();
				detail.setPayType(Constants.YOP_PAY);
				detail.setOrderId(info.getOrderId());
				orderFeignClient.updateOrderPayType(version, detail);
			}
			model.setTotalAmount(info.getOrderDetail().getPayment() + "");
			model.setPhone(info.getOrderDetail().getReceivePhone());
			return new ResultModel(yopPay(info.getCenterId(), model));
		}
		return null;
	}

	@Override
	public Map<String, Object> unionPay(Integer clientId, PayModel model, String type) {
		UnionPayConfig config = (UnionPayConfig) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.UNION_PAY);
		Map<String, Object> result = new HashMap<String, Object>();
		if (config == null) {
			config = payMapper.getUnionPayConfig(clientId);
			if (config == null) {
				Map<String, Object> resp = new HashMap<String, Object>();
				resp.put("error", "没有支付配置信息");
				return resp;
			}
			template.opsForValue().set(Constants.PAY + clientId + Constants.UNION_PAY, config);
		}

		if (config.getUrl() == null) {
			String url = userFeignClient.getClientUrl(clientId, Constants.FIRST_VERSION);
			config.setUrl(url);
			template.opsForValue().set(Constants.PAY + clientId + Constants.UNION_PAY, config);
			template.opsForValue().set(Constants.PAY + clientId + Constants.UNION_PAY_MER_ID, config.getMerId());
		}

		String str = UnionPayUtil.unionPay(config, model, type);
		result.put("htmlStr", str);
		return result;
	}

	@Override
	public String testHttps() {
		WeixinPayConfig config = payMapper.getWeixinPayConfig(2);
		String mchId = config.getMchID();
		String nonceStr = System.currentTimeMillis() + "";
		String signStr = "mch_id=" + mchId + "&nonce_str=" + nonceStr + "&key=" + config.getKey();
		String sign = DigestUtils.md5Hex(signStr);
		WXPay wxpay = new WXPay(config);
		Map<String, String> data = new HashMap<String, String>();
		data.put("mch_id", mchId);
		data.put("nonce_str", nonceStr);
		data.put("sign", sign);
		try {
			return wxpay.requestWithoutCert("https://apitest.mch.weixin.qq.com/sandboxnew/pay/getsignkey", data, 5000,
					5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String yopPay(Integer clientId, PayModel model) throws IOException, PayUtilException {
		YopConfigModel config = (YopConfigModel) template.opsForValue()
				.get(Constants.PAY + clientId + Constants.YOP_PAY);

		if (config == null) {
			config = payMapper.getYopPayConfig(clientId);
			if (config == null) {
				throw new PayUtilException("没有支付配置信息");
			}
			template.opsForValue().set(Constants.PAY + clientId + Constants.YOP_PAY, config);
		}

		if (config.getUrl() == null) {
			String url = userFeignClient.getClientUrl(clientId, Constants.FIRST_VERSION);
			config.setUrl(url);
			template.opsForValue().set(Constants.PAY + clientId + Constants.YOP_PAY, config);
		}
		String goodsParamExt = "{\"goodsName\":\"" + model.getDetail() + "\",\"goodsDesc\":\"\"}";
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", model.getOrderId());
		try {
			params.put("orderAmount", CalculationUtils.div(model.getTotalAmount(), 100, 2) + "");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		// params.put("timeoutExpress",
		// timeoutExpress);//单位：分钟，默认24小时，最小1分钟，最大180天
		// params.put("requestDate", requestDate);//请求时间，用于计算订单有效期，格式 yyyy-MM-dd
		// HH:mm:ss，不传默认为易宝接收到请求的时间
		// params.put("redirectUrl", Constants.YOP_RETURN_URL);//默认停留在易宝支付完成页面
		params.put("notifyUrl", Constants.YOP_NOTIFY_URL);
		params.put("goodsParamExt", goodsParamExt);
		params.put("paymentParamExt", "");// 支付扩展信息当需要限制交易所使用的卡的时候，可以使用本参数来对支付的卡号，姓名，身份证进行限制，仅对快捷支付有效
		params.put("industryParamExt", "");// 行业拓展参数,预留字段，暂时不需要输入
		params.put("memo", "");// 自定义自身业务需要使用的字段，如对账时定义该订单应属的会计科目等最多支持21个中文或32位英文字符
		// params.put("riskParamExt", riskParamExt);//风控拓展参数,如需填写，请联系易宝技术支持提供

		Map<String, String> tempResult = new HashMap<String, String>();
		String uri = YeepayService.getUrl(YeepayService.TRADEORDER_URL);
		tempResult = YeepayService.requestYOP(params, uri, YeepayService.TRADEORDER, config);

		String token = tempResult.get("token");
		String codeRe = tempResult.get("code");
		if (!"OPR00000".equals(codeRe)) {
			String message = tempResult.get("message");
			throw new PayUtilException(message);
		}

		String parentMerchantNo = config.getParentMerchantNo();
		String merchantNo = config.getMerchantNo();

		params.put("parentMerchantNo", parentMerchantNo);
		params.put("merchantNo", merchantNo);
		params.put("orderId", model.getOrderId());
		params.put("token", token);
		params.put("userNo", model.getPhone());
		params.put("userType", "PHONE");

		String url = YeepayService.getUrl(params, config);
		System.out.println(url);
		return url;
	}
}
