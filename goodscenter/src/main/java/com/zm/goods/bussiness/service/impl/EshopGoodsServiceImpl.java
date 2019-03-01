package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.goods.bussiness.dao.EshopGoodsMapper;
import com.zm.goods.bussiness.service.EshopGoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.EshopGoodsEntity;
import com.zm.goods.pojo.EshopGoodsInventoryEntity;
import com.zm.goods.pojo.EshopGoodsStockEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.dto.OrderGoodsDTO;
import com.zm.goods.pojo.dto.OrderInfoDTO;

@Service("eshopGoodsService")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class EshopGoodsServiceImpl implements EshopGoodsService {

	@Resource
	EshopGoodsMapper eshopGoodsMapper;
	
	@Override
	public ResultModel createPurchaseInfo(OrderInfoDTO info) {
		ResultModel result = null;

		List<EshopGoodsEntity> eshopGoodsList = getGoodsInfo(info);
		if (eshopGoodsList.size() != info.getOrderGoodsList().size()) {
			result = new ResultModel(false, "商品种类信息匹配不一致，请确认！");
			return result;
		}
		eshopGoodsMapper.createPurchaseInfo(info);
		eshopGoodsMapper.insertGoodsOperationRecordForPurchase(info);
		eshopGoodsMapper.syncGoodsInfoForPurchase(eshopGoodsList);
		eshopGoodsMapper.syncGoodsStockForPurchase(info);
		
		result = new ResultModel(true, "");
		return result;
	}
	
	public List<EshopGoodsEntity> getGoodsInfo(OrderInfoDTO info) {
		List<String> itemIds = new ArrayList<String>();
		for(OrderGoodsDTO og:info.getOrderGoodsList()) {
			itemIds.add(og.getItemId());
		}
		List<EshopGoodsEntity> itemInfoList = eshopGoodsMapper.getGoodsItemInfoByItemIds(itemIds);
		List<String> goodsIds = new ArrayList<String>();
		for(EshopGoodsEntity eg:itemInfoList) {
			goodsIds.add(eg.getGoodsId());
		}
		List<EshopGoodsEntity> goodsInfoList = eshopGoodsMapper.getGoodsInfoByItemIds(goodsIds);
		for(EshopGoodsEntity ieg:itemInfoList) {
			ieg.setMallId(info.getMallId());
			ieg.setGradeId(info.getGradeId());
			ieg.setOpt(info.getOpt());
			ieg.setStatus("1");
			for(OrderGoodsDTO og:info.getOrderGoodsList()) {
				if (ieg.getItemId().equals(og.getItemId())) {
					ieg.setProxyPrice(og.getActualPrice());
					ieg.setRetailPrice(og.getActualPrice());
					break;
				}
			}
			for(EshopGoodsEntity geg:goodsInfoList) {
				if (ieg.getGoodsId().equals(geg.getGoodsId())) {
					ieg.setGoodsName(geg.getGoodsName());
					ieg.setOrigin(geg.getOrigin());
					ieg.setFirstCategory(geg.getFirstCategory());
					ieg.setBrand(geg.getBrand());
					ieg.setItemImg(geg.getItemImg());
					break;
				}
			}
		}
		return itemInfoList;
	}
	
	@Override
	public void updateGoodsInfoForEshop(EshopGoodsEntity goods) {
		eshopGoodsMapper.updateGoodsInfoForEshop(goods);
	}
	
	@Override
	public ResultModel queryGoodsInfoForEshop(EshopGoodsEntity goods) {
		ResultModel result = null;
		List<EshopGoodsEntity> eshopGoodsList = eshopGoodsMapper.selectGoodsInfoForEshop(goods);
		result = new ResultModel(true, eshopGoodsList);
		return result;
	}
	
	@Override
	public void inventoryGoodsStockForEshop(EshopGoodsInventoryEntity goodsInventory) {
		Integer diffQty = goodsInventory.getCheckQty() - goodsInventory.getSysQty();
		if (diffQty > 0) {
			goodsInventory.setOperationType(Constants.OPERATION_TYPE_INVENTORY_PROFIT);
		} else if (diffQty < 0) {
			goodsInventory.setOperationType(Constants.OPERATION_TYPE_INVENTORY_LOSSES);
		} else {
			return;
		}
		goodsInventory.setDiffQty(Math.abs(diffQty));
		eshopGoodsMapper.insertGoodsOperationRecordForInventory(goodsInventory);
		eshopGoodsMapper.syncGoodsStockForInventory(goodsInventory);
	}
	
	@Override
	public ResultModel queryGoodsInventoryInfoForEshop(EshopGoodsInventoryEntity goodsInventory) {
		ResultModel result = null;
		List<EshopGoodsInventoryEntity> inventoryList = eshopGoodsMapper.selectGoodsInventoryInfoForEshop(goodsInventory);
		result = new ResultModel(true, inventoryList);
		return result;
	}
	
	@Override
	public ResultModel createSellOrderGoodsInfo(OrderInfoDTO info) {
		ResultModel result = null;

		ResultModel checkResult = checkSellOrderGoodsStock(info);
		if (!checkResult.isSuccess()) {
			return checkResult;
		}
		
		List<EshopGoodsStockEntity> goodsStockList = getGoodsStockDeductingList(info);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", goodsStockList);
		param.put("orderId", info.getOrderId());
		
		eshopGoodsMapper.syncGoodsStockForSellOrder(goodsStockList);
		eshopGoodsMapper.insertGoodsOperationRecordForSellOrder(param);
		
		result = new ResultModel(true, "");
		return result;
	}
	
	public List<EshopGoodsEntity> getCheckGoodsStockList(OrderInfoDTO info) {
		List<EshopGoodsEntity> checkGoodsList = new ArrayList<EshopGoodsEntity>();
		List<String> itemIdList = new ArrayList<String>();
		for(OrderGoodsDTO og: info.getOrderGoodsList()) {
			itemIdList.add(og.getItemId());
		}
		EshopGoodsEntity goodsInfo = new EshopGoodsEntity();
		goodsInfo.setMallId(info.getMallId());
		goodsInfo.setGradeId(info.getGradeId());
		goodsInfo.setItemIdList(itemIdList);
		
		checkGoodsList = eshopGoodsMapper.selectGoodsInfoForEshop(goodsInfo);
		return checkGoodsList;
	}

	@Override
	public ResultModel checkSellOrderGoodsStock(OrderInfoDTO info) {
		ResultModel result = null;
		
		List<EshopGoodsEntity> checkGoodsList = getCheckGoodsStockList(info);
		if (info.getOrderGoodsList().size() != checkGoodsList.size()) {
			result = new ResultModel(false, "商品种类信息匹配不一致，请确认！");
			return result;
		}
		
		for(OrderGoodsDTO og: info.getOrderGoodsList()) {
			for(EshopGoodsEntity eg: checkGoodsList) {
				if (og.getItemId().equals(eg.getItemId())) {
					if (eg.getItemQuantity() < og.getItemQuantity()) {
						result = new ResultModel(false, "商品：" + eg.getItemId() + 
														"的当前库存：" + eg.getItemQuantity() + 
														"小于销售数量：" + og.getItemQuantity() + "请确认！");
						return result;
					} else {
						break;
					}
				}
			}
		}
		
		result = new ResultModel(true, "");
		return result;
	}
	
	public List<EshopGoodsStockEntity> getGoodsStockDeductingList(OrderInfoDTO info) {
		List<EshopGoodsStockEntity> retGoodsList = new ArrayList<EshopGoodsStockEntity>();
		List<EshopGoodsEntity> checkGoodsList = getCheckGoodsStockList(info);
		EshopGoodsStockEntity goodsStock = null;
		
		for(OrderGoodsDTO og: info.getOrderGoodsList()) {
			int tmpQuantity = og.getItemQuantity();
			for(EshopGoodsEntity eg: checkGoodsList) {
				if (og.getItemId().equals(eg.getItemId())) {
					for(EshopGoodsStockEntity egs: eg.getGoodsStockList()) {
						if (tmpQuantity != 0) {
							goodsStock = new EshopGoodsStockEntity();
							goodsStock.setMallId(info.getMallId());
							goodsStock.setGradeId(info.getGradeId());
							goodsStock.setItemId(eg.getItemId());
							goodsStock.setLoc(egs.getLoc());
							if (egs.getQuantity() - tmpQuantity >= 0) {
								goodsStock.setQuantity(tmpQuantity);
								tmpQuantity = 0;
							} else {
								goodsStock.setQuantity(egs.getQuantity());
								tmpQuantity = tmpQuantity - egs.getQuantity();
							}
							goodsStock.setOpt(info.getOpt());
							retGoodsList.add(goodsStock);
						} else {
							break;
						}
					}
					break;
				}
			}
		}
		
		return retGoodsList;
	}
}
