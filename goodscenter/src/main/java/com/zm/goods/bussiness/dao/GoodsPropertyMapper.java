/**  
 * Project Name:goodscenter  
 * File Name:BrandMapper.java  
 * Package Name:com.zm.goods.bussiness.dao  
 * Date:Nov 9, 20178:48:41 PM  
 *  
 */
package com.zm.goods.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GoodsPropertyBindModel;
import com.zm.goods.pojo.GuidePropertyEntity;
import com.zm.goods.pojo.GuidePropertyValueEntity;
import com.zm.goods.pojo.PropertyEntity;
import com.zm.goods.pojo.PropertyValueEntity;
import com.zm.goods.pojo.po.GuidePropertyBindGoods;

/**
 * ClassName: GoodsPropertyMapper <br/>
 * Function: 商品属性. <br/>
 * date: Nov 9, 2017 8:48:41 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface GoodsPropertyMapper {

	/**
	 * selectForPage:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<PropertyEntity> selectForPage(PropertyEntity entity);
	
	Page<GuidePropertyEntity> selectGuidePropertyForPage(PropertyEntity entity);
	
	void insertPropertyName(PropertyEntity entity);
	
	void insertGuidePropertyName(PropertyEntity entity);
	
	PropertyEntity selectName(PropertyEntity entity);
	
	GuidePropertyEntity selectGuideName(PropertyEntity entity);
	
	void updatePropertyName(PropertyEntity entity);
	
	void updateGuidePropertyName(PropertyEntity entity);
	
	List<PropertyValueEntity> checkPropertyNameToDelete(String id);
	
	List<GuidePropertyValueEntity> checkGuidePropertyNameToDelete(String id);
	
	void deletePropertyName(String id);
	
	void deleteGuidePropertyName(String id);
	
	void insertPropertyValue(PropertyValueEntity entity);
	
	void insertGuidePropertyValue(PropertyValueEntity entity);
	
	PropertyEntity selectValue(PropertyValueEntity entity);
	
	GuidePropertyEntity selectGuideValue(PropertyValueEntity entity);
	
	void updatePropertyValue(PropertyValueEntity entity);
	
	void updateGuidePropertyValue(PropertyValueEntity entity);
	
	List<GoodsPropertyBindModel> checkPropertyValueToDelete(String id);
	
	void deletePropertyValue(String id);
	
	void deleteGuidePropertyValue(String id);
	
	void deleteGuidePropertyValueBindGoodsList(String id);
	/**
	 * @fun 根据三级分类ID获取绑定的系列属性名
	 * @param thirdCategory
	 * @return
	 */
	List<PropertyEntity> listSpecsPropertyNameByThirdCategory(String thirdCategory);
	/**
	 * @fun 根据系列属性名ID获取属性值
	 * @param nameId
	 * @return
	 */
	List<PropertyValueEntity> listSpecsPropertyValueByNameId(String nameId);
	/**
	 * @fun 查询所有系列属性名
	 * @return
	 */
	List<PropertyEntity> listAllSpecsPropertyName();
	/**
	 * @fun 根据三级分类ID获取绑定的导购属性名
	 * @param thirdCategory
	 * @return
	 */
	List<GuidePropertyEntity> listGuidePropertyNameByThirdCategory(String thirdCategory);
	/**
	 * @fun 根据系列属性名ID获取属性值
	 * @param nameId
	 * @return
	 */
	List<GuidePropertyValueEntity> listGuidePropertyValueByNameId(String nameId);
	/**
	 * @fun 查询所有导购属性名
	 * @return
	 */
	List<GuidePropertyEntity> listAllGuidePropertyName();
	/**
	 * @fun 批量插入商品导购属性绑定表
	 * @param bindList
	 */
	void insertIntoGoodsGuidePropertyBindBatch(List<GuidePropertyBindGoods> bindList);
}
