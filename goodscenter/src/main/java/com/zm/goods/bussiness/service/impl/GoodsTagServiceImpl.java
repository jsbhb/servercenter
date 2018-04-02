package com.zm.goods.bussiness.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.service.GoodsTagService;

@Service
public class GoodsTagServiceImpl implements GoodsTagService{

	@Resource
	GoodsTagMapper goodsTagMapper;
	
	@Override
	public List<String> listPreSellItemIds() {
		
		return goodsTagMapper.listCutOrderItemIds();
	}

}
