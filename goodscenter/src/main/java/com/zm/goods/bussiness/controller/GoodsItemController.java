package com.zm.goods.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.goods.bussiness.service.GoodsItemService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.ResultModel;

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

	@RequestMapping(value = "{version}/goods/item/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			entity.setGoodsId(request.getParameter("goodsId"));
			entity.setGoodsName(request.getParameter("goodsName"));
			entity.setItemCode(request.getParameter("itemCode"));
			entity.setSku(request.getParameter("sku"));
			entity.setItemId(request.getParameter("itemId"));
			entity.setStatus(request.getParameter("status"));
			entity.setSupplierId(request.getParameter("supplierId"));
			Page<GoodsItemEntity> page = goodsItemService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/save", method = RequestMethod.POST)
	public ResultModel save(@PathVariable("version") Double version, @RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsItemService.save(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/item/beUse", method = RequestMethod.POST)
	public ResultModel beUse(@PathVariable("version") Double version, @RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsItemService.beUse(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
	@RequestMapping(value = "{version}/goods/item/beFx", method = RequestMethod.POST)
	public ResultModel beFx(@PathVariable("version") Double version, @RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsItemService.beFx(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/item/notBeFx", method = RequestMethod.POST)
	public ResultModel notBeFx(@PathVariable("version") Double version, @RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsItemService.notBeFx(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/query", method = RequestMethod.POST)
	public ResultModel queryById(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsItemEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				GoodsItemEntity result = goodsItemService.queryById(entity.getId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
}
