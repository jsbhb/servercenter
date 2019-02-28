package com.zm.goods.bussiness.dao;

import java.util.List;

import com.zm.goods.pojo.GoodsTagBindEntity;
import com.zm.goods.pojo.GoodsTagEntity;

public interface GoodsTagMapper {

	List<GoodsTagEntity> listGoodsTagBySpecsTpIds(List<String> list);
	
	/**
	 * @fun获取卡单的所有itemid
	 * @return
	 */
	List<String> listCutOrderItemIds();

	void batchInsert(List<GoodsTagBindEntity> list);

	List<GoodsTagBindEntity> listGoodsTagBindBySpecsTpIdList(List<String> list);
	/**
	 * @fun 批量解除绑定
	 * @param idList
	 */
	void deleteGoodsTagBindByIds(List<Integer> idList);
	/**
	 * @fun 根据主键Id获取tag实体
	 * @param tagIdList
	 * @return
	 */
	List<GoodsTagEntity> listGoodsTagByTagIds(List<Integer> tagIdList);

}
