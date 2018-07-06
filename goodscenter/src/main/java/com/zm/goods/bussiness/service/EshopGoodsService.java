package com.zm.goods.bussiness.service;

import com.zm.goods.pojo.EshopGoodsEntity;
import com.zm.goods.pojo.EshopGoodsInventoryEntity;
import com.zm.goods.pojo.OrderInfoDTO;
import com.zm.goods.pojo.ResultModel;

public interface EshopGoodsService {

	ResultModel createPurchaseInfo(OrderInfoDTO info);
	
	void updateGoodsInfoForEshop(EshopGoodsEntity goods);
	
	ResultModel queryGoodsInfoForEshop(EshopGoodsEntity goods);
	
	void inventoryGoodsStockForEshop(EshopGoodsInventoryEntity goodsInventory);
	
	ResultModel queryGoodsInventoryInfoForEshop(EshopGoodsInventoryEntity goodsInventory);

	ResultModel createSellOrderGoodsInfo(OrderInfoDTO info);

	ResultModel checkSellOrderGoodsStock(OrderInfoDTO info);
}