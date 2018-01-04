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
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.service.GoodsItemService;
import com.zm.goods.pojo.GoodsItemEntity;
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
	GoodsItemMapper goodsItemMapper;

	@Override
	public Page<GoodsItemEntity> queryByPage(GoodsItemEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsItemMapper.selectForPage(entity);
	}

	@Override
	public GoodsItemEntity queryById(int id) {
		GoodsItemEntity entity = goodsItemMapper.selectById(id);
		return entity;
	}

	@Override
	@Transactional
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
	@Transactional
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
		return goodsItemMapper.queryCenterByPage(entity, centerId);
	}

}
