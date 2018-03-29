package com.zm.goods.bussiness.decorator.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.decorator.GoodsServiceDecoratorAbstract;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;

@Service
public class GoodsServiceTagDecorator extends GoodsServiceDecoratorAbstract{

	@Resource
	GoodsService goodsServiceImpl;
	
	@Resource
	GoodsTagMapper goodsTagMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object listGoods(Map<String, Object> param, Integer centerId, Integer userId, boolean proportion) {
		Object obj = goodsServiceImpl.listGoods(param, centerId, userId, proportion);
		if(proportion){
			Map<String,Object> result = (Map<String, Object>) obj;
			List<GoodsItem> goodsList = (List<GoodsItem>) result.get("goodsList");
		} else {
			List<GoodsItem> goodsList = (List<GoodsItem>) obj;
		}
		return null;
	}

	@Override
	public Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList, Pagination pagination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> listGoodsSpecs(List<String> list, Integer centerId, String source) { 
		// TODO Auto-generated method stub
		return null;
	}

}
