package com.zm.goods.bussiness.controller;

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
import com.zm.goods.bussiness.service.GoodsBackService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.pojo.ERPGoodsTagEntity;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsFielsMaintainBO;
import com.zm.goods.pojo.GoodsInfoEntity;
import com.zm.goods.pojo.GoodsInfoListForDownload;
import com.zm.goods.pojo.GoodsListDownloadParam;
import com.zm.goods.pojo.GoodsRebateEntity;
import com.zm.goods.pojo.GoodsStockEntity;
import com.zm.goods.pojo.GoodsTagBindEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.TagFuncEntity;
import com.zm.goods.pojo.ThirdWarehouseGoods;

/**
 * ClassName: GoodsBackController <br/>
 * Function: 商品PAI控制器. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class GoodsBackController {

	@Resource
	GoodsBackService goodsBackService;

	@RequestMapping(value = "{version}/goods/goods/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version, @RequestBody GoodsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<GoodsEntity> page = goodsBackService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/third/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody ThirdWarehouseGoods entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			entity.setSku(request.getParameter("sku"));
			entity.setItemCode(request.getParameter("itemCode"));
			String supplierId = request.getParameter("supplierId");
			if (supplierId != null && !"".equals(supplierId)) {
				entity.setSupplierId(Integer.parseInt(supplierId));
			}

			String status = request.getParameter("status");
			if (status != null && !"".equals(status)) {
				entity.setStatus(Integer.parseInt(status));
			}

			Page<ThirdWarehouseGoods> page = goodsBackService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goods/save", method = RequestMethod.POST)
	public ResultModel save(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.save(entity, request.getParameter("type"));
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goods/saveDetailPath", method = RequestMethod.POST)
	public ResultModel saveDetailPath(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.saveDetailPath(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goods/query", method = RequestMethod.POST)
	public ResultModel queryById(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				GoodsEntity result = goodsBackService.queryById(entity.getId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/goods/queryThird", method = RequestMethod.POST)
	public ResultModel queryThird(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody ThirdWarehouseGoods entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				ThirdWarehouseGoods result = goodsBackService.queryThird(entity);
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/goods/edit", method = RequestMethod.POST)
	public ResultModel edit(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				GoodsEntity result = goodsBackService.checkRecordForUpd(entity);
				if (result == null) {
					goodsBackService.edit(entity);
					return new ResultModel(true, "");
				} else {
					return new ResultModel(false, "该商品下的明细状态非不可分销，无法进行修改");
				}
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goods/remove", method = RequestMethod.POST)
	public ResultModel remove(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				GoodsEntity result = goodsBackService.checkRecordForDel(entity);
				if (result == null) {
					goodsBackService.remove(entity);
					return new ResultModel(true, "");
				} else {
					return new ResultModel(false, "该商品下的明细状态非初始化，无法进行删除");
				}
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsRebate/queryAllGoods", method = RequestMethod.POST)
	public ResultModel queryAllGoods(@PathVariable("version") Double version, @RequestBody GoodsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<GoodsRebateEntity> page = goodsBackService.queryAllGoods(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsRebate/queryById", method = RequestMethod.POST)
	public ResultModel queryById(@PathVariable("version") Double version, @RequestParam("itemId") String itemId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, goodsBackService.queryById(itemId));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsRebate/updateRebate", method = RequestMethod.POST)
	public ResultModel updateRebate(@PathVariable("version") Double version,
			@RequestBody List<GoodsRebateEntity> entityList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.insertGoodsRebate(entityList);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/queryForPage", method = RequestMethod.POST)
	public ResultModel goodsTagQueryForPage(@PathVariable("version") Double version,
			@RequestBody ERPGoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<ERPGoodsTagEntity> page = goodsBackService.queryTagForPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/queryTagInfo", method = RequestMethod.POST)
	public ResultModel queryTagInfo(@PathVariable("version") Double version, @RequestBody ERPGoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {

			ERPGoodsTagEntity result = goodsBackService.queryTagInfo(entity);
			return new ResultModel(true, result);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/queryTagListInfo", method = RequestMethod.POST)
	public ResultModel queryTagListInfo(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {

			List<ERPGoodsTagEntity> result = goodsBackService.queryTagListInfo();
			return new ResultModel(true, result);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/saveTag", method = RequestMethod.POST)
	public ResultModel saveTag(@PathVariable("version") Double version, @RequestBody ERPGoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.insertGoodsTag(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/modifyTag", method = RequestMethod.POST)
	public ResultModel modifyTag(@PathVariable("version") Double version, @RequestBody ERPGoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.updateGoodsTag(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/removeTag", method = RequestMethod.POST)
	public ResultModel removeTag(@PathVariable("version") Double version, @RequestBody ERPGoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.deleteGoodsTag(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/tagFunc", method = RequestMethod.POST)
	public ResultModel tagFunc(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				List<TagFuncEntity> result = goodsBackService.queryTagFuncList();
				return new ResultModel(true, result);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goods/saveGoodsInfo", method = RequestMethod.POST)
	public ResultModel saveGoodsInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsInfoEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return goodsBackService.saveGoodsInfo(entity);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goods/queryGoodsInfoEntity", method = RequestMethod.POST)
	public ResultModel queryGoodsInfoEntity(HttpServletRequest request, @PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String itemId = request.getParameter("itemId");
			GoodsInfoEntity goodsInfo = goodsBackService.queryGoodsInfoEntity(itemId);
			return new ResultModel(true, goodsInfo);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goods/updateGoodsInfo", method = RequestMethod.POST)
	public ResultModel updateGoodsInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsInfoEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return goodsBackService.updateGoodsInfo(entity);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/rebate", method = RequestMethod.GET)
	public ResultModel getGoodsRebate(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestParam("itemId") String itemId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsBackService.getGoodsRebate(itemId);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/queryGoodsInfoListForDownload", method = RequestMethod.POST)
	public ResultModel queryGoodsInfoListForDownload(HttpServletRequest request,
			@PathVariable("version") Double version, @RequestBody GoodsListDownloadParam param) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				List<GoodsInfoListForDownload> result = goodsBackService.queryGoodsListForDownload(param);
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/item/maintainStockByItemId", method = RequestMethod.POST)
	public ResultModel maintainStockByItemId(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody List<GoodsStockEntity> stocks) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (stocks.size() <= 0) {
					return new ResultModel(false, "参数值为空");
				}
				goodsBackService.maintainStockByItemId(stocks);
				return new ResultModel(true, "");
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/maintain/files", method = RequestMethod.POST)
	public ResultModel maintainFiles(@PathVariable("version") Double version,
			@RequestBody List<GoodsFielsMaintainBO> list) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, goodsBackService.maintainFiles(list));
			} catch (Exception e) {
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/goods/import/goods", method = RequestMethod.POST)
	public ResultModel importGoods(@PathVariable("version") Double version, @RequestBody List<GoodsInfoEntity> list) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsBackService.importGoods(list);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/goods/tag/batch-bind", method = RequestMethod.POST)
	public ResultModel tagBatchBind(@PathVariable("version") Double version,
			@RequestBody List<GoodsTagBindEntity> list) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsBackService.tagBatchBind(list);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/goods/goods/saveItemInfo", method = RequestMethod.POST)
	public ResultModel saveItemInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody GoodsInfoEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return goodsBackService.saveItemInfo(entity);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/publish/exception/{centerId}/{type}", method = RequestMethod.POST)
	public ResultModel listPublishExceptionGoods(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestBody GoodsEntity entity,
			@PathVariable("version") Integer type) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				Page<GoodsEntity> page = goodsBackService.listPublishExceptionGoods(type, entity, centerId);
				return new ResultModel(true, page, new Pagination(page));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
}
