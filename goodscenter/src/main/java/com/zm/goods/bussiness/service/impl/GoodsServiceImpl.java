package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.utils.CommonUtils;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	private static final Integer PICTURE_TYPE = 0;

	private static final Integer COOK_BOOK_TYPE = 1;

	@Resource
	GoodsMapper goodsMapper;

	@Override
	public List<GoodsItem> listGoods(Map<String, Object> param) {

		List<GoodsItem> goodsList = goodsMapper.listGoods(param);

		List<Integer> idList = new ArrayList<Integer>();

		for (GoodsItem item : goodsList) {
			idList.add(item.getGoodsId());
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);
		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);
		List<GoodsSpecs> specsList = null;
		if (param.get("goodsId") != null) {
			specsList = goodsMapper.listGoodsSpecs(idList);
		}

		packageGoodsItem(goodsList, fileList, specsList);

		return goodsList;
	}

	@Override
	public List<PriceContrast> listPriceContrast(Map<String, Object> param) {

		return goodsMapper.listPriceContrast(param);
	}

	@Override
	public List<GoodsFile> listGoodsFile(Integer goodsId) {

		List<Integer> idList = new ArrayList<Integer>();
		idList.add(goodsId);
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", COOK_BOOK_TYPE);

		return goodsMapper.listGoodsFile(parameter);
	}

	@Override
	public Map<String, Object> tradeGoodsDetail(String itemId) {
		GoodsSpecs specs = goodsMapper.getGoodsSpecs(itemId);
		getPriceInterval(specs);

		List<Integer> idList = new ArrayList<Integer>();
		idList.add(specs.getGoodsId());
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);

		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("specs", specs);
		result.put("pic", fileList);

		return result;
	}
	
	
	@Override
	public Map<String, Object> listGoodsSpecs(List<String> list) {
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecsByItemId(list);

		List<Integer> idList = new ArrayList<Integer>();
		for(GoodsSpecs model : specsList){
			idList.add(model.getGoodsId());
		}
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);

		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("specsList", specsList);
		result.put("pic", fileList);

		return result;
	}

	@Override
	public ResultModel getPriceAndDelStock(List<OrderBussinessModel> list, boolean delStock, boolean vip) {

		ResultModel result = new ResultModel();

		GoodsSpecs specs = null;
		Double totalAmount = 0.0;
		for (OrderBussinessModel model : list) {
			specs = goodsMapper.getGoodsSpecs(model.getItemId());
			for (GoodsPrice price : specs.getPriceList()) {
				boolean flag = model.getQuantity() >= price.getMin()
						&& (model.getQuantity() <= price.getMax() || price.getMax() == null);
				if (flag) {
					if (model.getDeliveryPlace() != null) {
						if (model.getDeliveryPlace().equals(price.getDeliveryPlace())) {
							totalAmount += model.getQuantity() * (vip ? price.getVipPrice() : price.getPrice());
						}
					} else {
						totalAmount += model.getQuantity() * (vip ? price.getVipPrice() : price.getPrice());
					}
				}
			}
		}

		if (delStock) {
			// TODO 用线程池对每个itemID加锁？
			synchronized (totalAmount) {
				for (OrderBussinessModel model : list) {

				}
			}
			result.setSuccess(false);
			result.setErrorMsg("库存不足");
			return result;
		}

		result.setSuccess(true);
		result.setObj(totalAmount);
		return result;
	}

	private void packageGoodsItem(List<GoodsItem> goodsList, List<GoodsFile> fileList, List<GoodsSpecs> specsList) {

		Map<Integer, GoodsItem> goodsMap = new HashMap<Integer, GoodsItem>();
		if (goodsList == null || goodsList.size() == 0) {
			return;
		}

		for (GoodsItem item : goodsList) {
			goodsMap.put(item.getGoodsId(), item);
		}

		GoodsItem item = null;
		List<GoodsFile> tempFileList = null;
		List<GoodsSpecs> tempSpecsList = null;
		if (fileList != null && fileList.size() > 0) {
			for (GoodsFile file : fileList) {
				item = goodsMap.get(file.getGoodsId());
				if (item == null) {
					continue;
				}
				if (item.getGoodsFileList() == null) {
					tempFileList = new ArrayList<GoodsFile>();
					tempFileList.add(file);
					item.setGoodsFileList(tempFileList);
				} else {
					item.getGoodsFileList().add(file);
				}
			}
		}

		if (specsList != null && specsList.size() > 0) {
			for (GoodsSpecs specs : specsList) {
				item = goodsMap.get(specs.getGoodsId());
				if (item == null) {
					continue;
				}
				if (item.getGoodsSpecsList() == null) {
					tempSpecsList = new ArrayList<GoodsSpecs>();
					tempSpecsList.add(specs);
					item.setGoodsSpecsList(tempSpecsList);
				} else {
					item.getGoodsSpecsList().add(specs);
				}
				getPriceInterval(specs);
			}
		}

	}

	private void getPriceInterval(GoodsSpecs specs) {
		List<GoodsPrice> priceList = specs.getPriceList();
		if (priceList == null) {
			return;
		}
		for (int i = 0; i < priceList.size(); i++) {
			if (i == 0) {
				specs.setMinPrice(priceList.get(i).getVipPrice());
				specs.setMaxPrice(priceList.get(i).getPrice());
			} else {
				specs.setMinPrice(CommonUtils.getMinDouble(specs.getMinPrice(), priceList.get(i).getVipPrice()));
				specs.setMaxPrice(CommonUtils.getMaxDouble(specs.getMaxPrice(), priceList.get(i).getPrice()));
			}
		}
	}

}
