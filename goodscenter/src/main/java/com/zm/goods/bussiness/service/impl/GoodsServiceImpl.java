package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.PriceContrast;

@Service
public class GoodsServiceImpl implements GoodsService {

	private static final Integer PICTURE_TYPE = 0;
	
	private static final Integer COOK_BOOK_TYPE = 1;
	
	@Resource
	GoodsMapper goodsMapper;
	
	@Override
	public List<GoodsItem> listBigTradeGoods(Map<String, Object> param) {
		
		List<GoodsItem> goodsList = goodsMapper.listBigTradeGoods(param);
		
		List<Integer> idList = new ArrayList<Integer>();
		
		for(GoodsItem item : goodsList){
			idList.add(item.getGoodsId());
		}
		Map<String,Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);
		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);
		
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(idList);
		
		packageGoodsItem(goodsList, fileList, specsList);
		
		return null;
	}
	
	@Override
	public List<PriceContrast> listPriceContrast(Map<String, Object> param) {
		
		return goodsMapper.listPriceContrast(param);
	}
	
	
	@Override
	public List<GoodsFile> listGoodsFile(Integer goodsId) {
		
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(goodsId);
		Map<String,Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", COOK_BOOK_TYPE);
		
		return goodsMapper.listGoodsFile(parameter);
	}
	
	@Override
	public Map<String, Object> tradeGoodsDetail(String itemId) {
		GoodsSpecs specs = goodsMapper.getGoodsSpecs(itemId);
		
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(specs.getGoodsId());
		Map<String,Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);
		
		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		result.put("specs", specs);
		result.put("pic", fileList);
		
		return result;
	}

	private void packageGoodsItem(List<GoodsItem> goodsList, List<GoodsFile> fileList, List<GoodsSpecs> specsList) {
		
		Map<Integer,GoodsItem> goodsMap = new HashMap<Integer, GoodsItem>();
		if(goodsList == null || goodsList.size() == 0){
			return;
		}
		
		for(GoodsItem item : goodsList){
			goodsMap.put(item.getGoodsId(), item);
		}
		
		GoodsItem item = null ;
		List<GoodsFile> tempFileList = null;
		List<GoodsSpecs> tempSpecsList = null;
		if(fileList == null || fileList.size() == 0){
			for(GoodsFile file : fileList){
				item = goodsMap.get(file.getGoodsId());
				if(item == null){
					continue;
				}
				if(item.getGoodsFileList() == null){
					tempFileList = new ArrayList<GoodsFile>();
					tempFileList.add(file);
					item.setGoodsFileList(tempFileList);
				} else {
					item.getGoodsFileList().add(file);
				}
			}
		}
		
		if(specsList == null || specsList.size() == 0){
			for(GoodsSpecs specs : specsList){
				item = goodsMap.get(specs.getGoodsId());
				if(item == null){
					continue;
				}
				if(item.getGoodsSpecsList() == null){
					tempSpecsList = new ArrayList<GoodsSpecs>();
					tempSpecsList.add(specs);
					item.setGoodsFileList(tempFileList);
				} else {
					item.getGoodsSpecsList().add(specs);
				}
			}
		}
		
	}

}
