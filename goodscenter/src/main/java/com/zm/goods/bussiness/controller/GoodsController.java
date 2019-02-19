package com.zm.goods.bussiness.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * ClassName: GoodsController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
@Api(value = "商品服务中心内部接口", description = "商品服务中心内部接口")
public class GoodsController {

	@Resource
	GoodsService goodsService;

	@Resource
	GoodsService goodsTagDecorator;

	@RequestMapping(value = "auth/{version}/goods/base", method = RequestMethod.GET)
	@ApiOperation(value = "搜索商品接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public ResultModel listGoods(@PathVariable("version") Double version, @ModelAttribute Pagination pagination,
			@ModelAttribute GoodsSearch searchModel, @ModelAttribute SortModelList sortList,
			@RequestParam(value = "gradeId", required = false, defaultValue = "0") int gradeId,
			@RequestParam(value = "welfare", required = false, defaultValue = "false") boolean welfare) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				Map<String, Object> resultMap = goodsTagDecorator.queryGoods(searchModel, sortList, pagination, gradeId,
						welfare);
				return new ResultModel(true, resultMap);
			} catch (WrongPlatformSource e) {
				e.printStackTrace();
				return new ResultModel(false, "该分级没有福利商城的资格");
			}
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "auth/{version}/{centerId}/goods", method = RequestMethod.GET)
	@ApiOperation(value = "搜索商品接口(根据goodsId)", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "centerId", dataType = "Integer", required = true, value = "客户端ID"),
			@ApiImplicitParam(paramType = "query", name = "goodsId", dataType = "String", required = false, value = "商品ID"),
			@ApiImplicitParam(paramType = "query", name = "specsTpId", dataType = "String", required = false, value = "specsTpId") })
	public ResultModel listBigTradeGoods(@PathVariable("version") Double version, HttpServletRequest req,
			@PathVariable("centerId") Integer centerId,
			@RequestParam(value = "isApplet", required = false, defaultValue = "false") boolean isApplet) {

		ResultModel result = new ResultModel();

		String goodsId = req.getParameter("goodsId");
		String specsTpId = req.getParameter("specsTpId");
		if (goodsId != null && specsTpId != null) {
			result.setSuccess(false);
			result.setErrorMsg("参数不全");
			return result;
		}
		if (Constants.FIRST_VERSION.equals(version)) {
			result.setObj(goodsTagDecorator.listGoods(goodsId, specsTpId, isApplet));
			result.setSuccess(true);
		}

		return result;
	}

	@RequestMapping(value = "auth/{version}/goods/goodsSpecs", method = RequestMethod.GET)
	@ApiOperation(value = "获取多个商品规格接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "query", name = "itemIds", dataType = "String", required = true, value = "商品唯一编码itemId,以逗号隔开") })
	public ResultModel listGoodsSpecs(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res,
			@RequestParam(value = "platformSource", required = false, defaultValue = "0") int platformSource,
			@RequestParam(value = "gradeId", required = false, defaultValue = "0") int gradeId) {

		ResultModel result = new ResultModel();
		String ids = req.getParameter("specsTpIds");

		String[] idArr = ids.split(",");
		List<String> list = Arrays.asList(idArr);
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> resultMap = null;
			try {
				resultMap = goodsService.listGoodsSpecs(list, platformSource, gradeId);
			} catch (WrongPlatformSource e) {
				LogUtil.writeErrorLog("获取规格信息出错", e);
				result.setSuccess(false);
				result.setErrorMsg(e.getMessage());
				return result;
			}

			result.setSuccess(true);
			result.setObj(resultMap);
		}

		return result;
	}

	@RequestMapping(value = "auth/{version}/goods/navigation", method = RequestMethod.GET)
	@ApiOperation(value = "获取首页分类接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "query", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel loadIndexNavigation(@PathVariable("version") Double version,
			@RequestParam("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, goodsService.loadIndexNavigation(centerId));
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 一般贸易商品上架
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/trade/upShelves/{centerId}/{display}", method = RequestMethod.POST)
	public ResultModel tradeGoodsUpShelves(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestBody List<String> specsTpIdList,
			@PathVariable("display") int display) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (specsTpIdList == null || specsTpIdList.size() == 0) {
				return new ResultModel(false, "没有选择商品明细");
			}
			ResultModel result = goodsService.tradeGoodsUpShelves(specsTpIdList, centerId, display);
			return result;
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 跨境商品单个上架
	 * @param version
	 * @param centerId
	 * @param specsTpIdList
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/kj/signal/upShelves/{centerId}", method = RequestMethod.POST)
	public ResultModel signalKjGoodsUpShelves(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestBody List<String> specsTpIdList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (itemIdList == null || itemIdList.size() == 0) {
				return new ResultModel(false, "没有选择商品明细");
			}
			ResultModel result = goodsService.upShelves(itemIdList, centerId);
			return result;
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 跨境商品批量上架
	 * @param version
	 * @param centerId
	 * @param specsTpIdList
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/kj/batch/upShelves/{centerId}", method = RequestMethod.POST)
	public ResultModel batchKjGoodsUpShelves(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestBody List<String> specsTpIdList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (itemIdList == null || itemIdList.size() == 0) {
				return new ResultModel(false, "没有选择商品明细");
			}
			ResultModel result = goodsService.upShelves(itemIdList, centerId);
			return result;
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 商品下架
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/downShelves/{centerId}", method = RequestMethod.POST)
	public ResultModel downShelves(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@RequestParam("itemId") String itemId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String[] arr = itemId.split(",");
			List<String> itemIdList = Arrays.asList(arr);
			ResultModel result = goodsService.downShelves(itemIdList, centerId);
			return result;
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 同步库存
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/syncStock", method = RequestMethod.POST)
	public ResultModel syncStock(@PathVariable("version") Double version, @RequestBody List<String> itemIdList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsService.syncStock(itemIdList);
		}

		return new ResultModel(false, "版本错误");
	}

}
