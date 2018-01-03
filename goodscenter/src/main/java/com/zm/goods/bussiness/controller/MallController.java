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
import com.zm.goods.pojo.DictData;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.PopularizeDict;
import com.zm.goods.pojo.ResultModel;

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

	@RequestMapping(value = "{version}/mall/floor/queryDictForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody PopularizeDict entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String code = request.getParameter("code");
			if (code != null && !"".equals(code)) {
				entity.setCode(code);
			}
			entity.setCenterId(Integer.parseInt(request.getParameter("centerId")));
			Page<PopularizeDict> page = mallService.queryDictByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/mall/floor/saveDict", method = RequestMethod.POST)
	public ResultModel saveDict(@PathVariable("version") Double version, @RequestBody PopularizeDict entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				mallService.saveDict(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/mall/floor/queryDict", method = RequestMethod.POST)
	public ResultModel queryDict(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody PopularizeDict entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				PopularizeDict result = mallService.queryDictById(entity.getId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/mall/floor/queryDataForPage", method = RequestMethod.POST)
	public ResultModel queryDataForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody DictData entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String dictId = request.getParameter("dictId");
			if (dictId != null && "".equals(dictId)) {
				entity.setDictId(Integer.parseInt(dictId));
			}
			entity.setCenterId(Integer.parseInt(request.getParameter("centerId")));
			Page<DictData> page = mallService.queryDataByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/mall/floor/saveData", method = RequestMethod.POST)
	public ResultModel save(@PathVariable("version") Double version, @RequestBody DictData entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				mallService.saveData(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/mall/floor/queryData", method = RequestMethod.POST)
	public ResultModel queryById(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody DictData entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				PopularizeDict result = mallService.queryDataById(entity.getId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/mall/index/init", method = RequestMethod.POST)
	public ResultModel initDict(@PathVariable("version") Double version, @RequestBody PopularizeDict entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				mallService.initData(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/mall/index/queryDataAll", method = RequestMethod.POST)
	public ResultModel initData(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody Layout layout) {

		if (Constants.FIRST_VERSION.equals(version)) {
			List<DictData> list = mallService.queryDataAll(layout);
			return new ResultModel(true, list);
		}

		return new ResultModel(false, "版本错误");
	}

}
