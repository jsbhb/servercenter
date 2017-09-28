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
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.ResultModel;
import com.zm.pay.utils.DateUtils;

@RestController
public class PayController {
	
	private static final Long time = (Constants.PAY_EFFECTIVE_TIME_HOUR - 1) * 3600000L;//支付有效期前一小时交易关闭
	
	@Resource
	OrderFeignClient orderFeignClient;
	
	@Resource
	PayService payService;

	@RequestMapping(value = "wxpay/{type}/{clientId}", method = RequestMethod.POST)
	public Map<String, String> wxPay(@PathVariable("clientId") Integer clientId, @PathVariable("type") String type,
			@RequestBody PayModel model, HttpServletRequest req) throws Exception {

		Map<String, String> result = payService.weiXinPay(clientId, type, model);

		return result;
	}


	@RequestMapping(value = "{version}/pay/{payType}/{type}/{orderId}", method = RequestMethod.POST)
	public ResultModel pay(@PathVariable("orderId") String orderId, @PathVariable("version") Double version,
			@PathVariable("type") String type, @PathVariable("payType") Integer payType, HttpServletRequest req)
					throws Exception {

		if (Constants.FIRST_VERSION.equals(version)) {

			OrderInfo info = orderFeignClient.getOrderByOrderIdForPay(version, orderId);
			if (info == null) {
				throw new RuntimeException("没有对应订单");
			}
			if (Constants.ORDER_CANCEL.equals(info.getStatus())) {
				throw new RuntimeException("该订单已经超时关闭");
			}
			//判断订单是否超时
			if(DateUtils.judgeDate(info.getCreateTime(), time)){
				orderFeignClient.closeOrder(version, orderId);
				throw new RuntimeException("该订单已经超时关闭");
			}
			
			//end
			//封装支付信息
			PayModel model = new PayModel();
			model.setBody("购物订单");
			model.setOrderId(info.getOrderId());
			model.setTotalAmount((int) (info.getOrderDetail().getPayment() * 100) + "");
			StringBuilder sb = new StringBuilder();
			for(OrderGoods goods : info.getOrderGoodsList()){
				sb.append(goods.getItemName() + "*" + goods.getItemQuantity() + ";");
			}
			model.setDetail(sb.toString().substring(0, sb.toString().length() - 1));
			//end
			if (Constants.ALI_PAY.equals(payType)) {

			}
			
			//微信支付
			if (Constants.WX_PAY.equals(payType)) {
				if (Constants.JSAPI.equals(type)) {
					if (req.getParameter("openId") == null || "".equals(req.getParameter("openId"))) {
						throw new RuntimeException("请先用微信授权登录");
					}
				}
				//微信特定参数
				model.setIP(req.getRemoteAddr());
				model.setOpenId(req.getParameter("openId"));
				Map<String, String> result = payService.weiXinPay(info.getCenterId(), type, model);
				
				if(!Constants.WX_PAY.equals(info.getOrderDetail().getPayType())){
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

}
