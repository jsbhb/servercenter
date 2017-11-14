package com.zm.goods.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.CatalogService;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.FirstCatalogEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.SecondCatalogEntity;
import com.zm.goods.pojo.ThirdCatalogEntity;

/**
 * ClassName: CatalogController <br/>
 * Function: 分类接口. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class CatalogController {

	@Resource
	CatalogService catalogService;

	@RequestMapping(value = "{version}/goods/catalog/query", method = RequestMethod.GET)
	public ResultModel query(HttpServletRequest request, @PathVariable("version") Double version) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {

				List<FirstCatalogEntity> result = catalogService.queryAll();
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/catalog/queryFirstAll", method = RequestMethod.GET)
	public ResultModel queryFirstAll(HttpServletRequest request, @PathVariable("version") Double version) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {

				List<FirstCatalogEntity> result = catalogService.queryFirstAll();
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/catalog/querySecondByFirstId", method = RequestMethod.POST)
	public ResultModel querySecondByFirstId(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody SecondCatalogEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {

				List<SecondCatalogEntity> result = catalogService.querySecondByFirstId(entity.getFirstId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/catalog/queryThirdBySecondId", method = RequestMethod.POST)
	public ResultModel queryThirdBySecondId(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody ThirdCatalogEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {

				List<ThirdCatalogEntity> result = catalogService.queryThirdBySecondId(entity.getSecondId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/catalog/savefirst", method = RequestMethod.POST)
	public ResultModel savefirst(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody FirstCatalogEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				catalogService.saveFirstCatalog(entity);
				return new ResultModel(true, "");
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/catalog/savesecond", method = RequestMethod.POST)
	public ResultModel savesecond(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody SecondCatalogEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				catalogService.saveSecondCatalog(entity);
				return new ResultModel(true, "");
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/catalog/savethird", method = RequestMethod.POST)
	public ResultModel savethird(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody ThirdCatalogEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				catalogService.saveThirdCatalog(entity);
				return new ResultModel(true, "");
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, e.getMessage());
		}
	}
}
