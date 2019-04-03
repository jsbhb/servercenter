package com.zm.order.bussiness.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.model.SendOrderResult;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderCount;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderIdAndSupplierId;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.PostFeeDTO;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.ShoppingCart;
import com.zm.order.pojo.ThirdOrderInfo;
import com.zm.order.pojo.bo.OrderStatusCallBack;
import com.zm.order.utils.JSONUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

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
@Api(value = "订单类相关API", description = "订单类相关API")
public class OrderController {

	@Resource
	OrderService orderService;

	@ApiOperation(value = "创建订单接口", response = ResultModel.class)
	@RequestMapping(value = "{version}/order", method = RequestMethod.POST, produces = "application/json;utf-8")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "type", dataType = "String", required = true, value = "支付模式，如公众号支付JSAPI"),
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel createOrder(@PathVariable("version") Double version, @RequestBody OrderInfo orderInfo,
			HttpServletResponse res, HttpServletRequest req) {

		ResultModel result = new ResultModel();

		String payType = orderInfo.getOrderDetail().getPayType() + "";
		String type = req.getParameter("type");

		if (payType == null || type == null || orderInfo.getShopId() == null) {
			result.setSuccess(false);
			result.setErrorCode(ErrorCodeEnum.MISSING_PARAM.getErrorCode());
			result.setErrorMsg(ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			return result;
		}

		if (Constants.FIRST_VERSION.equals(version)) {

			try {
				result = orderService.saveOrder(orderInfo, payType, type, req);
			} catch (DataIntegrityViolationException e) {
				e.printStackTrace();
				result.setSuccess(false);
				result.setErrorMsg("请确认字段是否填写完全");
				return result;

			} catch (Exception e) {
				LogUtil.writeErrorLog("创建订单出错", e);
				result.setSuccess(false);
				result.setErrorMsg(e.getMessage());
				return result;
			}
		}
		return result;
	}

	@RequestMapping(value = "{version}/order", method = RequestMethod.GET)
	@ApiOperation(value = "获取订单接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel listUserOrder(@PathVariable("version") Double version, @ModelAttribute OrderInfo info,
			@ModelAttribute Pagination pagination, HttpServletRequest req, HttpServletResponse res) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getShopId() == null) {
				return new ResultModel(true, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			result = orderService.listUserOrder(info, pagination);
		}

		return result;
	}

	@RequestMapping(value = "{version}/order/{userId}/{orderId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "用户删除订单接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "userId", dataType = "Integer", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "订单ID") })
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
	@ApiOperation(value = "用户确认订单接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "userId", dataType = "Integer", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "订单ID") })
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
	@ApiIgnore
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

	@RequestMapping(value = "{version}/order/cancel/{userId}/{orderId}", method = RequestMethod.POST)
	@ApiOperation(value = "用户取消订单接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "userId", dataType = "Integer", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "订单ID") })
	public ResultModel orderCancel(@PathVariable("version") Double version, @PathVariable("orderId") String orderId,
			@PathVariable("userId") Integer userId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return orderService.orderCancel(userId, orderId);
			} catch (Exception e) {
				LogUtil.writeErrorLog("用户取消订单接口", e);
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order/backcancel/{orderId}", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel orderBackCancel(@PathVariable("version") Double version, @PathVariable("orderId") String orderId,
			@RequestParam("payNo") String payNo) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return orderService.orderBackCancel(orderId, payNo);
			} catch (Exception e) {
				LogUtil.writeErrorLog("后台取消订单接口", e);
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order/getClientId/{orderId}", method = RequestMethod.GET)
	@ApiIgnore
	public OrderInfo getClientIdByOrderId(@PathVariable("orderId") String orderId,
			@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			OrderInfo info = orderService.getClientIdByOrderId(orderId);

			return info;
		}
		return null;
	}

	@RequestMapping(value = "{version}/order/shoping-cart", method = RequestMethod.POST, produces = "application/json;utf-8")
	@ApiOperation(value = "保存用户购物车接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"), })
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
			return result;
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/shoping-cart/{gradeId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "获取用户购物车接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "userId", dataType = "Integer", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType = "path", name = "gradeId", dataType = "Integer", required = true, value = "gradeID") })
	public ResultModel listShoppingCart(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("userId") Integer userId, @PathVariable("gradeId") Integer gradeId,
			Pagination pagination,
			@RequestParam(value = "platformSource", required = false, defaultValue = "0") int platformSource) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			ShoppingCart shoppingCart = new ShoppingCart();
			pagination.init();
			shoppingCart.setUserId(userId);
			shoppingCart.setGradeId(gradeId);
			shoppingCart.setPlatformSource(platformSource);

			List<ShoppingCart> list = null;
			try {
				list = orderService.listShoppingCart(shoppingCart, pagination);
			} catch (Exception e) {
				LogUtil.writeErrorLog("获取用户购物车接口", e);
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
			result.setSuccess(true);
			result.setObj(list);
			return result;
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/statusCount/{gradeId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "获取订单各个状态数量接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "userId", dataType = "Integer", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType = "path", name = "gradeId", dataType = "Integer", required = true, value = "gradeID") })
	public ResultModel getCountByStatus(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("userId") Integer userId, @PathVariable("gradeId") Integer gradeId,
			@RequestParam(value = "type", required = false, defaultValue = "0") String type) {

		ResultModel result = new ResultModel();
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("gradeId", gradeId);
			List<OrderCount> list = orderService.getCountByStatus(param, type);
			result.setSuccess(true);
			result.setObj(list);
			return result;
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/shoping-cart/{gradeId}/{userId}/{ids}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除用户购物车", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "userId", dataType = "Integer", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType = "path", name = "ids", dataType = "String", required = true, value = "id,多个用‘，’隔开") })
	public ResultModel removeShoppingCart(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("userId") Integer userId, @PathVariable("ids") String ids,
			@PathVariable("gradeId") Integer gradeId,
			@RequestParam(value = "platformSource", required = false, defaultValue = "0") int platformSource) {

		ResultModel result = new ResultModel();
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			String[] idArr = ids.split(",");
			param.put("userId", userId);
			param.put("idArr", idArr);
			param.put("gradeId", gradeId);
			param.put("platformSource", platformSource);
			orderService.removeShoppingCart(param);
			result.setSuccess(true);
			return result;
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/shoping-cart/count/{gradeId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "获取购物车数量接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "userId", dataType = "Integer", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType = "path", name = "gradeId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel countShoppingCart(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("userId") Integer userId, @PathVariable("gradeId") Integer gradeId,
			@RequestParam(value = "platformSource", required = false, defaultValue = "0") int platformSource) {

		ResultModel result = new ResultModel();
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("gradeId", gradeId);
			param.put("platformSource", platformSource);
			Integer count = orderService.countShoppingCart(param);
			result.setSuccess(true);
			result.setObj(count);
			return result;
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/shoping-cart/quantity/{gradeId}/{userId}/{itemId}", method = RequestMethod.GET)
	@ApiOperation(value = "根据itemId获取购物车内商品数量接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "userId", dataType = "Integer", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType = "path", name = "itemId", dataType = "String", required = true, value = "itemID"),
			@ApiImplicitParam(paramType = "path", name = "gradeId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel getShopCartQuantityByItemId(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("userId") Integer userId, @PathVariable("itemId") String itemId,
			@PathVariable("gradeId") Integer gradeId,
			@RequestParam(value = "platformSource", required = false, defaultValue = "0") int platformSource) {

		ResultModel result = new ResultModel();
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("itemId", itemId);
			param.put("gradeId", gradeId);
			param.put("platformSource", platformSource);
			Integer count = orderService.countShoppingCartQuantity(param);
			result.setSuccess(true);
			result.setObj(count == null ? 0 : count);
			return result;
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/pay/{orderId}", method = RequestMethod.GET)
	@ApiIgnore
	public OrderInfo getOrderByOrderIdForPay(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return orderService.getOrderByOrderIdForPay(orderId);
		}

		return null;

	}

	@RequestMapping(value = "{version}/order/payType", method = RequestMethod.POST, produces = "application/json;utf-8")
	@ApiIgnore
	public boolean updateOrderPayType(@PathVariable("version") Double version, @RequestBody OrderDetail detail) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return orderService.updateOrderPayType(detail);
		}

		return false;

	}

	@RequestMapping(value = "{version}/order/close/{userId}/{orderId}", method = RequestMethod.POST)
	@ApiOperation(value = "未付款时关闭订单接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "userId", dataType = "Integer", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "订单ID") })
	public ResultModel closeOrder(@PathVariable("version") Double version, @PathVariable("orderId") String orderId,
			@PathVariable("userId") Integer userId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			orderService.closeOrder(userId, orderId);
			return new ResultModel(true, "");
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/close", method = RequestMethod.GET)
	@ApiIgnore
	public ResultModel timeTaskcloseOrder(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {

			orderService.timeTaskcloseOrder();
			return new ResultModel(true, null);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/paycustom", method = RequestMethod.GET)
	@ApiIgnore
	public ResultModel payCustom(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, orderService.listPayCustomOrder());
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/paycustom/{orderId}", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel updatePayCustom(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			orderService.updatePayCustom(orderId);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/postfee", method = RequestMethod.POST, produces = "application/json;utf-8")
	@ApiOperation(value = "获取运费接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel getPostFee(@PathVariable("version") Double version, HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String data = req.getParameter("data");
			List<PostFeeDTO> postFee = null;
			try {
				postFee = JSONUtil.parse(data, new TypeReference<List<PostFeeDTO>>() {
				});
			} catch (RuntimeException e) {
				e.printStackTrace();
				return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
						ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());

			}
			for (PostFeeDTO dto : postFee) {
				if (!dto.valid()) {
					return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
							ErrorCodeEnum.MISSING_PARAM.getErrorCode());
				}
			}

			return new ResultModel(true, orderService.getPostFee(postFee));

		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/express", method = RequestMethod.GET)
	@ApiIgnore
	public ResultModel listExpress(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, orderService.listExpress());
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/saveThirdOrder", method = RequestMethod.POST)
	@ApiIgnore
	public boolean saveThirdOrder(@PathVariable("version") Double version, @RequestBody List<SendOrderResult> list) {

		if (Constants.FIRST_VERSION.equals(version)) {

			orderService.saveThirdOrder(list);
			return true;
		}

		throw new RuntimeException("版本错误");

	}

	@RequestMapping(value = "{version}/order/checkOrderStatus", method = RequestMethod.POST)
	public ResultModel checkOrderStatus(@PathVariable("version") Double version,
			@RequestBody List<OrderIdAndSupplierId> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return orderService.checkOrderStatus(list);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order/changeOrderStatusByThirdWarehouse", method = RequestMethod.POST)
	public boolean changeOrderStatusByThirdWarehouse(@PathVariable("version") Double version,
			@RequestBody List<ThirdOrderInfo> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			orderService.changeOrderStatusByThirdWarehouse(list);
			return true;
		}

		return false;

	}

	@RequestMapping(value = "{version}/order/profit/{shopId}", method = RequestMethod.GET)
	public ResultModel getProfit(@PathVariable("version") Double version, @PathVariable("shopId") Integer shopId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, orderService.getProfit(shopId));
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	@RequestMapping(value = "{version}/order/listUnDeliverOrder", method = RequestMethod.GET)
	public ResultModel listUnDeliverOrder(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, orderService.listUnDeliverOrder());
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order/confirmByTimeTask", method = RequestMethod.GET)
	public void confirmByTimeTask(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {

			orderService.confirmByTimeTask();
		}
	}

	@RequestMapping(value = "{version}/order/repayingPushJudge/{shopId}/{pushUserId}", method = RequestMethod.GET)
	public ResultModel repayingPushJudge(@PathVariable("version") Double version,
			@PathVariable("shopId") Integer shopId, @PathVariable("pushUserId") Integer pushUserId) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return orderService.repayingPushJudge(pushUserId, shopId);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order/pushUserOrderCount/{shopId}", method = RequestMethod.POST)
	public ResultModel pushUserOrderCount(@PathVariable("version") Double version,
			@PathVariable("shopId") Integer shopId, @RequestBody List<Integer> pushUserIdList) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return orderService.pushUserOrderCount(shopId, pushUserIdList);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order/sendToWarehouse", method = RequestMethod.GET)
	@ApiIgnore
	public ResultModel sendToWarehouse(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, orderService.listOrderForSendToWarehouse());
		}

		return new ResultModel(false, "版本错误");

	}

	/**
	 * @fun 退款中
	 * @param version
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "{version}/order/refunds/{orderId}", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel refunds(@PathVariable("version") Double version, @PathVariable("orderId") String orderId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return orderService.refunds(orderId);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

	/**
	 * @fun 轮询查询资金池不足的订单并重新计算
	 * @param version
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "{version}/order/capitalpool/notenough", method = RequestMethod.POST)
	@ApiIgnore
	public boolean capitalPoolRecount(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return orderService.capitalPoolRecount();
		}

		return false;

	}

	/**
	 * @fun 供应商订单状态回传
	 * @return
	 */
	@RequestMapping(value = "{version}/order/status/call-back", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel orderStatusCallBack(@PathVariable("version") Double version,
			@RequestBody OrderStatusCallBack callBack) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return orderService.orderStatusCallBack(callBack);
		}

		return null;
	}

	/**
	 * @fun 退款中
	 * @param version
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "{version}/order/refundsWithSendOrder/{orderId}", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel refundsWithSendOrder(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return orderService.refundsWithSendOrder(orderId);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 手动进行推送订单、订单申报、加签等操作
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "{version}/order/exception/{orderId}", method = RequestMethod.GET)
	public OrderInfo handleSupplierCenterExceptionOrder(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return orderService.handleSupplierCenterExceptionOrder(orderId);
		}
		return null;
	}

}