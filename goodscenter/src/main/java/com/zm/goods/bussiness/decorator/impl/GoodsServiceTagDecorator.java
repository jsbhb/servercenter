package com.zm.goods.bussiness.decorator.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.decorator.GoodsServiceDecoratorAbstract;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.GoodsTagEntity;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;

@Service("goodsTagDecorator")
public class GoodsServiceTagDecorator extends GoodsServiceDecoratorAbstract {

	@Resource
	GoodsService goodsServiceImpl;

	@Resource
	GoodsTagMapper goodsTagMapper;

	private final String GOODS_LIST = "goodsList";

	@SuppressWarnings("unchecked")
	@Override
	public Object listGoods(Map<String, Object> param, Integer centerId, Integer userId, boolean proportion) {
		Object obj = goodsServiceImpl.listGoods(param, centerId, userId, proportion);
		if (proportion) {
			Map<String, Object> result = (Map<String, Object>) obj;
			List<GoodsItem> goodsList = (List<GoodsItem>) result.get(GOODS_LIST);
			addItemGoodsTag(goodsList);
			return result;
		} else {
			List<GoodsItem> goodsList = (List<GoodsItem>) obj;
			addItemGoodsTag(goodsList);
			return goodsList;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList, Pagination pagination) {
		Map<String, Object> result = goodsServiceImpl.queryGoods(searchModel, sortList, pagination);
		List<GoodsItem> goodsList = (List<GoodsItem>) result.get(GOODS_LIST);
		addItemGoodsTag(goodsList);
		return result;
	}

	/**
	 * @fun 把标签和商品进行组合
	 * @param goodsList
	 */
	private void addItemGoodsTag(List<GoodsItem> goodsList) {
		List<String> itemIdList = new ArrayList<String>();
		if (goodsList != null && goodsList.size() > 0) {
			for (GoodsItem item : goodsList) {
				if (item.getGoodsSpecsList() != null) {
					for (GoodsSpecs specs : item.getGoodsSpecsList()) {
						itemIdList.add(specs.getItemId());
					}
				}
			}
			List<GoodsTagEntity> list = goodsTagMapper.listGoodsTagByGoodsId(itemIdList);
			List<GoodsTagEntity> temp = null;
			Map<String, List<GoodsTagEntity>> map = new HashMap<String, List<GoodsTagEntity>>();
			if (list != null && list.size() > 0) {
				for (GoodsTagEntity tag : list) {
					if (map.get(tag.getItemId()) == null) {
						temp = new ArrayList<GoodsTagEntity>();
						temp.add(tag);
						map.put(tag.getItemId(), temp);
					} else {
						map.get(tag.getItemId()).add(tag);
					}
				}
			}
			for (GoodsItem item : goodsList) {
				if (item.getGoodsSpecsList() != null) {
					for (GoodsSpecs specs : item.getGoodsSpecsList()) {
						specs.setTagList(map.get(specs.getItemId()));
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> listGoodsSpecs(List<String> list, String source, int platformSource, int gradeId) throws WrongPlatformSource {
		Map<String, Object> result = goodsServiceImpl.listGoodsSpecs(list, source, platformSource, gradeId);
		List<GoodsSpecs> specsList = (List<GoodsSpecs>) result.get("specsList");
		List<String> itemIdList = new ArrayList<String>();
		for (GoodsSpecs specs : specsList) {
			itemIdList.add(specs.getGoodsId());
		}
		List<GoodsTagEntity> tagList = goodsTagMapper.listGoodsTagByGoodsId(itemIdList);
		List<GoodsTagEntity> temp = null;
		Map<String, List<GoodsTagEntity>> map = new HashMap<String, List<GoodsTagEntity>>();
		if (tagList != null && tagList.size() > 0) {
			for (GoodsTagEntity tag : tagList) {
				if (map.get(tag.getItemId()) == null) {
					temp = new ArrayList<GoodsTagEntity>();
					temp.add(tag);
					map.put(tag.getItemId(), temp);
				} else {
					map.get(tag.getItemId()).add(tag);
				}
			}
			for (GoodsSpecs specs : specsList) {
				specs.setTagList(map.get(specs.getItemId()));
			}
		}
		return result;
	}

}
