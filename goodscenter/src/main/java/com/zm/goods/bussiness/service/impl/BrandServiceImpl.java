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
import com.zm.goods.bussiness.dao.BrandMapper;
import com.zm.goods.bussiness.service.BrandService;
import com.zm.goods.pojo.BrandEntity;

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
public class BrandServiceImpl implements BrandService {

	@Resource
	BrandMapper brandMapper;

	@Override
	public Page<BrandEntity> queryByPage(BrandEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return brandMapper.selectForPage(entity);
	}

	@Override
	public BrandEntity queryById(int id) {
		return brandMapper.selectById(id);
	}

	@Override
	public void saveBrand(BrandEntity entity) {
		brandMapper.insert(entity);
	}

	@Override
	public List<BrandEntity> queryAll() {
		return brandMapper.selectAll();
	}

	@Override
	public void delete(String brandId) throws Exception {
		List<BrandEntity> list = brandMapper.checkBrandJoinGoodsList(brandId);
		if (list != null && list.size() > 0) {
			throw new Exception("已绑定商品,无法删除");
		}
		brandMapper.delete(brandId);
	}

	@Override
	public void modify(BrandEntity entity) {
		brandMapper.update(entity);
	}

	@Override
	public void remove(String brandId) {
		brandMapper.delete(brandId);		
	}

}
