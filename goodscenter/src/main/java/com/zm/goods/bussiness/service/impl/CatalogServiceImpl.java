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
import org.springframework.transaction.annotation.Transactional;

import com.zm.goods.bussiness.dao.CatalogMapper;
import com.zm.goods.bussiness.dao.GoodsBaseMapper;
import com.zm.goods.bussiness.service.CatalogService;
import com.zm.goods.pojo.CategoryTypeEnum;
import com.zm.goods.pojo.FirstCatalogEntity;
import com.zm.goods.pojo.GoodsBaseEntity;
import com.zm.goods.pojo.SecondCatalogEntity;
import com.zm.goods.pojo.ThirdCatalogEntity;

/**
 * ClassName: BrandService <br/>
 * Function: 分类服务. <br/>
 * date: Nov 9, 2017 8:37:14 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
public class CatalogServiceImpl implements CatalogService {

	@Resource
	CatalogMapper catalogMapper;

	@Resource
	GoodsBaseMapper goodsBaseMapper;

	@Override
	public List<FirstCatalogEntity> queryAll() {
		return catalogMapper.selectAll();
	}

	@Override
	public void saveSecondCatalog(SecondCatalogEntity entity) {
		catalogMapper.insertSecondCatalog(entity);
	}

	@Override
	public void saveFirstCatalog(FirstCatalogEntity entity) {
		catalogMapper.insertFristCatalog(entity);
	}

	@Override
	public void saveThirdCatalog(ThirdCatalogEntity entity) {
		catalogMapper.insertThirdCatalog(entity);
	}

	@Override
	public List<FirstCatalogEntity> queryFirstAll() {
		return catalogMapper.selectFirstAll();
	}

	@Override
	public List<SecondCatalogEntity> querySecondByFirstId(String firstId) {
		return catalogMapper.selectSecondByFirstId(firstId);
	}

	@Override
	public List<ThirdCatalogEntity> queryThirdBySecondId(String secondId) {
		return catalogMapper.selectThirdBySecondId(secondId);
	}

	@Override
	@Transactional
	public void delete(String id, String type) throws Exception {
		GoodsBaseEntity entity = new GoodsBaseEntity();
		if (CategoryTypeEnum.FIRST.getType().equals(type)) {
			entity.setFirstCatalogId(id);
		} else if (CategoryTypeEnum.SECOND.getType().equals(type)) {
			entity.setSecondCatalogId(id);
		} else if (CategoryTypeEnum.THIRD.getType().equals(type)) {
			entity.setThirdCatalogId(id);
		}

		GoodsBaseEntity goodsBaseEntity = goodsBaseMapper.selectForExists(entity);

		if (goodsBaseEntity != null) {
			throw new Exception("已绑定商品,无法删除");
		}
		
		if (CategoryTypeEnum.FIRST.getType().equals(type)) {
			catalogMapper.deleteByFristId(id);
		} else if (CategoryTypeEnum.SECOND.getType().equals(type)) {
			catalogMapper.deleteBySecondId(id);
		} else if (CategoryTypeEnum.THIRD.getType().equals(type)) {
			catalogMapper.deleteByThirdId(id);
		}

	}

	@Override
	public void modifyFirstCatalog(FirstCatalogEntity entity) {
		catalogMapper.updateFirstCatalog(entity);
	}

	@Override
	public void modifySecondCatalog(SecondCatalogEntity entity) {
		catalogMapper.updateSecondCatalog(entity);
	}

	@Override
	public void modifyThirdCatalog(ThirdCatalogEntity entity) {
		catalogMapper.updateThirdCatalog(entity);
	}

}
