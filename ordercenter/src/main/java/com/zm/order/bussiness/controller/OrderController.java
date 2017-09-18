package com.zm.order.bussiness.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.order.bussiness.service.OrderService;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.AbstractPayConfig;
import com.zm.order.pojo.OrderCount;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.ShoppingCart;
import com.zm.order.pojo.WeiXinPayConfig;
import com.zm.order.utils.JSONUtil;

/**
 * ClassName: OrderController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 11, 2017 11:27:08 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class OrderController {

	@Resource
	OrderService orderService;

	@RequestMapping(value = "{version}/order", method = RequestMethod.POST)
	public ResultModel createOrder(@PathVariable("version") Double version, @RequestBody OrderInfo orderInfo,
			HttpServletResponse res, HttpServletRequest req) {

		ResultModel result = new ResultModel();

		String payType = orderInfo.getOrderDetail().getPayType() + "";
		String type = req.getParameter("type");
		AbstractPayConfig payConfig = null;
		if(Constants.WX_PAY.equals(payType)){
			String openId = req.getParameter("openId");
			if(Constants.JSAPI.equals(type)){
				if(openId == null || "".equals(openId)){
					result.setSuccess(false);
					result.setErrorMsg("请使用微信授权登录");
					return result;
				}
			}
			String ip = req.getRemoteAddr();
			payConfig = new WeiXinPayConfig(openId, ip);
		}

		if (payType == null || type == null) {
			result.setSuccess(false);
			result.setErrorMsg("参数不全");
			return result;
		}

		if (Constants.FIRST_VERSION.equals(version)) {

			try {
				result = orderService.saveOrder(orderInfo, payType, type, payConfig);
			} catch (DataIntegrityViolationException e) {
				e.printStackTrace();
				result.setSuccess(false);
				result.setErrorMsg("请确认字段是否填写完全");
				return result;

			} catch (Exception e) {
				e.printStackTrace();
				result.setSuccess(false);
				result.setErrorMsg("微服务出现问题");
				return result;
			}
		}
		return result;
	}

	@RequestMapping(value = "{version}/order", method = RequestMethod.GET)
	public ResultModel listUserOrder(@PathVariable("version") Double version, OrderInfo info, Pagination pagination,
			HttpServletRequest req, HttpServletResponse res) {

		ResultModel result = new ResultModel();

		if (info.getOrderFlag() == null) {
			result.setSuccess(false);
			result.setErrorMsg("参数不全");
			return result;
		}

		if (Constants.FIRST_VERSION.equals(version)) {
			result = orderService.listUserOrder(info, pagination);
		}

		return result;
	}

	@RequestMapping(value = "{version}/order/{userId}/{orderId}", method = RequestMethod.DELETE)
	public ResultModel removeUserOrder(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("orderId") String orderId, HttpServletRequest req, HttpServletResponse res) {

		ResultModel result = new ResultModel();
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("userId", userId);
		param.put("orderId", orderId);

		if (Constants.FIRST_VERSION.equals(version)) {
			result = orderService.removeUserOrder(param);
		}

		return result;
	}

	@RequestMapping(value = "{version}/order/confirm/{userId}/{orderId}", method = RequestMethod.PUT)
	public ResultModel confirmUserOrder(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("orderId") String orderId, HttpServletRequest req, HttpServletResponse res) {

		ResultModel result = new ResultModel();

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("userId", userId);
		param.put("orderId", orderId);

		if (Constants.FIRST_VERSION.equals(version)) {
			result = orderService.confirmUserOrder(param);
		}

		return result;
	}

	@RequestMapping(value = "{version}/order/alread-pay/{orderId}", method = RequestMethod.PUT)
	public ResultModel updateOrderPayStatusByOrderId(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId, HttpServletRequest req, HttpServletResponse res) {

		ResultModel result = new ResultModel();

		String payNo = req.getParameter("payNo");

		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("orderId", orderId);
			param.put("payNo", payNo);
			result = orderService.updateOrderPayStatusByOrderId(param);
		}

		return result;
	}

	@RequestMapping(value = "{version}/order/cancel", method = RequestMethod.PUT)
	public ResultModel orderCancel(@PathVariable("version") Double version, @RequestBody OrderInfo info,
			HttpServletRequest req, HttpServletResponse res) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			result = orderService.orderCancel(info);
		}

		return result;
	}

	@RequestMapping(value = "{version}/order/getClientId/{orderId}", method = RequestMethod.GET)
	public Integer getClientIdByOrderId(@PathVariable("orderId") String orderId,
			@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Integer clientId = orderService.getClientIdByOrderId(orderId);

			return clientId;
		}
		return null;
	}

	@RequestMapping(value = "{version}/order/shoping-cart", method = RequestMethod.POST)
	public ResultModel saveShoppingCart(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestBody ShoppingCart cart) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {

			if (!cart.check()) {
				result.setSuccess(false);
				result.setErrorMsg("参数不全");
				return result;
			}

			orderService.saveShoppingCart(cart);
			result.setSuccess(true);
		}

		return result;

	}

	@RequestMapping(value = "{version}/order/shoping-cart/{centerId}/{userId}", method = RequestMethod.GET)
	public ResultModel listShoppingCart(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("userId") Integer userId, @PathVariable("centerId") Integer centerId,
			Pagination pagination) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			pagination.init();
			param.put("userId", userId);
			param.put("centerId", centerId);
			param.put("pagination", pagination);

			List<ShoppingCart> list = null;
			try {
				list = orderService.listShoppingCart(param);
			} catch (Exception e) {
				result.setSuccess(false);
				result.setErrorMsg("后台出错");
				e.printStackTrace();
				return result;
			}
			result.setSuccess(true);
			result.setObj(list);

		}

		return result;

	}

	@RequestMapping(value = "{version}/order/statusCount/{centerId}/{userId}", method = RequestMethod.GET)
	public ResultModel getCountByStatus(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("userId") Integer userId,
			@PathVariable("centerId") Integer centerId) {

		ResultModel result = new ResultModel();
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("centerId", centerId);
			List<OrderCount> list = orderService.getCountByStatus(param);
			result.setSuccess(true);
			result.setObj(list);

		}
		return result;

	}

	@RequestMapping(value = "{version}/order/shoping-cart/{userId}/{ids}", method = RequestMethod.DELETE)
	public ResultModel removeShoppingCart(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("userId") Integer userId, @PathVariable("ids") String ids) {

		ResultModel result = new ResultModel();
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			String[] idArr = ids.split(",");
			param.put("userId", userId);
			param.put("idArr", idArr);
			orderService.removeShoppingCart(param);
			result.setSuccess(true);

		}
		return result;

	}

	@RequestMapping(value = "{version}/order/shoping-cart/count/{centerId}/{userId}", method = RequestMethod.GET)
	public ResultModel countShoppingCart(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("userId") Integer userId,
			@PathVariable("centerId") Integer centerId) {

		ResultModel result = new ResultModel();
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("centerId", centerId);
			Integer count = orderService.countShoppingCart(param);
			result.setSuccess(true);
			result.setObj(count);
		}
		return result;

	}

	public static void main(String[] args) {
		OrderInfo info = new OrderInfo();
		info.setCombinationId("123GXS");
		info.setCenterId(0);
		info.setUserId(1);
		info.setSupplierId(1);
		info.setExpressType(1);
		info.setTdq(1);
		OrderDetail detail = new OrderDetail();
		info.setOrderFlag(0);
		detail.setReceiveAddress("asdfasdf");
		detail.setReceiveArea("fdsafdsa");
		detail.setReceiveZipCode("123123");
		detail.setReceiveProvince("ewq1");
		detail.setReceiveName("test");
		detail.setReceivePhone("13456123123");
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		OrderGoods goods = new OrderGoods();
		goods.setItemId("bl01");
		goods.setActualPrice(100.00);
		goods.setItemPrice(100.00);
		goods.setItemQuantity(1);
		list.add(goods);
		info.setOrderDetail(detail);
		info.setOrderGoodsList(list);

		System.out.println(JSONUtil.toJson(info));

	}

}