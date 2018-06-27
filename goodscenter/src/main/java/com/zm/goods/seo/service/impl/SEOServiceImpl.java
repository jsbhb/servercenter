package com.zm.goods.seo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.pojo.bo.ItemStockBO;
import com.zm.goods.seo.service.SEOService;

@Service("seoService")
public class SEOServiceImpl implements SEOService {

	@Resource
	GoodsMapper goodsMapper;
	
	@Override
	public List<ItemStockBO> getGoodsStock(String goodsId) {
		List<String> itemIds = goodsMapper.listItemIdsByGoodsId(goodsId);
		if(itemIds != null && itemIds.size() > 0){
			return goodsMapper.listStockByItemIds(itemIds);
		}
		return null;
	}

}
