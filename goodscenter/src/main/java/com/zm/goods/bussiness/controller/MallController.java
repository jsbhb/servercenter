package com.zm.goods.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.goods.bussiness.service.MallService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.ComponentData;
import com.zm.goods.pojo.ComponentPage;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.po.BigSalesGoodsRecord;

/**
 * ClassName: MallController <br/>
 * Function: 商城接口. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class MallController {

	@Resource
	MallService mallService;

	
	@RequestMapping(value = "{version}/mall/index/model/queryComponentForPage", method = RequestMethod.POST)
	public ResultModel queryComponentForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody ComponentPage entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<ComponentPage> page = mallService.queryComponentByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/mall/index/model/queryComponentDataByPageId", method = RequestMethod.POST)
	public ResultModel queryComponentDataByPageId(HttpServletRequest request, @PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String pageId = request.getParameter("pageId");
			List<ComponentData> dataList = mallService.queryComponentDataByPageId(pageId);
			return new ResultModel(true, dataList);
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/mall/index/model/updateComponentData", method = RequestMethod.POST)
	public ResultModel updateComponentData(@PathVariable("version") Double version, @RequestBody ComponentData data) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				mallService.updateComponentData(data);
				return new ResultModel(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/mall/index/model/mergeBigSaleData", method = RequestMethod.POST)
	public ResultModel mergeBigSaleData(@PathVariable("version") Double version, @RequestBody List<BigSalesGoodsRecord> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				mallService.mergeBigSaleData(list);
				return new ResultModel(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/mall/index/model/queryBigSaleData", method = RequestMethod.POST)
	public ResultModel queryBigSaleData(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, mallService.queryBigSaleData());
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
}
