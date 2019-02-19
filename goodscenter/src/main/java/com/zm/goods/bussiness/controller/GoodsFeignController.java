package com.zm.goods.bussiness.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsFeignService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsConvert;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.bo.GoodsItemBO;

import springfox.documentation.annotations.ApiIgnore;

/**
 * @fun feign微服务内部调用控制器
 * @author user
 *
 */
@RestController
public class GoodsFeignController {

	@Resource
	GoodsFeignService goodsFeignService;

	/**
	 * @fun 订单导入商品信息的补全
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/feign/manualordergoods/check", method = RequestMethod.POST)
	public ResultModel manualOrderGoodsCheck(@PathVariable("version") Double version,
			@RequestBody List<GoodsItemBO> list) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsFeignService.manualOrderGoodsCheck(list);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	/**
	 * @fun 订单商品获取商品信息，校验商品合法性等操作
	 * @param version
	 * @param req
	 * @param res
	 * @param list
	 * @param supplierId
	 * @param vip
	 * @param centerId
	 * @param orderFlag
	 * @param couponIds
	 * @param userId
	 * @param isFx
	 * @param platformSource
	 * @param gradeId
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/for-order", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel getPriceAndDelStock(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestBody List<OrderBussinessModel> list, Integer supplierId, boolean vip,
			Integer centerId, Integer orderFlag, @RequestParam(value = "couponIds", required = false) String couponIds,
			@RequestParam(value = "userId", required = false) Integer userId, boolean isFx, int platformSource,
			int gradeId) {

		ResultModel result = new ResultModel();
		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				result = goodsFeignService.getPriceAndDelStock(list, supplierId, vip, centerId, orderFlag, couponIds, userId,
						isFx, platformSource, gradeId);
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("【获取商品价格信息出错】", e);
			result.setErrorMsg(ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			result.setSuccess(false);
			result.setErrorCode(ErrorCodeEnum.SERVER_ERROR.getErrorCode());
		}

		return result;
	}
	
	/**
	 * @fun 库存回滚
	 */
	@RequestMapping(value = "{version}/goods/stockback", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel stockBack(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list,
			Integer orderFlag) {

		if (Constants.FIRST_VERSION.equals(version)) {

			goodsFeignService.stockBack(list, orderFlag);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 库存判断
	 */
	@RequestMapping(value = "{version}/goods/stockjudge/{orderFlag}/{supplierId}", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel stockJudge(@PathVariable("version") Double version,
			@PathVariable("supplierId") Integer supplierId, @PathVariable("orderFlag") Integer orderFlag,
			@RequestBody List<OrderBussinessModel> list) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return goodsFeignService.stockJudge(list, orderFlag, supplierId);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 根据同步到的库存更新库存信息
	 */
	@RequestMapping(value = "/{version}/goods/stock", method = RequestMethod.POST)
	public boolean updateThirdWarehouseStock(@PathVariable("version") Double version,
			@RequestBody List<WarehouseStock> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsFeignService.updateThirdWarehouseStock(list);
		}
		return false;
	}

	/**
	 * @fun 保存第三方同步到的商品
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "/{version}/goods/thirdGoods", method = RequestMethod.POST)
	public boolean saveThirdGoods(@PathVariable("version") Double version,
			@RequestBody List<ThirdWarehouseGoods> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsFeignService.saveThirdGoods(list);
		}
		return false;
	}

	/**
	 * @fun 获取需要同步库存的商品
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/checkStock", method = RequestMethod.GET)
	public List<OrderBussinessModel> checkStock(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsFeignService.checkStock();
		}

		return null;
	}
	
	/**
	 * @fun 推送订单时获取sku和换算比例
	 * @param version
	 * @param set
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/list-itemId", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, GoodsConvert> listSkuAndConversionByItemId(@PathVariable("version") Double version,
			@RequestBody Set<String> set) {
		if (Constants.FIRST_VERSION.equals(version)) {
			if (set == null || set.size() == 0) {
				return null;
			}
			return goodsFeignService.listSkuAndConversionByItemId(set);
		}

		return null;
	}

	@RequestMapping(value = "{version}/goods/cal-stock", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel calStock(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list,
			Integer supplierId, Integer orderFlag) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsFeignService.calStock(list, supplierId, orderFlag);
		}

		return null;
	}
	
	/**
	 * @fun 获取预售商品的itemId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/tag/presell", method = RequestMethod.POST)
	public List<String> listPreSellItemIds(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsFeignService.listPreSellItemIds();
		} 
		return null;
	}
}
