package com.zm.goods.seo.service;

import java.util.List;

import com.zm.goods.pojo.bo.ItemStockBO;

public interface SEOService {

	List<ItemStockBO> getGoodsStock(String goodsId);
}
