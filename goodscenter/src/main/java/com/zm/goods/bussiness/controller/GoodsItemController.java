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
import com.zm.goods.bussiness.service.GoodsBackService;
import com.zm.goods.bussiness.service.GoodsItemService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.GoodsExtensionEntity;
import com.zm.goods.pojo.GoodsPriceRatioEntity;
import com.zm.goods.pojo.GoodsRatioPlatformEntity;
import com.zm.goods.pojo.ResultModel;

import springfox.documentation.annotations.ApiIgnore;

/**
 * ClassName: GoodsItemController <br/>
 * Function: 商品明细API接口. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class GoodsItemController {

	@Resource
	GoodsItemService goodsItemService;

	@Resource
	GoodsBackService goodsBackService;

	@RequestMapping(value = "{version}/goods/item/queryGoodsExtensionInfo", method = RequestMethod.POST)
	public ResultModel queryGoodsExtensionInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsExtensionEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			GoodsExtensionEntity result = goodsItemService.queryGoodsExtensionInfo(entity);
			return new ResultModel(true, result);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/updateGoodsExtensionInfo", method = RequestMethod.POST)
	public ResultModel updateGoodsExtensionInfo(@PathVariable("version") Double version,
			@RequestBody GoodsExtensionEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsItemService.updateGoodsExtension(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}


	@RequestMapping(value = "{version}/goods/item/queryGoodsRatioPlatformForPage", method = RequestMethod.POST)
	public ResultModel queryGoodsRatioPlatformForPage(HttpServletRequest request,
			@PathVariable("version") Double version, @RequestBody GoodsRatioPlatformEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<GoodsRatioPlatformEntity> page = goodsItemService.queryGoodsRatioPlanformPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/queryGoodsRatioPlatformForEdit", method = RequestMethod.POST)
	public ResultModel queryGoodsRatioPlatformForEdit(HttpServletRequest request,
			@PathVariable("version") Double version, @RequestBody GoodsRatioPlatformEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			GoodsRatioPlatformEntity ratioPlatformInfo = goodsItemService.queryGoodsRatioPlanformInfo(entity);
			return new ResultModel(true, ratioPlatformInfo);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/createGoodsRatioPlatformInfo", method = RequestMethod.POST)
	public ResultModel createGoodsRatioPlatformInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsRatioPlatformEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			goodsItemService.createGoodsRatioPlatformInfo(entity);
			return new ResultModel(true, "");
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/updateGoodsRatioPlatformInfo", method = RequestMethod.POST)
	public ResultModel updateGoodsRatioPlatformInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsRatioPlatformEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			goodsItemService.updateGoodsRatioPlatformInfo(entity);
			return new ResultModel(true, "");
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/syncGoodsPriceRatioInfo", method = RequestMethod.POST)
	public ResultModel syncGoodsPriceRatioInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody List<GoodsPriceRatioEntity> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			goodsItemService.syncGoodsPriceRatioInfo(list);
			return new ResultModel(true, "");
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/item/timeTaskQueryStockQtyNotEnoughList", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel stockQtyNotEnoughList(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsItemService.syncStockQtyNotEnoughItemList();
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/item/timeTaskQueryStockQtyEnoughList", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel stockQtyEnoughList(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsItemService.syncStockQtyEnoughItemList();
		}
		return new ResultModel(false, "版本错误");
	}
}
