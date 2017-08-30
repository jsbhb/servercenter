package com.zm.goods.bussiness.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.ResultModel;

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

	@RequestMapping(value = "{version}/goods/big-trade", method = RequestMethod.GET)
	public ResultModel listBigTradeGoods(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {

		ResultModel result = new ResultModel();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);
		
		String type = req.getParameter("type");
		String categoryId = req.getParameter("categoryId");
		String goodsId = req.getParameter("goodsId");
		
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("type", type);
			param.put("categoryId", categoryId);
			param.put("goodsId", goodsId);
			List<GoodsItem> goodsList = goodsService.listBigTradeGoods(param);
			
			result.setSuccess(true);
			result.setObj(goodsList);
		}
		
		return result;
	}
	
	@RequestMapping(value = "{version}/goods/priceconstrast/{itemId}", method = RequestMethod.GET)
	public ResultModel listPriceConstrast(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("itemId") String itemId) {

		ResultModel result = new ResultModel();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);
		
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("itemId", itemId);
			param.put("startTime", startTime);
			param.put("endTime", endTime);
			List<PriceContrast> list = goodsService.listPriceContrast(param);
			
			result.setSuccess(true);
			result.setObj(list);
		}
		
		return result;
	}
	
	@RequestMapping(value = "{version}/goods/goodsSpecs/{itemId}", method = RequestMethod.GET)
	public ResultModel getGoodsSpecs(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @PathVariable("itemId") String itemId) {

		ResultModel result = new ResultModel();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);
		
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String,Object> resultMap = goodsService.tradeGoodsDetail(itemId);
			
			result.setSuccess(true);
			result.setObj(resultMap);
		}
		
		return result;
	}
}
