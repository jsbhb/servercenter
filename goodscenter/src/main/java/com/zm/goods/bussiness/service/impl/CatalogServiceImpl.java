/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service.impl  
 * Date:Nov 9, 20178:37:14 PM  
 *  
 */
package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.dao.CatalogMapper;
import com.zm.goods.bussiness.service.CatalogService;
import com.zm.goods.enummodel.CategoryTypeEnum;
import com.zm.goods.pojo.CategoryPropertyBindModel;
import com.zm.goods.pojo.FirstCatalogEntity;
import com.zm.goods.pojo.PropertyEntity;
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

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void delete(String id, String type) throws Exception {
		int i = 0;
		if (CategoryTypeEnum.FIRST.getType().equals(type)) {
			i = catalogMapper.countFirstCategory(id);
		} else if (CategoryTypeEnum.SECOND.getType().equals(type)) {
			i = catalogMapper.countSecondCategory(id);
		} else if (CategoryTypeEnum.THIRD.getType().equals(type)) {
			i = catalogMapper.countThirdCategory(id);
		}

		if (i > 0) {
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

	@Override
	public List<SecondCatalogEntity> querySecondAll() {
		return catalogMapper.selectSecondAll();
	}

	@Override
	public List<ThirdCatalogEntity> queryThirdAll() {
		return catalogMapper.selectThirdAll();
	}

	@Override
	public void updateFirstByParam(FirstCatalogEntity entity) {
		catalogMapper.updateFirstCatalogByParam(entity);
	}

	@Override
	public void updateSecondByParam(SecondCatalogEntity entity) {
		catalogMapper.updateSecondCatalogByParam(entity);
	}

	@Override
	public void updateThirdByParam(ThirdCatalogEntity entity) {
		catalogMapper.updateThirdCatalogByParam(entity);
	}
	
	@Override
	public SecondCatalogEntity queryFirstBySecond(SecondCatalogEntity entity) {
		return catalogMapper.selectFirstBySecond(entity);
	}
	
	@Override
	public FirstCatalogEntity queryCatalogInfoByParams(String id,String catalog) {
		return catalogMapper.selectCatalogInfoByParams(id,catalog);
	}
	
	@Override
	public Page<CategoryPropertyBindModel> queryJoinPropertyListForPage(CategoryPropertyBindModel entity,String propertyType) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return catalogMapper.selectJoinPropertyListForPage(entity,propertyType);
	}
	
	@Override
	public Page<PropertyEntity> queryAllPropertyListForPage(PropertyEntity entity, String propertyType) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return catalogMapper.selectAllPropertyListForPage(entity,propertyType);
	}
	
	@Override
	public void categoryJoinProperty(CategoryPropertyBindModel entity, String propertyType) throws Exception {
		List<CategoryPropertyBindModel> arrList = new ArrayList<CategoryPropertyBindModel>();
		FirstCatalogEntity first = catalogMapper.selectCatalogInfoByParams(entity.getCategoryId(),entity.getCategoryType()+"");
		if (first != null) {
			CategoryPropertyBindModel firstLevel = new CategoryPropertyBindModel();
			firstLevel.setPropertyId(entity.getPropertyId());
			firstLevel.setSort(1);
			firstLevel.setCategoryId(first.getFirstId());
			firstLevel.setCategoryType(1);
			firstLevel.setOpt(entity.getOpt());
			arrList.add(firstLevel);
			if (first.getSeconds() != null && first.getSeconds().size() > 0) {
				SecondCatalogEntity second = first.getSeconds().get(0);
				CategoryPropertyBindModel secondLevel = new CategoryPropertyBindModel();
				secondLevel.setPropertyId(entity.getPropertyId());
				secondLevel.setSort(1);
				secondLevel.setCategoryId(second.getSecondId());
				secondLevel.setCategoryType(2);
				secondLevel.setOpt(entity.getOpt());
				arrList.add(secondLevel);
				if (second.getThirds() != null && second.getThirds().size() > 0) {
					ThirdCatalogEntity third = second.getThirds().get(0);
					CategoryPropertyBindModel thirdLevel = new CategoryPropertyBindModel();
					thirdLevel.setPropertyId(entity.getPropertyId());
					thirdLevel.setSort(1);
					thirdLevel.setCategoryId(third.getThirdId());
					thirdLevel.setCategoryType(3);
					thirdLevel.setOpt(entity.getOpt());
					arrList.add(thirdLevel);
				}
			}
		}
		catalogMapper.insertCategoryJoinProperty(arrList,propertyType);
	}
	
	@Override
	public void categoryUnJoinProperty(String id, String propertyType) throws Exception {
		catalogMapper.removeCategoryJoinProperty(id,propertyType);
	}
	
	@Override
	public void modifyCategoryJoinProperty(String id, String sort, String propertyType) throws Exception {
		catalogMapper.updateCategoryJoinProperty(id,sort,propertyType);
	}
}
