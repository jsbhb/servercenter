package com.zm.goods.bussiness.decorator.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.decorator.GoodsServiceDecoratorAbstract;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.GoodsTagEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.dto.ShopManage4GoodsDTO;
import com.zm.goods.pojo.vo.Goods4ShopManager;

@Service("goodsTagDecorator")
public class GoodsServiceTagDecorator extends GoodsServiceDecoratorAbstract {

	@Resource
	GoodsService goodsServiceImpl;

	@Resource
	GoodsTagMapper goodsTagMapper;

	private final String GOODS_LIST = "goodsList";

	@SuppressWarnings("unchecked")
	@Override
	public Object listGoods(Map<String, Object> param, Integer centerId, Integer userId, boolean proportion,
			boolean isApplet) {
		Object obj = goodsServiceImpl.listGoods(param, centerId, userId, proportion, isApplet);
		if (proportion) {
			Map<String, Object> result = (Map<String, Object>) obj;
			List<GoodsItem> goodsList = (List<GoodsItem>) result.get(GOODS_LIST);
			packTagList(goodsList);
			return result;
		} else {
			List<GoodsItem> goodsList = (List<GoodsItem>) obj;
			packTagList(goodsList);
			return goodsList;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList, Pagination pagination,
			int gradeId, boolean welfare) throws WrongPlatformSource {
		Map<String, Object> result = goodsServiceImpl.queryGoods(searchModel, sortList, pagination, gradeId, welfare);
		List<GoodsItem> goodsList = (List<GoodsItem>) result.get(GOODS_LIST);
		packTagList(goodsList);
		return result;
	}

	// 封装标签和商品
	private void packTagList(List<GoodsItem> goodsList) {
		if(goodsList != null){
			List<String> itemIdList = new ArrayList<>();
			for (GoodsItem item : goodsList) {
				if (item.getGoodsSpecsList() != null) {
					for (GoodsSpecs specs : item.getGoodsSpecsList()) {
						itemIdList.add(specs.getItemId());
					}
				}
			}
			Map<String, List<GoodsTagEntity>> map = addItemGoodsTag(itemIdList);
			for (GoodsItem item : goodsList) {
				if (item.getGoodsSpecsList() != null) {
					for (GoodsSpecs specs : item.getGoodsSpecsList()) {
						specs.setTagList(map.get(specs.getItemId()));
					}
				}
			}
		}
	}

	/**
	 * @fun 获取标签
	 * @param goodsList
	 */
	private Map<String, List<GoodsTagEntity>> addItemGoodsTag(List<String> itemIdList) {
		Map<String, List<GoodsTagEntity>> map = new HashMap<String, List<GoodsTagEntity>>();
		if (itemIdList != null && itemIdList.size() > 0) {
			List<GoodsTagEntity> list = goodsTagMapper.listGoodsTagByGoodsId(itemIdList);
			List<GoodsTagEntity> temp = null;
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
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> listGoodsSpecs(List<String> list, String source, int platformSource, int gradeId)
			throws WrongPlatformSource {
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

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel listGoodsItem4ShopManage(ShopManage4GoodsDTO smg) {
		ResultModel result = goodsServiceImpl.listGoodsItem4ShopManage(smg);
		List<Goods4ShopManager> gsmList = (List<Goods4ShopManager>) result.getObj();
		if(gsmList != null && gsmList.size() > 0){
			List<String> itemIdList = gsmList.stream().map(Goods4ShopManager::getItemId).collect(Collectors.toList());
			Map<String, List<GoodsTagEntity>> map = addItemGoodsTag(itemIdList);
			for (Goods4ShopManager item : gsmList) {
				item.setTagList(map.get(item.getItemId()));
			}
		}
		return result;
	}

}
