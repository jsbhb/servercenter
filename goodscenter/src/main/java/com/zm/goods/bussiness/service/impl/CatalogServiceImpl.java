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

import com.zm.goods.bussiness.dao.CatalogMapper;
import com.zm.goods.bussiness.service.CatalogService;
import com.zm.goods.pojo.FirstCatalogEntity;
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

}
