package com.zm.goods.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.EshopGoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.pojo.EshopGoodsEntity;
import com.zm.goods.pojo.EshopGoodsInventoryEntity;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.OrderInfoDTO;
import com.zm.goods.pojo.ResultModel;

import io.swagger.annotations.Api;

/**
 * ClassName: GoodsController <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
@Api(value = "商品服务中心内部接口", description = "商品服务中心内部接口")
public class EshopGoodsController {

	@Resource
	EshopGoodsService eshopGoodsService;

	/**
	 * @fun 商品采购单新增接口
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/createPurchaseInfoForEshop", method = RequestMethod.POST)
	public ResultModel createPurchaseInfo(@PathVariable("version") Double version, @RequestBody OrderInfoDTO info) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null || 
				info.getOrderId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopGoodsService.createPurchaseInfo(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/goods/updateGoodsInfoForEshop", method = RequestMethod.POST)
	public ResultModel updateGoodsInfoForEshop(@PathVariable("version") Double version, @RequestBody EshopGoodsEntity goods) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (goods.getMallId() == null || goods.getGradeId() == null || 
				goods.getItemId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				eshopGoodsService.updateGoodsInfoForEshop(goods);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/goods/queryGoodsInfoForEshop", method = RequestMethod.POST)
	public ResultModel queryGoodsInfoForEshop(@PathVariable("version") Double version, @RequestBody EshopGoodsEntity goods) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (goods.getMallId() == null || goods.getGradeId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopGoodsService.queryGoodsInfoForEshop(goods);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	
	@RequestMapping(value = "{version}/goods/inventoryGoodsStockForEshop", method = RequestMethod.POST)
	public ResultModel inventoryGoodsStockForEshop(@PathVariable("version") Double version, @RequestBody EshopGoodsInventoryEntity goodsInventory) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (goodsInventory.check()) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				eshopGoodsService.inventoryGoodsStockForEshop(goodsInventory);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}
	@RequestMapping(value = "{version}/goods/queryGoodsInventoryInfoForEshop", method = RequestMethod.POST)
	public ResultModel queryGoodsInventoryInfoForEshop(@PathVariable("version") Double version, @RequestBody EshopGoodsInventoryEntity goodsInventory) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (goodsInventory.getMallId() == null || goodsInventory.getGradeId() == null ||
				goodsInventory.getOperationType() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopGoodsService.queryGoodsInventoryInfoForEshop(goodsInventory);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}

	/**
	 * @fun 商品库存校验接口
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/checkSellOrderGoodsStockForEshop", method = RequestMethod.POST)
	public ResultModel checkSellOrderGoodsStockForEshop(@PathVariable("version") Double version, @RequestBody OrderInfoDTO info) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null || 
				info.getOrderGoodsList() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopGoodsService.checkSellOrderGoodsStock(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}

	/**
	 * @fun 商品销售单新增接口
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/createSellOrderGoodsInfoForEshop", method = RequestMethod.POST)
	public ResultModel createSellOrderGoodsInfoForEshop(@PathVariable("version") Double version, @RequestBody OrderInfoDTO info) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (info.getMallId() == null || info.getGradeId() == null || 
				info.getOrderId() == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return eshopGoodsService.createSellOrderGoodsInfo(info);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		} else {
			return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
		}
	}

	/**
	 * @fun 根据商品GoodsId查询对应的ItemId信息接口
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/queryGoodsItemInfoByGoodsIdForEshop", method = RequestMethod.POST)
	public List<GoodsItemEntity> queryGoodsItemInfoByGoodsIdForEshop(@PathVariable("version") Double version, @RequestBody List<String> goodsIds) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (goodsIds == null || goodsIds.size() <= 0) {
				return null;
			}
			try {
				return eshopGoodsService.queryGoodsItemInfoByGoodsIdForEshop(goodsIds);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}
}
