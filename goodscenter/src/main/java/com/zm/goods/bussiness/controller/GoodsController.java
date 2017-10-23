package com.zm.goods.bussiness.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;

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
public class GoodsController {

	@Resource
	GoodsService goodsService;

	@RequestMapping(value = "auth/{version}/goods/base", method = RequestMethod.GET)
	public ResultModel listGoods(@PathVariable("version") Double version, Pagination pagination,
			GoodsSearch searchModel, SortModelList sortList) {
		
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> resultMap = goodsService.queryGoods(searchModel, sortList, pagination);
			return new ResultModel(true, resultMap);
		}
		
		return new ResultModel(false,"版本错误");

	}

	@RequestMapping(value = "auth/{version}/goods", method = RequestMethod.GET)
	public ResultModel listBigTradeGoods(@PathVariable("version") Double version, HttpServletRequest req,
			Pagination pagination, HttpServletResponse res) {

		ResultModel result = new ResultModel();

		String type = req.getParameter("type");
		String categoryId = req.getParameter("categoryId");
		String goodsId = req.getParameter("goodsId");
		String centerId = req.getParameter("centerId");
		Map<String, Object> param = new HashMap<String, Object>();

		if (type == null || centerId == null) {
			result.setSuccess(false);
			result.setErrorMsg("参数不全");
			return result;
		}

		if (Constants.FIRST_VERSION.equals(version)) {
			if (Constants.O2O_CENTERID.equals(centerId) || Constants.BIG_TRADE_CENTERID.equals(centerId)) {
				param.put("centerId", "");
			} else {
				param.put("centerId", "_" + centerId);
			}
			param.put("centerId", centerId);
			param.put("type", type);
			param.put("categoryId", categoryId);
			param.put("goodsId", goodsId);
			pagination.init();
			param.put("pagination", pagination);
			List<GoodsItem> goodsList = goodsService.listGoods(param);

			result.setSuccess(true);
			result.setObj(goodsList);
		}

		return result;
	}

	@RequestMapping(value = "{version}/goods/priceconstrast/{itemId}", method = RequestMethod.GET)
	public ResultModel listPriceConstrast(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("itemId") String itemId) {

		ResultModel result = new ResultModel();

		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String centerId = req.getParameter("centerId");

		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("itemId", itemId);
			if (Constants.O2O_CENTERID.equals(centerId) || Constants.BIG_TRADE_CENTERID.equals(centerId)) {
				param.put("centerId", "");
			} else {
				param.put("centerId", "_" + centerId);
			}
			param.put("startTime", startTime);
			param.put("endTime", endTime);
			List<PriceContrast> list = goodsService.listPriceContrast(param);

			result.setSuccess(true);
			result.setObj(list);
		}

		return result;
	}

	@RequestMapping(value = "auth/{version}/goods/goodsSpecs/{centerId}/{itemId}", method = RequestMethod.GET)
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

	@RequestMapping(value = "{version}/goods/goodsSpecs", method = RequestMethod.GET)
	public ResultModel listGoodsSpecs(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {

		ResultModel result = new ResultModel();
		String ids = req.getParameter("ids");
		Integer centerId = Integer.valueOf(req.getParameter("centerId"));
		
		String[] idArr = ids.split(",");
		List<String> list = Arrays.asList(idArr);
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> resultMap = goodsService.listGoodsSpecs(list, centerId);

			result.setSuccess(true);
			result.setObj(resultMap);
		}

		return result;
	}

	@RequestMapping(value = "{version}/goods/for-order", method = RequestMethod.POST)
	public ResultModel getPriceAndDelStock(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestBody List<OrderBussinessModel> list, boolean delStock, boolean vip,
			Integer centerId, Integer orderFlag) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			result = goodsService.getPriceAndDelStock(list, delStock, vip, centerId, orderFlag);
		}

		return result;
	}

	@RequestMapping(value = "{version}/goods/active", method = RequestMethod.GET)
	public ResultModel getActivity(@PathVariable("version") Double version,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam("typeStatus") Integer typeStatus, @RequestParam("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("typeStatus", typeStatus);
			if (!Constants.ACTIVE_AREA.equals(typeStatus)) {
				param.put("type", type);
			}
			if (Constants.O2O_CENTERID.equals(centerId) || Constants.BIG_TRADE_CENTERID.equals(centerId)) {
				param.put("centerId", "");
			} else {
				param.put("centerId", "_" + centerId);
			}

			return new ResultModel(true, goodsService.getActivity(param));
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "{version}/goods/modular/{centerId}/{page}", method = RequestMethod.GET)
	public ResultModel getModular(@PathVariable("version") Double version, @PathVariable("page") String page,
			@PathVariable("centerId") Integer centerId) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, goodsService.getModular(page, centerId));
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "{version}/goods/modulardata/{centerId}/{page}", method = RequestMethod.POST)
	public ResultModel getModularData(@PathVariable("version") Double version, @RequestBody Layout layout,
			@PathVariable("page") String page, @PathVariable("centerId") Integer centerId, HttpServletRequest req) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, goodsService.getModularData(page, layout, centerId));
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "{version}/goods/table/{centerId}", method = RequestMethod.POST)
	public ResultModel createTable(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId) {
		if (Constants.FIRST_VERSION.equals(version)) {

			goodsService.createTable(centerId);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "{version}/goods/active/start/{centerId}/{activeId}", method = RequestMethod.POST)
	public ResultModel startActive(@PathVariable("version") Double version, @PathVariable("activeId") Integer activeId,
			@PathVariable("centerId") Integer centerId) {
		if (Constants.FIRST_VERSION.equals(version)) {

			goodsService.updateActiveStart(centerId, activeId);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/active/end/{centerId}/{activeId}", method = RequestMethod.POST)
	public ResultModel endActive(@PathVariable("version") Double version, @PathVariable("activeId") Integer activeId,
			@PathVariable("centerId") Integer centerId) {
		if (Constants.FIRST_VERSION.equals(version)) {

			goodsService.updateActiveEnd(centerId, activeId);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/endactive", method = RequestMethod.GET)
	public ResultModel getEndActive(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {

			Map<String, Object> result = goodsService.getEndActive();
			return new ResultModel(true, result);
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/createlucene", method = RequestMethod.GET)
	public ResultModel createLucene(@PathVariable("version") Double version, @RequestParam("centerId") Integer centerId){
		
		if (Constants.FIRST_VERSION.equals(version)) {

			goodsService.createGoodsLucene(centerId);
			return new ResultModel(true, null);
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/navigation", method = RequestMethod.GET)
	public ResultModel loadIndexNavigation(@PathVariable("version") Double version, @RequestParam("centerId") Integer centerId){
		
		if (Constants.FIRST_VERSION.equals(version)) {
			
			return new ResultModel(true, goodsService.loadIndexNavigation(centerId));
		}

		return new ResultModel(false, "版本错误");
	}
}
