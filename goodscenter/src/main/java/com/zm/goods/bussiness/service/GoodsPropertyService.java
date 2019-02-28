/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service  
 * Date:Nov 9, 20178:37:03 PM  
 *  
 */
package com.zm.goods.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GuidePropertyEntity;
import com.zm.goods.pojo.PropertyEntity;
import com.zm.goods.pojo.PropertyValueEntity;

/**
 * ClassName: GoodsPropertyService <br/>
 * Function: 商品属性服务. <br/>
 * date: Nov 9, 2017 8:37:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface GoodsPropertyService {

	/**
	 * queryByPage:分页查询商品属性信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<PropertyEntity> queryByPage(PropertyEntity entity);

	Page<GuidePropertyEntity> queryGuidePropertyByPage(PropertyEntity entity);
	
	void addGoodsPropertyName(PropertyEntity entity, String hidTabId) throws Exception;
	
	PropertyEntity queryName(PropertyEntity entity);
	
	GuidePropertyEntity queryGuideName(PropertyEntity entity);
	
	void editGoodsPropertyName(PropertyEntity entity, String hidTabId) throws Exception;
	
	void removeGoodsPropertyName(String id, String hidTabId) throws Exception;
	
	void addGoodsPropertyValue(PropertyValueEntity entity, String hidTabId) throws Exception;
	
	PropertyEntity queryValue(PropertyValueEntity entity);
	
	GuidePropertyEntity queryGuideValue(PropertyValueEntity entity);
	
	void editGoodsPropertyValue(PropertyValueEntity entity, String hidTabId) throws Exception;
	
	void removeGoodsPropertyValue(String id, String hidTabId) throws Exception;
}
