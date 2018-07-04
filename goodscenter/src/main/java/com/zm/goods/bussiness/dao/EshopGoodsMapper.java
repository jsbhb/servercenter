package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.goods.pojo.EshopGoodsEntity;
import com.zm.goods.pojo.EshopGoodsInventoryEntity;
import com.zm.goods.pojo.EshopGoodsStockEntity;
import com.zm.goods.pojo.OrderInfoDTO;

public interface EshopGoodsMapper {

	void createPurchaseInfo(OrderInfoDTO info);
	
	void insertGoodsOperationRecordForPurchase(OrderInfoDTO info);
	
	List<EshopGoodsEntity> getGoodsItemInfoByItemIds(List<String> itemIds);
	
	List<EshopGoodsEntity> getGoodsInfoByItemIds(List<String> goodsIds);
	
	void syncGoodsInfoForPurchase(List<EshopGoodsEntity> goodsList);
	
	void syncGoodsStockForPurchase(OrderInfoDTO info);
	
	void updateGoodsInfoForEshop(EshopGoodsEntity goods);
	
	List<EshopGoodsEntity> selectGoodsInfoForEshop(EshopGoodsEntity goods);
	
	void insertGoodsOperationRecordForInventory(EshopGoodsInventoryEntity goodsInventory);
	
	void syncGoodsStockForInventory(EshopGoodsInventoryEntity goodsInventory);
	
	List<EshopGoodsInventoryEntity> selectGoodsInventoryInfoForEshop(EshopGoodsInventoryEntity goodsInventory);
	
	void syncGoodsStockForSellOrder(List<EshopGoodsStockEntity> goodsStockList);
	
	void insertGoodsOperationRecordForSellOrder(Map<String,Object> param);
}
