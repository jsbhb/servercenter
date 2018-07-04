package com.zm.order.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.order.bussiness.service.EshopOrderService;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderInfoDTO;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.ResultModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
@Api(value = "Eshop订单类相关API", description = "Eshop订单类相关API")
public class EshopOrderController {

	@Resource
	EshopOrderService eshopOrderService;

	@RequestMapping(value = "{version}/order/userOrderListForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "获取用户订单列表接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel userOrderList(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			@ModelAttribute Pagination pagination, HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopOrderService.queryUserOrderList(info, pagination);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/order/userOrderDetailForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "获取用户订单明细接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel userOrderDetail(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null || info.getOrderId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopOrderService.queryUserOrderDetail(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/order/setUserOrderFlgForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "设置用户订单进货/撤销接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel setUserOrderFlg(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null || 
				info.getOrderId() == null || info.getIsEshopIn() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				eshopOrderService.setUserOrderFlg(info);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/order/userOrderInStockForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "用户订单入库处理接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel userOrderInStock(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null || 
				info.getOrderId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopOrderService.userOrderInstock(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/order/createSellOrderInfoForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "销售单新增处理接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel createSellOrderInfoForEshop(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null || 
				info.getOrderGoodsList() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopOrderService.createSellOrderInfo(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/order/confirmSellOrderInfoForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "销售单确认，库存扣减处理接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel confirmSellOrderInfoForEshop(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null || 
				info.getOrderId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopOrderService.confirmSellOrderInfo(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/order/querySellOrderInfoForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "查询销售单接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel querySellOrderInfoForEshop(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopOrderService.querySellOrderInfo(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/order/querySellOrderDetailForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "查询销售单详情接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel querySellOrderDetailForEshop(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null ||
				info.getOrderId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopOrderService.querySellOrderDetail(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/order/querySellOrderCountInfoForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "查询销售单统计接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel querySellOrderCountInfoForEshop(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopOrderService.querySellOrderCountInfo(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/order/querySellOrderGoodsCountInfoForEshop", method = RequestMethod.POST)
	@ApiOperation(value = "查询销售单商品统计接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel querySellOrderGoodsCountInfoForEshop(@PathVariable("version") Double version, @ModelAttribute OrderInfoDTO info,
			HttpServletRequest req, HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopOrderService.querySellOrderGoodsCountInfo(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
}