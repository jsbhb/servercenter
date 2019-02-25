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
			@RequestBody List<String> specsTpIdList) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.publishGoods(specsTpIdList);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 删除商品
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/publish/del/{centerId}", method = RequestMethod.POST)
	public ResultModel goodsPublishDel(@PathVariable("version") Double version, @RequestBody List<String> specsTpIdList,
			@PathVariable("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.delPublishGoods(specsTpIdList);
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
			@RequestParam(value = "specsTpId", required = false) String specsTpId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.getGoodsAccessPath(goodsId, specsTpId);
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun addsitemap
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/seo/sitemap", method = RequestMethod.POST)
	public ResultModel addSitemap(@PathVariable("version") Double version, @RequestBody List<String> domains) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.addSitemap(domains);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun delsitemap
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/seo/sitemap", method = RequestMethod.DELETE)
	public ResultModel delsitemap(@PathVariable("version") Double version, @RequestBody List<String> domains) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.delsitemap(domains);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
