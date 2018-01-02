package com.zm.goods.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.goods.bussiness.service.GoodsBackService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.ResultModel;
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
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version, @RequestBody ThirdWarehouseGoods entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			entity.setSku(request.getParameter("sku"));
			entity.setItemCode(request.getParameter("itemCode"));
			String supplierId = request.getParameter("supplierId");
			if(supplierId != null&&!"".equals(supplierId)){
				entity.setSupplierId(Integer.parseInt(supplierId));
			}
			
			String status = request.getParameter("status");
			if(status != null&&!"".equals(status)){
				entity.setStatus(Integer.parseInt(status));
			}

			Page<ThirdWarehouseGoods> page = goodsBackService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/goods/save", method = RequestMethod.POST)
	public ResultModel save(HttpServletRequest request,@PathVariable("version") Double version, @RequestBody GoodsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try{
				goodsBackService.save(entity,request.getParameter("type"));
				return new ResultModel(true, "");
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			}
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
}
