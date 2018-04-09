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
import com.zm.goods.pojo.ERPGoodsTagBindEntity;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.GoodsPrice;
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
	
	@Resource
	GoodsBackService goodsBackService;

	@RequestMapping(value = "{version}/goods/item/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String gradeLevel = request.getParameter("gradeLevel");
			
//			if (entity.getTagBindEntity() != null) {
//				ERPGoodsTagBindEntity param = entity.getTagBindEntity();
//				//增加根据标签查询的条件
//				String itemIds = "";
//				List<ERPGoodsTagBindEntity> erpGoodsTagBindList = goodsBackService.queryGoodsTagBindListInfo(param);
//				for(ERPGoodsTagBindEntity ent:erpGoodsTagBindList) {
//					itemIds = itemIds + "'" + ent.getItemId() + "',";
//				}
//				if (erpGoodsTagBindList.size() > 0) {
//					itemIds = itemIds.substring(0,itemIds.length()-1);
//				}
//				param.setItemId(itemIds);
//				entity.setTagBindEntity(param);
//			}
			
			if ("1".equals(gradeLevel)) {
				Page<GoodsItemEntity> page = goodsItemService.queryByPage(entity);
				return new ResultModel(true, page, new Pagination(page));
			} else {
				String centerId = request.getParameter("centerId");
				if (centerId == null || "".equals(centerId)) {
					new ResultModel(false, "没有获取区域中心编号");
				}
				Page<GoodsItemEntity> page = goodsItemService.queryCenterByPage(entity, Integer.parseInt(centerId));
				return new ResultModel(true, page, new Pagination(page));
			}

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
	
	@RequestMapping(value = "{version}/goods/item/update", method = RequestMethod.POST)
	public ResultModel update(@PathVariable("version") Double version, @RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsItemService.update(entity);
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
	
	@RequestMapping(value = "{version}/goods/item/queryPurchaseItemForPage", method = RequestMethod.POST)
	public ResultModel queryPurchaseItemForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String centerId = request.getParameter("centerId");
			if (centerId == null || "".equals(centerId)) {
				new ResultModel(false, "没有获取订货平台编号");
			}
			Page<GoodsItemEntity> page = goodsItemService.queryPurchaseCenterByPage(entity, Integer.parseInt(centerId));
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/item/queryPurchaseItem", method = RequestMethod.POST)
	public ResultModel queryPurchaseItem(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<GoodsItemEntity> page = goodsItemService.queryPurchaseCenterItem(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/queryPurchaseItemForEdit", method = RequestMethod.POST)
	public ResultModel queryPurchaseItemForEdit(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsItemEntity entity) {
	
		if (Constants.FIRST_VERSION.equals(version)) {
			GoodsPrice result = goodsItemService.queryPurchaseCenterItemForEdit(entity);
			return new ResultModel(true, result);
		}
	
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/queryPurchaseItemForCheck", method = RequestMethod.POST)
	public ResultModel queryPurchaseItemForCheck(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsItemEntity entity) {
	
		if (Constants.FIRST_VERSION.equals(version)) {
			GoodsPrice result = goodsItemService.queryItemPrice(entity.getItemId());
			return new ResultModel(true, result);
		}
	
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/item/editPurchaseItem", method = RequestMethod.POST)
	public ResultModel editPurchaseItem(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsPrice entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				//先确认当前商品的起批量是否在总部设置的区间内
				GoodsPrice chkGoodsPrice = goodsItemService.queryItemPrice(entity.getItemId());
				if (chkGoodsPrice == null) {
					return new ResultModel(false, "当前商品数据异常，请联系系统管理员");
				}
				if (chkGoodsPrice.getMin() > entity.getMin()) {
					return new ResultModel(false, "当前商品最小起批量应大于等于"+chkGoodsPrice.getMin());
				}
				if (chkGoodsPrice.getMax() < entity.getMax()) {
					return new ResultModel(false, "当前商品最大起批量应小于等于"+chkGoodsPrice.getMax());
				}
				goodsItemService.updateItemPrice(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/queryForPageDownload", method = RequestMethod.POST)
	public ResultModel queryForPageDownload(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsItemEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String gradeLevel = request.getParameter("gradeLevel");

			if ("1".equals(gradeLevel)) {
				Page<GoodsEntity> page = goodsItemService.queryByPageDownload(entity);
				return new ResultModel(true, page, new Pagination(page));
			} else {
				String centerId = request.getParameter("centerId");
				if (centerId == null || "".equals(centerId)) {
					new ResultModel(false, "没有获取区域中心编号");
				}
				Page<GoodsEntity> page = goodsItemService.queryCenterByPageDownload(entity, Integer.parseInt(centerId));
				return new ResultModel(true, page, new Pagination(page));
			}

		}

		return new ResultModel(false, "版本错误");
	}
}
