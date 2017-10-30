package com.zm.pay.bussiness.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.pay.bussiness.service.PayService;
import com.zm.pay.constants.Constants;
import com.zm.pay.feignclient.OrderFeignClient;
import com.zm.pay.feignclient.model.OrderDetail;
import com.zm.pay.feignclient.model.OrderGoods;
import com.zm.pay.feignclient.model.OrderInfo;
import com.zm.pay.pojo.CustomModel;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.RefundPayModel;
import com.zm.pay.pojo.ResultModel;
import com.zm.pay.utils.DateUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "支付服务接口", description = "支付服务接口")
public class PayController {

	private static final Long time = (Constants.PAY_EFFECTIVE_TIME_HOUR - 1) * 3600000L;// 支付有效期前一小时交易关闭

	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	PayService payService;

	@RequestMapping(value = "wxpay/{type}/{clientId}", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, String> wxPay(@PathVariable("clientId") Integer clientId, @PathVariable("type") String type,
			@RequestBody PayModel model, HttpServletRequest req) throws Exception {

		return payService.weiXinPay(clientId, type, model);

	}

	@RequestMapping(value = "alipay/{type}/{clientId}", method = RequestMethod.POST)
	@ApiIgnore
	public String aliPay(@PathVariable("clientId") Integer clientId, @PathVariable("type") String type,
			@RequestBody PayModel model, HttpServletRequest req) throws Exception {

		return payService.aliPay(clientId, type, model);

	}

	@RequestMapping(value = "alipay/refund/{clientId}", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, Object> aliRefundPay(@PathVariable("clientId") Integer clientId,
			@RequestBody RefundPayModel model) throws Exception {

		return payService.aliRefundPay(clientId, model);

	}

	@RequestMapping(value = "wx/refund/{clientId}", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, Object> wxRefundPay(@PathVariable("clientId") Integer clientId,
			@RequestBody RefundPayModel model) throws Exception {

		return payService.wxRefundPay(clientId, model);

	}

	@RequestMapping(value = "{version}/pay/{payType}/{type}/{orderId}", method = RequestMethod.POST)
	@ApiOperation(value = "付款接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "payType", dataType = "Integer", required = true, value = "付款方式1微信，2支付宝"),
			@ApiImplicitParam(paramType = "path", name = "type", dataType = "String", required = true, value = "支付模式，如公众号支付JSAPI"),
			@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "订单号") })
	public ResultModel pay(@PathVariable("orderId") String orderId, @PathVariable("version") Double version,
			@PathVariable("type") String type, @PathVariable("payType") Integer payType, HttpServletRequest req)
					throws Exception {

		if (Constants.FIRST_VERSION.equals(version)) {

			OrderInfo info = orderFeignClient.getOrderByOrderIdForPay(version, orderId);
			if (info == null) {
				throw new RuntimeException("没有对应订单");
			}
			if (Constants.ORDER_CLOSE.equals(info.getStatus())) {
				throw new RuntimeException("该订单已经超时关闭");
			}
			if (Constants.ORDER_CANCEL.equals(info.getStatus())) {
				throw new RuntimeException("该订单已经退单");
			}
			// 判断订单是否超时
			if (DateUtils.judgeDate(info.getCreateTime(), time)) {
				orderFeignClient.closeOrder(version, orderId);
				throw new RuntimeException("该订单已经超时关闭");
			}
			// end
			// 封装支付信息
			PayModel model = new PayModel();
			model.setBody("购物订单");
			model.setOrderId(info.getOrderId());
			model.setTotalAmount((int) (info.getOrderDetail().getPayment() * 100) + "");
			StringBuilder sb = new StringBuilder();
			for (OrderGoods goods : info.getOrderGoodsList()) {
				sb.append(goods.getItemName() + "*" + goods.getItemQuantity() + ";");
			}
			model.setDetail(sb.toString().substring(0, sb.toString().length() - 1));
			// end
			if (Constants.ALI_PAY.equals(payType)) {
				if (!Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {
					OrderDetail detail = new OrderDetail();
					detail.setPayType(Constants.ALI_PAY);
					detail.setOrderId(info.getOrderId());
					orderFeignClient.updateOrderPayType(version, detail);
				}
				return new ResultModel(payService.aliPay(info.getCenterId(), type, model));
			}

			// 微信支付
			if (Constants.WX_PAY.equals(payType)) {
				if (Constants.JSAPI.equals(type)) {
					if (req.getParameter("openId") == null || "".equals(req.getParameter("openId"))) {
						throw new RuntimeException("请先用微信授权登录");
					}
				}
				// 微信特定参数
				model.setIP(req.getRemoteAddr());
				model.setOpenId(req.getParameter("openId"));
				Map<String, String> result = payService.weiXinPay(info.getCenterId(), type, model);

				if (!Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
					OrderDetail detail = new OrderDetail();
					detail.setPayType(Constants.WX_PAY);
					detail.setOrderId(info.getOrderId());
					orderFeignClient.updateOrderPayType(version, detail);
				}

				return new ResultModel(result);
			}
		}

		return null;
	}

	@RequestMapping(value = "pay/paycustom", method = RequestMethod.POST)
	@ApiIgnore
	public boolean payCustom(@RequestBody CustomModel model) throws Exception {

		return payService.payCustom(model);
	}

}
