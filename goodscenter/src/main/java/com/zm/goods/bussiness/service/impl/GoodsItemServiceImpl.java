/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service.impl  
 * Date:Nov 9, 20178:37:14 PM  
 *  
 */
package com.zm.goods.bussiness.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.component.ThreadPoolComponent;
import com.zm.goods.bussiness.dao.GoodsBackMapper;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.service.GoodsItemService;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.pojo.GoodsExtensionEntity;
import com.zm.goods.pojo.GoodsPriceRatioEntity;
import com.zm.goods.pojo.GoodsRatioPlatformEntity;
import com.zm.goods.pojo.ResultModel;

/**
 * ClassName: GoodsBackServiceImpl <br/>
 * Function: 品牌服务. <br/>
 * date: Nov 9, 2017 8:37:14 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
public class GoodsItemServiceImpl implements GoodsItemService {
	@Resource
	GoodsBackMapper goodsBackMapper;
	
	@Resource
	GoodsItemMapper goodsItemMapper;
	
	@Resource
	GoodsTagMapper goodsTagMapper;
	
	@Resource
	ThreadPoolComponent threadPoolComponent;
	
	@Resource
	GoodsService goodsService;

	@Override
	public GoodsExtensionEntity queryGoodsExtensionInfo(GoodsExtensionEntity entity) {
		return goodsItemMapper.selectGoodsExtensionInfo(entity);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void updateGoodsExtension(GoodsExtensionEntity entity) {
		goodsItemMapper.updateOrInsertGoodsExtension(entity);
	}

	@Override
	public Page<GoodsRatioPlatformEntity> queryGoodsRatioPlanformPage(GoodsRatioPlatformEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsItemMapper.selectGoodsRatioPlatformForPage(entity);
	}

	@Override
	public GoodsRatioPlatformEntity queryGoodsRatioPlanformInfo(GoodsRatioPlatformEntity entity) {
		return goodsItemMapper.selectGoodsRatioPlatformForEdit(entity);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void createGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity) {
		goodsItemMapper.insertGoodsRatioPlatformInfo(entity);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void updateGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity) {
		goodsItemMapper.updateGoodsRatioPlatformInfo(entity);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void syncGoodsPriceRatioInfo(List<GoodsPriceRatioEntity> list) {
		goodsItemMapper.syncGoodsPriceRatioInfo(list);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public ResultModel syncStockQtyNotEnoughItemList() {
		ResultModel result = new ResultModel();
		return result;
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public ResultModel syncStockQtyEnoughItemList() {
		ResultModel result = new ResultModel();
		return result;
	}
}
