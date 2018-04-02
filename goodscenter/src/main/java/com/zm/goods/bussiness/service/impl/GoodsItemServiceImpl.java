/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service.impl  
 * Date:Nov 9, 20178:37:14 PM  
 *  
 */
package com.zm.goods.bussiness.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.dao.GoodsBackMapper;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.service.GoodsItemService;
import com.zm.goods.pojo.ERPGoodsTagBindEntity;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.GoodsStatusEnum;

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

	@Override
	public Page<GoodsItemEntity> queryByPage(GoodsItemEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsItemMapper.selectForPage(entity);
	}

	@Override
	public GoodsItemEntity queryById(int id) {
		GoodsItemEntity entity = goodsItemMapper.selectById(id);
		GoodsEntity goodsEntity = new GoodsEntity();
		goodsEntity.setGoodsItem(entity);
		ERPGoodsTagBindEntity tagBind = goodsBackMapper.selectGoodsTagBindByGoodsId(goodsEntity);
		entity.setTagBindEntity(tagBind);
		return entity;
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void save(GoodsItemEntity entity) {
		goodsItemMapper.insert(entity);
		goodsItemMapper.insertPrice(entity.getGoodsPrice());
	}

	@Override
	public void notBeFx(GoodsItemEntity entity) {
		entity.setStatus(GoodsStatusEnum.USEFUL.getIndex()+"");
		goodsItemMapper.updateStatus(entity);
	}

	@Override
	public void beFx(GoodsItemEntity entity) {
		entity.setStatus(GoodsStatusEnum.FX.getIndex()+"");
		goodsItemMapper.updateStatus(entity);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void beUse(GoodsItemEntity entity) {
		entity.setStatus(GoodsStatusEnum.USEFUL.getIndex()+"");
		goodsItemMapper.updateStatus(entity);
		goodsItemMapper.insertStock(entity);
	}

	@Override
	public Page<GoodsItemEntity> queryCenterByPage(GoodsItemEntity entity, int centerId) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entity", entity);
		params.put("centerId", centerId);
		return goodsItemMapper.selectCenterForPage(params);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void update(GoodsItemEntity entity) {
		goodsItemMapper.update(entity);
		goodsItemMapper.updatePrice(entity.getGoodsPrice());
		goodsBackMapper.updateTagBind(entity.getTagBindEntity());
	}

	@Override
	public Page<GoodsItemEntity> queryPurchaseCenterByPage(GoodsItemEntity entity, int centerId) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entity", entity);
		if (centerId == -1) {
			params.put("centerId", "2B");
		} else {
			params.put("centerId", centerId);
		}
		return goodsItemMapper.selectPurchaseCenterForPage(params);
	}

	@Override
	public Page<GoodsItemEntity> queryPurchaseCenterItem(GoodsItemEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entity", entity);
		return goodsItemMapper.selectPurchaseCenterItem(params);
	}

	@Override
	public GoodsPrice queryPurchaseCenterItemForEdit(GoodsItemEntity entity) {
		return goodsItemMapper.selectPurchaseCenterItemForEdit(entity);
	}

	@Override
	public GoodsPrice queryItemPrice(String itemId) {
		return goodsItemMapper.selectItemPrice(itemId);
	}

	@Override
	public void updateItemPrice(GoodsPrice entity) {
		goodsItemMapper.updateItemPrice(entity);
	}

	@Override
	public Page<GoodsEntity> queryByPageDownload(GoodsItemEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsItemMapper.selectForPageDownload(entity);
	}

	@Override
	public Page<GoodsEntity> queryCenterByPageDownload(GoodsItemEntity entity, int centerId) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entity", entity);
		params.put("centerId", centerId);
		return goodsItemMapper.selectCenterForPageDownload(params);
	}
}
