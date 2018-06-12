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
import com.zm.goods.bussiness.dao.SpecsMapper;
import com.zm.goods.bussiness.service.SpecsService;
import com.zm.goods.pojo.SpecsEntity;
import com.zm.goods.pojo.SpecsTemplateEntity;
import com.zm.goods.pojo.SpecsValueEntity;
import com.zm.goods.pojo.bo.GoodsSpecsBO;

/**
 * ClassName: BrandService <br/>
 * Function: 品牌服务. <br/>
 * date: Nov 9, 2017 8:37:14 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
public class SpecsServiceImpl implements SpecsService {

	@Resource
	SpecsMapper specsMapper;

	@Override
	public Page<SpecsTemplateEntity> queryByPage(SpecsTemplateEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return specsMapper.selectForPage(entity);
	}

	@Override
	public SpecsTemplateEntity queryById(int id) {
		return specsMapper.selectById(id);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void save(SpecsTemplateEntity entity) {
		specsMapper.insertTemplate(entity);

		List<SpecsEntity> specs = entity.getSpecs();
		for (SpecsEntity spec : specs) {
			spec.setTemplateId(entity.getId());
			specsMapper.insertSpce(spec);
		}

		for (SpecsEntity spec : specs) {
			List<SpecsValueEntity> values = spec.getValues();

			for (SpecsValueEntity value : values) {
				value.setSpecsId(spec.getId());
			}

			specsMapper.insertSpcesValue(spec.getValues());
		}
	}

	@Override
	public List<SpecsTemplateEntity> queryAll() {
		return specsMapper.selectAll();
	}

	@Override
	public void saveSpecs(SpecsEntity entity) {
		specsMapper.insertSpce(entity);

		for (SpecsValueEntity value : entity.getValues()) {
			value.setSpecsId(entity.getId());
		}
		specsMapper.insertSpcesValue(entity.getValues());
	}

	@Override
	public void saveValue(SpecsValueEntity value) {
		List<SpecsValueEntity> list = new ArrayList<SpecsValueEntity>();
		list.add(value);
		specsMapper.insertSpcesValue(list);
	}

	@Override
	public List<SpecsEntity> selectAllSpece() {
		return specsMapper.selectAllSpece();
	}

	@Override
	public List<SpecsValueEntity> selectAllSpeceValue() {
		return specsMapper.selectAllSpeceValue();
	}

	@Override
	public int addSpecs(GoodsSpecsBO entity) {
		specsMapper.addSpece(entity);
		return entity.getSpecsNameId();
	}

	@Override
	public int addSpecsValue(GoodsSpecsBO entity) {
		specsMapper.addSpecsValue(entity);
		return entity.getSpecsValueId();
	}

	@Override
	public List<SpecsEntity> selectAllSpeceInfo() {
		return specsMapper.selectAllSpeceInfo();
	}

}
