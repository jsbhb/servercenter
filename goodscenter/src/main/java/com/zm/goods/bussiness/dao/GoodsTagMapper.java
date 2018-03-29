package com.zm.goods.bussiness.dao;

import java.util.List;

import com.zm.goods.pojo.GoodsTagEntity;

public interface GoodsTagMapper {

	List<GoodsTagEntity> listGoodsTagByGoodsId(List<String> list);
}
