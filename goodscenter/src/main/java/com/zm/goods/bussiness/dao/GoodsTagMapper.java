package com.zm.goods.bussiness.dao;

import java.util.List;

import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.GoodsTagBindEntity;
import com.zm.goods.pojo.GoodsTagEntity;

public interface GoodsTagMapper {

	List<GoodsTagEntity> listGoodsTagByGoodsId(List<String> list);
	
	/**
	 * @fun获取卡单的所有itemid
	 * @return
	 */
	List<String> listCutOrderItemIds();

	void batchInsert(List<GoodsTagBindEntity> list);

	List<GoodsEntity> listGoodsIdByItemList(List<String> list);

	List<GoodsTagBindEntity> listGoodsTagBindByItemList(List<String> list);

	List<GoodsItemEntity> listGoodsTagByItemId(List<String> list);

	List<GoodsTagEntity> listTage();
}
