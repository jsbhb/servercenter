package com.zm.supplier.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.supplier.bussiness.service.SupplierService;
import com.zm.supplier.common.Pagination;
import com.zm.supplier.common.ResultModel;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.log.LogUtil;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.SupplierEntity;

@RestController
public class SupplierController {

	@Resource
	SupplierService supplierService;

	@RequestMapping(value = "{version}/supplier/express/{supplierId}", method = RequestMethod.GET)
	public ResultModel listExpressBySupplierId(@PathVariable("version") Double version,
			@PathVariable("supplierId") Integer supplierId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return new ResultModel(true, supplierService.listExpressBySupplierId(supplierId));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/supplier/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version, @RequestBody SupplierEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<SupplierEntity> page = supplierService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/supplier/save", method = RequestMethod.POST)
	public ResultModel save(@PathVariable("version") Double version, @RequestBody SupplierEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {

			try {
				supplierService.saveSupplier(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/supplier/queryAll", method = RequestMethod.POST)
	public ResultModel queryAll(HttpServletRequest request, @PathVariable("version") Double version) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				List<SupplierEntity> result = supplierService.queryAll();
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/supplier/query", method = RequestMethod.POST)
	public ResultModel queryById(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody SupplierEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				SupplierEntity result = supplierService.queryById(entity.getId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/supplier/sendOrder", method = RequestMethod.POST)
	public ResultModel sendOrder(@PathVariable("version") Double version, @RequestBody List<OrderInfo> infoList) {

		if (Constants.FIRST_VERSION.equals(version)) {

			supplierService.sendOrder(infoList);
			return new ResultModel(true, "");
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/supplier/sendOrderCancel", method = RequestMethod.POST)
	public ResultModel sendOrderCancel(@PathVariable("version") Double version, @RequestBody OrderInfo info) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return supplierService.sendOrderCancel(info);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/supplier/checkOrderStatus", method = RequestMethod.POST)
	public ResultModel checkOrderStatus(@PathVariable("version") Double version,
			@RequestBody List<OrderIdAndSupplierId> list) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return supplierService.checkOrderStatus(list);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/supplier/checkStock/{supplierId}", method = RequestMethod.POST)
	public ResultModel checkStock(@PathVariable("version") Double version,
			@PathVariable("supplierId") Integer supplierId, @RequestBody List<OrderBussinessModel> list,
			@RequestParam(value = "flag", required = false, defaultValue = "false") boolean flag) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return supplierService.checkStock(list, supplierId, flag);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/supplier/getGoods/{supplierId}", method = RequestMethod.POST)
	public ResultModel getGoods(@PathVariable("version") Double version, @PathVariable("supplierId") Integer supplierId,
			@RequestParam("supplierName") String supplierName, @RequestBody List<String> list) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return supplierService.getGoods(list, supplierId, supplierName);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/supplier/butt-joint", method = RequestMethod.GET)
	public ResultModel getButtJointSupplier(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return supplierService.getButtJointSupplier();
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/supplier/update", method = RequestMethod.POST)
	public ResultModel update(@PathVariable("version") Double version, @RequestBody SupplierEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {

			try {
				supplierService.updateSupplier(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/handle/exception/order/{orderId}/{type}/{appType}", method = RequestMethod.POST)
	public ResultModel handleExceptionOrder(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId, @PathVariable("type") int type,
			@PathVariable("appType") int appType) {

		if (Constants.FIRST_VERSION.equals(version)) {

			try {
				supplierService.handleExceptionOrder(orderId, type,appType);
				return new ResultModel(true, "success");
			} catch (Exception e) {
				LogUtil.writeErrorLog("处理异常订单出错", e);
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
}
