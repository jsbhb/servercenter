package com.zm.goods.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.goods.bussiness.service.BrandService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.BrandEntity;
import com.zm.goods.pojo.ResultModel;

/**
 * ClassName: BrandController <br/>
 * Function: 品牌接口. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class BrandController {

	@Resource
	BrandService brandService;

	@RequestMapping(value = "{version}/goods/brand/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version, @RequestBody BrandEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<BrandEntity> page = brandService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/brand/save", method = RequestMethod.POST)
	public ResultModel save(@PathVariable("version") Double version, @RequestBody BrandEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try{
				brandService.saveBrand(entity);
				return new ResultModel(true, "");
			}catch(Exception e){
				return new ResultModel(false, e.getMessage());			}
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/brand/query", method = RequestMethod.POST)
	public ResultModel queryById(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody BrandEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				BrandEntity result = brandService.queryById(entity.getId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
}
