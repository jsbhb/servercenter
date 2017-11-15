/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service.impl  
 * Date:Nov 9, 20178:37:14 PM  
 *  
 */
package com.zm.goods.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.dao.GoodsBaseMapper;
import com.zm.goods.bussiness.service.GoodsBaseService;
import com.zm.goods.pojo.GoodsBaseEntity;

/**
 * ClassName: GoodsBaseServiceImpl <br/>
 * Function: 基础商品服务实现类. <br/>
 * date: Nov 9, 2017 8:37:14 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
public class GoodsBaseServiceImpl implements GoodsBaseService {

	@Resource
	GoodsBaseMapper goodsBaseMapper;
	
	@Override
	public Page<GoodsBaseEntity> queryByPage(GoodsBaseEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBaseMapper.selectForPage(entity);
	}

	@Override
	public GoodsBaseEntity queryById(int id) {
		return goodsBaseMapper.selectById(id);
	}

	@Override
	public void saveEntity(GoodsBaseEntity entity) {
		goodsBaseMapper.insert(entity);
	}

}
