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
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.ERPGoodsTagEntity;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsInfoEntity;
import com.zm.goods.pojo.GoodsRebateEntity;
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
	
	@RequestMapping(value = "{version}/goods/goods/saveDetailPath", method = RequestMethod.POST)
	public ResultModel saveDetailPath(HttpServletRequest request,@PathVariable("version") Double version, @RequestBody GoodsEntity entity) {
		
		if (Constants.FIRST_VERSION.equals(version)) {
			try{
				goodsBackService.saveDetailPath(entity);
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
	
	@RequestMapping(value = "{version}/goods/goods/edit", method = RequestMethod.POST)
	public ResultModel edit(HttpServletRequest request,@PathVariable("version") Double version, @RequestBody GoodsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try{
				GoodsEntity result = goodsBackService.checkRecordForUpd(entity);
				if (result == null) {
					goodsBackService.edit(entity);
					return new ResultModel(true, "");
				} else {
					return new ResultModel(false, "该商品下的明细状态非不可分销，无法进行修改");	
				}
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			}
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/goods/remove", method = RequestMethod.POST)
	public ResultModel remove(HttpServletRequest request,@PathVariable("version") Double version, @RequestBody GoodsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try{
				GoodsEntity result = goodsBackService.checkRecordForDel(entity);
				if (result == null) {
					goodsBackService.remove(entity);
					return new ResultModel(true, "");
				} else {
					return new ResultModel(false, "该商品下的明细状态非初始化，无法进行删除");	
				}
			}catch(Exception e){
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
	public ResultModel queryById(@PathVariable("version") Double version, @RequestBody GoodsRebateEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {			
			try{
				GoodsRebateEntity result = goodsBackService.queryById(entity);
				return new ResultModel(true, result);
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsRebate/updateRebate", method = RequestMethod.POST)
	public ResultModel updateRebate(@PathVariable("version") Double version, @RequestBody GoodsRebateEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {			
			try{
				GoodsRebateEntity result = goodsBackService.checkRecordForRebate(entity);
				if (result == null) {
					goodsBackService.insertGoodsRebate(entity);
				} else {
					goodsBackService.updateGoodsRebate(entity);
				}
				return new ResultModel(true, "");
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/queryForPage", method = RequestMethod.POST)
	public ResultModel goodsTagQueryForPage(@PathVariable("version") Double version, @RequestBody ERPGoodsTagEntity entity) {

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
			try{
				goodsBackService.insertGoodsTag(entity);
				return new ResultModel(true, "");
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/modifyTag", method = RequestMethod.POST)
	public ResultModel modifyTag(@PathVariable("version") Double version, @RequestBody ERPGoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {			
			try{
				goodsBackService.updateGoodsTag(entity);
				return new ResultModel(true, "");
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/removeTag", method = RequestMethod.POST)
	public ResultModel removeTag(@PathVariable("version") Double version, @RequestBody ERPGoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {			
			try{
				goodsBackService.deleteGoodsTag(entity);
				return new ResultModel(true, "");
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/tagFunc", method = RequestMethod.POST)
	public ResultModel tagFunc(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {			
			try{
				List<TagFuncEntity> result = goodsBackService.queryTagFuncList();
				return new ResultModel(true, result);
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			
			}
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/goods/saveGoodsInfo", method = RequestMethod.POST)
	public ResultModel saveGoodsInfo(HttpServletRequest request,@PathVariable("version") Double version, @RequestBody GoodsInfoEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try{
				goodsBackService.saveGoodsInfo(entity);
				return new ResultModel(true, "");
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			}
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
}
