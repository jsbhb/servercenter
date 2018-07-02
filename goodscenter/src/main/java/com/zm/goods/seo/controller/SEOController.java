package com.zm.goods.seo.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.seo.service.SEOService;

/**
 * @fun 用于提供前端服务器需求数据
 * @author user
 *
 */
@RestController
public class SEOController {

	@Resource
	SEOService seoService;

	/**
	 * @fun 获取item的stock
	 * @param version
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "auth/{version}/goods/stock/{centerId}/{goodsId}", method = RequestMethod.GET)
	public ResultModel getGoodsStock(@PathVariable("version") Double version,
			@PathVariable("goodsId") String goodsId, @PathVariable("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return new ResultModel(true, seoService.getGoodsStock(goodsId, centerId));
		}

		return null;
	}

	/**
	 * @fun 发布导航
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/navigation/publish", method = RequestMethod.POST)
	public ResultModel navPublish(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.navPublish();
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 发布首页
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/index/publish/{id}", method = RequestMethod.POST)
	public ResultModel indexPublish(@PathVariable("version") Double version, @PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.indexPublish(id);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 发布商品
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/publish/{centerId}", method = RequestMethod.POST)
	public ResultModel goodsPublish(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@RequestBody List<String> itemIdList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.publish(itemIdList, centerId);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 删除商品
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/publish/del", method = RequestMethod.POST)
	public ResultModel goodsPublishDel(@PathVariable("version") Double version, @RequestBody List<String> itemIdList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.delPublish(itemIdList);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 获取商品路径
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "auth/{version}/goods/access-path", method = RequestMethod.GET)
	public ResultModel getGoodsAccessPath(@PathVariable("version") Double version,
			@RequestParam(value = "goodsId", required = false) String goodsId,
			@RequestParam(value = "itemId", required = false) String itemId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.getGoodsAccessPath(goodsId, itemId);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
