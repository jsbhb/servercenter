package com.zm.goods.bussiness.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.zm.goods.pojo.ErrorCodeEnum;
import com.zm.goods.pojo.GoodsConvert;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.vo.TimeLimitActive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

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
			@ModelAttribute GoodsSearch searchModel, @ModelAttribute SortModelList sortList) {

		if (Constants.FIRST_VERSION.equals(version)) {
//			Map<String, Object> resultMap = goodsService.queryGoods(searchModel, sortList, pagination);
			Map<String, Object> resultMap = goodsTagDecorator.queryGoods(searchModel, sortList, pagination);
			return new ResultModel(true, resultMap);
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "auth/{version}/{centerId}/goods", method = RequestMethod.GET)
	@ApiOperation(value = "搜索商品接口(根据goodsId)", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "centerId", dataType = "Integer", required = true, value = "客户端ID"),
			@ApiImplicitParam(paramType = "query", name = "goodsId", dataType = "String", required = false, value = "商品ID"),
			@ApiImplicitParam(paramType = "query", name = "itemId", dataType = "String", required = false, value = "itemID") })
	public ResultModel listBigTradeGoods(@PathVariable("version") Double version, HttpServletRequest req,
			Pagination pagination, @PathVariable("centerId") Integer centerId,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "proportion", required = false, defaultValue = "false") boolean proportion) {

		ResultModel result = new ResultModel();

		String goodsId = req.getParameter("goodsId");
		String itemId = req.getParameter("itemId");
		if (goodsId != null && itemId != null) {
			result.setSuccess(false);
			result.setErrorMsg("参数错误");
			return result;
		}
		Map<String, Object> param = new HashMap<String, Object>();

		if (centerId == null) {
			result.setSuccess(false);
			result.setErrorMsg("参数不全");
			return result;
		}

		if (Constants.FIRST_VERSION.equals(version)) {
			param.put("centerId", "_" + centerId);
			param.put("goodsId", goodsId);
			param.put("itemId", itemId);
			pagination.init();
			param.put("pagination", pagination);

//			result.setObj(goodsService.listGoods(param, centerId, userId, proportion));
			result.setObj(goodsTagDecorator.listGoods(param, centerId, userId, proportion));
			result.setSuccess(true);
		}

		return result;
	}

	@RequestMapping(value = "auth/{version}/goods/priceconstrast/{itemId}", method = RequestMethod.GET)
	@ApiOperation(value = "商品价格比价接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "itemId", dataType = "String", required = true, value = "商品唯一编码itemId"),
			@ApiImplicitParam(paramType = "query", name = "startTime", dataType = "String", required = false, value = "开始时间"),
			@ApiImplicitParam(paramType = "query", name = "endTime", dataType = "String", required = false, value = "结束时间"),
			@ApiImplicitParam(paramType = "query", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel listPriceConstrast(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("itemId") String itemId) {

		ResultModel result = new ResultModel();

		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		Integer centerId = Integer.valueOf(req.getParameter("centerId"));

		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("itemId", itemId);
			param.put("centerId", "_" + centerId);
			param.put("startTime", startTime);
			param.put("endTime", endTime);
			List<PriceContrast> list = goodsService.listPriceContrast(param);

			result.setSuccess(true);
			result.setObj(list);
		}

		return result;
	}

	@RequestMapping(value = "auth/{version}/goods/goodsSpecs/{centerId}/{itemId}", method = RequestMethod.GET)
	@ApiOperation(value = "获取单个商品规格接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "itemId", dataType = "String", required = true, value = "商品唯一编码itemId"),
			@ApiImplicitParam(paramType = "path", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel getGoodsSpecs(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("itemId") String itemId,
			@PathVariable("centerId") Integer centerId) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> resultMap = goodsService.tradeGoodsDetail(itemId, centerId);

			result.setSuccess(true);
			result.setObj(resultMap);
		}

		return result;
	}

	@RequestMapping(value = "auth/{version}/goods/goodsSpecs", method = RequestMethod.GET)
	@ApiOperation(value = "获取多个商品规格接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "query", name = "itemIds", dataType = "String", required = true, value = "商品唯一编码itemId,以逗号隔开"),
			@ApiImplicitParam(paramType = "query", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel listGoodsSpecs(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res,
			@RequestParam(value = "source", required = false, defaultValue = "mall") String source) {

		ResultModel result = new ResultModel();
		String ids = req.getParameter("itemIds");
		Integer centerId = Integer.valueOf(req.getParameter("centerId"));

		String[] idArr = ids.split(",");
		List<String> list = Arrays.asList(idArr);
		if (Constants.FIRST_VERSION.equals(version)) {
//			Map<String, Object> resultMap = goodsService.listGoodsSpecs(list, centerId, source);
			Map<String, Object> resultMap = goodsTagDecorator.listGoodsSpecs(list, centerId, source);

			result.setSuccess(true);
			result.setObj(resultMap);
		}

		return result;
	}

	@RequestMapping(value = "{version}/goods/for-order", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel getPriceAndDelStock(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestBody List<OrderBussinessModel> list, Integer supplierId, boolean vip,
			Integer centerId, Integer orderFlag, @RequestParam(value = "couponIds", required = false) String couponIds,
			@RequestParam(value = "userId", required = false) Integer userId) {

		ResultModel result = new ResultModel();
		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				result = goodsService.getPriceAndDelStock(list, supplierId, vip, centerId, orderFlag, couponIds,
						userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setErrorMsg(ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			result.setSuccess(false);
			result.setErrorCode(ErrorCodeEnum.SERVER_ERROR.getErrorCode());
		}

		return result;
	}

	@RequestMapping(value = "{version}/goods/for-buttjoinorder", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel delButtjoinOrderStock(@PathVariable("version") Double version,
			@RequestBody List<OrderBussinessModel> list, Integer supplierId, Integer orderFlag) {
		ResultModel result = new ResultModel();
		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				result = goodsService.delButtjoinOrderStock(list, supplierId, orderFlag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setErrorMsg(ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			result.setSuccess(false);
			result.setErrorCode(ErrorCodeEnum.SERVER_ERROR.getErrorCode());
		}

		return result;
	}

	@RequestMapping(value = "auth/{version}/goods/active", method = RequestMethod.GET)
	@ApiOperation(value = "获取活动接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "query", name = "typeStatus", dataType = "Integer", required = true, value = "活动范围，1全场，0特定区域"),
			@ApiImplicitParam(paramType = "query", name = "type", dataType = "Integer", required = false, value = "活动类型：0：限时抢购；1：满减，2满打折"),
			@ApiImplicitParam(paramType = "query", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel getActivity(@PathVariable("version") Double version,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam("typeStatus") Integer typeStatus, @RequestParam("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("typeStatus", typeStatus);
			if (!Constants.ACTIVE_AREA.equals(typeStatus)) {
				param.put("type", type);
			}
			param.put("centerId", "_" + centerId);

			return new ResultModel(true, goodsService.getActivity(param));
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "auth/{version}/goods/modular/{centerId}/{page}/{pageType}", method = RequestMethod.GET)
	@ApiOperation(value = "获取模块布局接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "page", dataType = "String", required = true, value = "页面"),
			@ApiImplicitParam(paramType = "path", name = "pageType", dataType = "Integer", required = true, value = "页面类型：0：PC；1：手机"),
			@ApiImplicitParam(paramType = "path", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel getModular(@PathVariable("version") Double version, @PathVariable("page") String page,
			@PathVariable("centerId") Integer centerId, @PathVariable("pageType") Integer pageType) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, goodsService.getModular(page, centerId, pageType));
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "auth/{version}/goods/modulardata/{centerId}/{page}/{pageType}", method = RequestMethod.POST, produces = "application/json;utf-8")
	@ApiOperation(value = "获取模块和数据接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "page", dataType = "String", required = true, value = "页面"),
			@ApiImplicitParam(paramType = "path", name = "pageType", dataType = "Integer", required = true, value = "页面类型：0：PC；1：手机"),
			@ApiImplicitParam(paramType = "path", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel getModularData(@PathVariable("version") Double version, @RequestBody Layout layout,
			@PathVariable("page") String page, @PathVariable("centerId") Integer centerId,
			@PathVariable("pageType") Integer pageType) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, goodsService.getModularData(pageType, page, layout, centerId));
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "{version}/goods/table/{centerId}", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel createTable(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId) {
		if (Constants.FIRST_VERSION.equals(version)) {

			goodsService.createTable(centerId);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "{version}/goods/active/start/{centerId}/{activeId}", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel startActive(@PathVariable("version") Double version, @PathVariable("activeId") Integer activeId,
			@PathVariable("centerId") Integer centerId) {
		if (Constants.FIRST_VERSION.equals(version)) {

			goodsService.updateActiveStart(centerId, activeId);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/active/end/{centerId}/{activeId}", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel endActive(@PathVariable("version") Double version, @PathVariable("activeId") Integer activeId,
			@PathVariable("centerId") Integer centerId) {
		if (Constants.FIRST_VERSION.equals(version)) {

			goodsService.updateActiveEnd(centerId, activeId);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/endactive", method = RequestMethod.GET)
	@ApiIgnore
	public ResultModel getEndActive(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {

			Map<String, Object> result = goodsService.getEndActive();
			return new ResultModel(true, result);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/createlucene", method = RequestMethod.GET)
	@ApiOperation(value = "创建lucene接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "query", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel createLucene(@PathVariable("version") Double version,
			@RequestParam("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			goodsService.createGoodsLucene(centerId);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, "版本错误");
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
	 * @fun 库存回滚
	 */
	@RequestMapping(value = "{version}/goods/stockback", method = RequestMethod.POST)
	@ApiIgnore
	public ResultModel stockBack(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list,
			Integer orderFlag) {

		if (Constants.FIRST_VERSION.equals(version)) {

			goodsService.stockBack(list, orderFlag);
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

			return goodsService.stockJudge(list, orderFlag, supplierId);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 获取限时抢购商品
	 */
	@RequestMapping(value = "auth/{version}/goods/timelimit/{centerId}", method = RequestMethod.GET)
	@ApiOperation(value = "获取限时抢购数据接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel getTimelimitGoods(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			List<TimeLimitActive> list = goodsService.getTimelimitGoods(centerId);
			if (list == null) {
				return new ResultModel(true, "目前没有正在进行或即将开始的限时抢购活动");
			}

			return new ResultModel(true, list);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 获取有特殊属性的商品
	 * 
	 */
	@RequestMapping(value = "auth/{version}/goods/specialgoods/{centerId}/{type}", method = RequestMethod.GET)
	@ApiOperation(value = "获取特殊属性商品接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "type", dataType = "type", required = true, value = "页面类型：0：新品；1：特推，2渠道，3精选"),
			@ApiImplicitParam(paramType = "path", name = "centerId", dataType = "Integer", required = true, value = "客户端ID") })
	public ResultModel getSpecialGoods(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @PathVariable("type") Integer type) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, goodsService.listSpecialGoods(centerId, type));
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
			return goodsService.updateThirdWarehouseStock(list);
		}
		return false;
	}

	@RequestMapping(value = "/{version}/goods/thirdGoods", method = RequestMethod.POST)
	public boolean saveThirdGoods(@PathVariable("version") Double version,
			@RequestBody List<ThirdWarehouseGoods> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsService.saveThirdGoods(list);
		}
		return false;
	}

	/**
	 * @fun 根据list 获取渠道价格总价
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/costPrice", method = RequestMethod.POST)
	public Double getCostPrice(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsService.getCostPrice(list);
		}

		return 0.0;
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
			return goodsService.checkStock();
		}

		return null;
	}

	/**
	 * @fun 商品上架
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/upShelves/{centerId}", method = RequestMethod.POST)
	public ResultModel upShelves(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@RequestBody List<String> itemIdList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (itemIdList == null || itemIdList.size() == 0) {
				return new ResultModel(false, "没有选择商品明细");
			}
			return goodsService.upShelves(itemIdList, centerId);
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
			return goodsService.downShelves(itemIdList, centerId);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 总部不可分销时区域中心全部下架
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/unDistribution", method = RequestMethod.POST)
	public ResultModel unDistribution(@PathVariable("version") Double version, @RequestParam("itemId") String itemId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String[] arr = itemId.split(",");
			List<String> itemIdList = Arrays.asList(arr);
			return goodsService.unDistribution(itemIdList);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 商品同步
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/syncgoods/{centerId}", method = RequestMethod.POST)
	public ResultModel syncgoods(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@RequestBody List<String> itemIdList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsService.syncgoods(itemIdList, centerId);
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

	/**
	 * @fun 订货平台商品同步
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/itemSync/{centerId}", method = RequestMethod.POST)
	public ResultModel itemSync(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@RequestBody List<String> itemIdList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (itemIdList == null || itemIdList.size() == 0) {
				return new ResultModel(false, "没有选择要同步的商品");
			}
			return goodsService.syncgoods(itemIdList, centerId);
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/list-itemId", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, GoodsConvert> listSkuAndConversionByItemId(@PathVariable("version") Double version, @RequestBody Set<String> set){
		if (Constants.FIRST_VERSION.equals(version)) {
			if (set == null || set.size() == 0) {
				return null;
			}
			return goodsService.listSkuAndConversionByItemId(set);
		}
		
		return null;
	}

}
