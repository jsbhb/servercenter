package com.zm.order.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.order.bussiness.service.OrderStockOutService;
import com.zm.order.common.Pagination;
import com.zm.order.common.ResultModel;
import com.zm.order.constants.Constants;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.OrderInfoListForDownload;
import com.zm.order.pojo.ThirdOrderInfo;
import com.zm.order.pojo.bo.OrderMaintenanceBO;
import com.zm.order.pojo.bo.RebateDownload;

/**
 * ClassName: OrderBackController <br/>
 * Function: 订单后台API控制器. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class OrderStockOutController {

	@Resource
	OrderStockOutService orderStockOutService;

	@RequestMapping(value = "{version}/order/stockOut/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderInfo entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<OrderInfo> page = orderStockOutService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/order/stockOut/queryForPageForGoods", method = RequestMethod.POST)
	public ResultModel queryForPageForGoods(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderGoods entity) {

		if (Constants.FIRST_VERSION.equals(version)) {

			entity.setOrderId(request.getParameter("orderId"));

			Page<OrderGoods> page = orderStockOutService.queryByPageForGoods(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/order/stockOut/query", method = RequestMethod.POST)
	public ResultModel queryById(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderInfo entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getOrderId() == null || "".equals(entity.getOrderId())) {
					return new ResultModel(false, "没有编号信息");
				}

				OrderInfo result = orderStockOutService.queryByOrderId(entity.getOrderId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/order/stockOut/queryThirdInfo", method = RequestMethod.POST)
	public ResultModel queryThirdInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderInfo entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getOrderId() == null || "".equals(entity.getOrderId())) {
					return new ResultModel(false, "没有编号信息");
				}

				List<ThirdOrderInfo> result = orderStockOutService.queryThirdInfo(entity.getOrderId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	private final String REBATE_EXPORT = "1";

	@RequestMapping(value = "{version}/order/stockOut/queryOrdreInfoListForDownload", method = RequestMethod.POST)
	public ResultModel queryOrdreListForDownload(HttpServletRequest request, @PathVariable("version") Double version) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				String startTime = request.getParameter("startTime");
				String endTime = request.getParameter("endTime");
				String gradeId = request.getParameter("gradeId");
				String supplierId = request.getParameter("supplierId");
				String type = request.getParameter("type");
				if (startTime == null || endTime == null) {
					return new ResultModel(false, "查询日期为空");
				}

				if (REBATE_EXPORT.equals(type)) {
					Integer exportType = Integer.valueOf(request.getParameter("exportType"));
					List<RebateDownload> result = orderStockOutService.queryForRebate(startTime, endTime, gradeId,
							exportType);
					return new ResultModel(true, result);
				} else {
					List<OrderInfoListForDownload> result = orderStockOutService.queryOrdreListForDownload(startTime,
							endTime, gradeId, supplierId);
					return new ResultModel(true, result);
				}
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/order/stockOut/maintenance/express", method = RequestMethod.POST)
	public ResultModel maintenanceExpress(@PathVariable("version") Double version,
			@RequestBody List<OrderMaintenanceBO> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			orderStockOutService.maintenanceExpress(list);
			return new ResultModel(true, "success");
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order/import", method = RequestMethod.POST)
	public com.zm.order.pojo.ResultModel importOrder(@PathVariable("version") Double version,
			@RequestBody List<OrderInfo> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return orderStockOutService.importOrder(list);
			} catch (Exception e) {
				LogUtil.writeErrorLog("批量导入出错", e);
				if (e.getMessage().contains("Duplicate entry")) {
					return new com.zm.order.pojo.ResultModel(false,
							"订单号或支付单重复" + e.getMessage().substring(
									e.getMessage().indexOf("Duplicate entry") + "Duplicate entry".length(),
									e.getMessage().indexOf("for key")));
				}
				return new com.zm.order.pojo.ResultModel(false, e.getMessage());
			}
		}

		return new com.zm.order.pojo.ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order/stockOut/sendStockInGoodsInfoToMJY", method = RequestMethod.POST)
	public ResultModel sendStockInGoodsInfoToMJY(HttpServletRequest request, @PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				String orderId = request.getParameter("orderId");
				if (orderId == null) {
					return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
				}
				return orderStockOutService.getStockInGoodsInfoByOrderId(orderId);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order/stockOut/sendStockOutGoodsInfoToMJY", method = RequestMethod.POST)
	public ResultModel sendStockOutGoodsInfoToMJY(HttpServletRequest request, @PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				String orderId = request.getParameter("orderId");
				if (orderId == null) {
					return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
				}
				return orderStockOutService.getStockOutGoodsInfoByOrderId(orderId);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
