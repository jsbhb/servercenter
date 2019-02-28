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

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.dao.GoodsPropertyMapper;
import com.zm.goods.bussiness.service.GoodsPropertyService;
import com.zm.goods.pojo.GoodsPropertyBindModel;
import com.zm.goods.pojo.GuidePropertyEntity;
import com.zm.goods.pojo.GuidePropertyValueEntity;
import com.zm.goods.pojo.PropertyEntity;
import com.zm.goods.pojo.PropertyValueEntity;

/**
 * ClassName: GoodsPropertyService <br/>
 * Function: 商品属性服务. <br/>
 * date: Nov 9, 2017 8:37:14 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
public class GoodsPropertyServiceImpl implements GoodsPropertyService {
	
	@Resource
	GoodsPropertyMapper goodsPropertyMapper;

	@Override
	public Page<PropertyEntity> queryByPage(PropertyEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsPropertyMapper.selectForPage(entity);
	}

	@Override
	public Page<GuidePropertyEntity> queryGuidePropertyByPage(PropertyEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsPropertyMapper.selectGuidePropertyForPage(entity);
	}

	@Override
	public void addGoodsPropertyName(PropertyEntity entity, String hidTabId) throws Exception {
		if (hidTabId.equals("property")) {
			goodsPropertyMapper.insertPropertyName(entity);
		} else {
			goodsPropertyMapper.insertGuidePropertyName(entity);
		}
	}
	
	@Override
	public PropertyEntity queryName(PropertyEntity entity) {
		return goodsPropertyMapper.selectName(entity);
	}

	@Override
	public GuidePropertyEntity queryGuideName(PropertyEntity entity) {
		return goodsPropertyMapper.selectGuideName(entity);
	}

	@Override
	public void editGoodsPropertyName(PropertyEntity entity, String hidTabId) throws Exception {
		if (hidTabId.equals("property")) {
			goodsPropertyMapper.updatePropertyName(entity);
		} else {
			goodsPropertyMapper.updateGuidePropertyName(entity);
		}
	}

	@Override
	public void removeGoodsPropertyName(String id, String hidTabId) throws Exception {
		if (hidTabId.equals("property")) {
			List<PropertyValueEntity> list = goodsPropertyMapper.checkPropertyNameToDelete(id);
			if (list == null || list.size() <= 0) {
				goodsPropertyMapper.deletePropertyName(id);
			} else {
				new Exception("该属性名已绑定属性值，请先删除属性值");
			}
		} else {
			List<GuidePropertyValueEntity> list = goodsPropertyMapper.checkGuidePropertyNameToDelete(id);
			if (list == null || list.size() <= 0) {
				goodsPropertyMapper.deleteGuidePropertyName(id);
			} else {
				new Exception("该属性名已绑定属性值，请先删除属性值");
			}
		}
	}

	@Override
	public void addGoodsPropertyValue(PropertyValueEntity entity, String hidTabId) throws Exception {
		if (hidTabId.equals("property")) {
			goodsPropertyMapper.insertPropertyValue(entity);
		} else {
			goodsPropertyMapper.insertGuidePropertyValue(entity);
		}
	}
	
	@Override
	public PropertyEntity queryValue(PropertyValueEntity entity) {
		return goodsPropertyMapper.selectValue(entity);
	}

	@Override
	public GuidePropertyEntity queryGuideValue(PropertyValueEntity entity) {
		return goodsPropertyMapper.selectGuideValue(entity);
	}

	@Override
	public void editGoodsPropertyValue(PropertyValueEntity entity, String hidTabId) throws Exception {
		if (hidTabId.equals("property")) {
			goodsPropertyMapper.updatePropertyValue(entity);
		} else {
			goodsPropertyMapper.updateGuidePropertyValue(entity);
		}
	}

	@Override
	public void removeGoodsPropertyValue(String id, String hidTabId) throws Exception {
		if (hidTabId.equals("property")) {
			List<GoodsPropertyBindModel> list = goodsPropertyMapper.checkPropertyValueToDelete(id);
			if (list == null || list.size() <= 0) {
				goodsPropertyMapper.deletePropertyValue(id);
			} else {
				new Exception("该属性值已绑定商品，请先取消商品绑定关系");
			}
		} else {
			goodsPropertyMapper.deleteGuidePropertyValue(id);
			goodsPropertyMapper.deleteGuidePropertyValueBindGoodsList(id);
		}
	}
}
