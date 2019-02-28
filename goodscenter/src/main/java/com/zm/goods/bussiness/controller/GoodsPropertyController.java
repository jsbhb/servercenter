package com.zm.goods.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsPropertyService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.PropertyEntity;
import com.zm.goods.pojo.PropertyValueEntity;
import com.zm.goods.pojo.ResultModel;

/**
 * ClassName: GoodsPropertyController <br/>
 * Function: 商品属性接口. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class GoodsPropertyController {

	@Resource
	GoodsPropertyService goodsPropertyService;

	@RequestMapping(value = "{version}/goods/property/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version, @RequestBody PropertyEntity entity,
			@RequestParam String hidTableId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (hidTableId.equals("property")) {
				return new ResultModel(true, goodsPropertyService.queryByPage(entity),
						new Pagination(goodsPropertyService.queryByPage(entity)));
			} else {
				return new ResultModel(true, goodsPropertyService.queryGuidePropertyByPage(entity),
						new Pagination(goodsPropertyService.queryGuidePropertyByPage(entity)));
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/addName", method = RequestMethod.POST)
	public ResultModel addName(@PathVariable("version") Double version, @RequestBody PropertyEntity entity,
			@RequestParam String hidTableId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsPropertyService.addGoodsPropertyName(entity, hidTableId);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/queryName", method = RequestMethod.POST)
	public ResultModel queryName(@PathVariable("version") Double version, @RequestBody PropertyEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, goodsPropertyService.queryName(entity));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/queryGuideName", method = RequestMethod.POST)
	public ResultModel queryGuideName(@PathVariable("version") Double version, @RequestBody PropertyEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, goodsPropertyService.queryGuideName(entity));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/editName", method = RequestMethod.POST)
	public ResultModel editName(@PathVariable("version") Double version, @RequestBody PropertyEntity entity,
			@RequestParam String hidTableId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsPropertyService.editGoodsPropertyName(entity, hidTableId);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/deleteName", method = RequestMethod.POST)
	public ResultModel deleteName(@PathVariable("version") Double version, @RequestParam String id,
			@RequestParam String hidTableId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsPropertyService.removeGoodsPropertyName(id, hidTableId);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/addValue", method = RequestMethod.POST)
	public ResultModel addValue(@PathVariable("version") Double version, @RequestParam PropertyValueEntity entity,
			@RequestParam String hidTableId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsPropertyService.addGoodsPropertyValue(entity, hidTableId);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/queryValue", method = RequestMethod.POST)
	public ResultModel queryValue(@PathVariable("version") Double version, @RequestBody PropertyValueEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, goodsPropertyService.queryValue(entity));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/queryGuideValue", method = RequestMethod.POST)
	public ResultModel queryGuideValue(@PathVariable("version") Double version, @RequestBody PropertyValueEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, goodsPropertyService.queryGuideValue(entity));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/editValue", method = RequestMethod.POST)
	public ResultModel editValue(@PathVariable("version") Double version, @RequestBody PropertyValueEntity entity,
			@RequestParam String hidTableId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsPropertyService.editGoodsPropertyValue(entity, hidTableId);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/property/deleteValue", method = RequestMethod.POST)
	public ResultModel deleteValue(@PathVariable("version") Double version, @RequestParam String id,
			@RequestParam String hidTableId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsPropertyService.removeGoodsPropertyValue(id, hidTableId);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
}
