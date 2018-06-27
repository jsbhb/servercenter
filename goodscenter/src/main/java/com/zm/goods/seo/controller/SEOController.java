package com.zm.goods.seo.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.bo.ItemStockBO;
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
	@RequestMapping(value = "{version}/goods/stock/{goodsId}", method = RequestMethod.GET)
	public List<ItemStockBO> getGoodsStock(@PathVariable("version") Double version,
			@PathVariable("goodsId") String goodsId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return seoService.getGoodsStock(goodsId);
		}
		
		return null;
	}
}
