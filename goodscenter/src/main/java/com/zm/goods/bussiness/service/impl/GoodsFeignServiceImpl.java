package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.service.GoodsFeignService;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.GoodsItemBO;

@Service
public class GoodsFeignServiceImpl implements GoodsFeignService {

	@Resource
	GoodsItemMapper goodsItemMapper;

	@Override
	public ResultModel manualOrderGoodsCheck(Set<GoodsItemBO> set) {
		if (set == null || set.size() == 0) {
			return new ResultModel(false, "没有商品信息");
		}
		List<String> itemIds = new ArrayList<String>();
		for (GoodsItemBO model : set) {
			itemIds.add(model.getItemId());
		}
		List<GoodsItemEntity> list = goodsItemMapper.listGoodsItemByItemIds(itemIds);
		if (list == null || list.size() == 0) {
			return new ResultModel(false, "导入的订单商品在系统中没有，请先在系统完善商品");
		}
		Map<String, GoodsItemEntity> tempMap = new HashMap<String, GoodsItemEntity>();
		for (GoodsItemEntity model : list) {
			tempMap.put(model.getItemId(), model);
		}
		GoodsItemEntity entity = null;
		StringBuilder sb = new StringBuilder();
		boolean success = true;
		for (GoodsItemBO model : set) {
			entity = tempMap.get(model.getItemId());
			if (entity == null) {
				sb.append("商品编号：" + model.getItemId() + ",在系统中不存在，请先新增后修改itemId!!!");
				success = false;
				continue;
			}
			if (!entity.getItemCode().equals(model.getItemCode()) || !entity.getSku().equals(model.getItemCode())) {
				sb.append("商品编号：" + model.getItemId() + ",商家编码或货号和系统内不一致，请核对!!!");
				success = false;
				continue;
			}
		}
		return new ResultModel(success, sb.toString());
	}

}
