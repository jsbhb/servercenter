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
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.dao.GoodsBackMapper;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.service.GoodsBackService;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.ThirdWarehouseGoods;

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
public class GoodsBackServiceImpl implements GoodsBackService {

	@Resource
	GoodsBackMapper goodsBackMapper;

	@Resource
	GoodsItemMapper goodsItemMapper;

	@Override
	public Page<GoodsEntity> queryByPage(GoodsEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectForPage(entity);
	}

	@Override
	public GoodsEntity queryById(int id) {
		GoodsEntity entity = goodsBackMapper.selectById(id);
		List<GoodsFile> fileList = goodsBackMapper.selectGoodsFileByGoodsId(entity);
		entity.setFiles(fileList);
		return entity;
	}

	@Override
	@Transactional
	public void save(GoodsEntity entity, String type) {
		goodsBackMapper.insert(entity);
		goodsItemMapper.insert(entity.getGoodsItem());
		goodsItemMapper.insertPrice(entity.getGoodsItem().getGoodsPrice());
		if(entity.getFiles() != null && entity.getFiles().size() > 0){
			goodsItemMapper.insertFiles(entity.getFiles());
		}

		if ("sync".equals(type)) {
			goodsBackMapper.updateThirdStatus(entity.getThirdId());
		}
	}

	@Override
	public Page<ThirdWarehouseGoods> queryByPage(ThirdWarehouseGoods entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectThirdForPage(entity);
	}

	@Override
	public ThirdWarehouseGoods queryThird(ThirdWarehouseGoods entity) {
		return goodsBackMapper.selectThird(entity);
	}

	@Override
	@Transactional
	public void edit(GoodsEntity entity) {
		goodsBackMapper.update(entity);
		List<GoodsFile> isrFileList = new ArrayList<GoodsFile>();
		if(entity.getFiles() != null && entity.getFiles().size() > 0){
			for(GoodsFile gf : entity.getFiles())  {
				if (gf.getId() != null) {
					goodsItemMapper.updateFiles(gf);
				} else {
					isrFileList.add(gf);
				}
			}
		}
		goodsItemMapper.insertFiles(isrFileList);
	}

	@Override
	@Transactional
	public void remove(GoodsEntity entity) {
		goodsBackMapper.delete(entity);
		goodsItemMapper.delete(entity);
	}

	@Override
	public GoodsEntity checkRecordForDel(GoodsEntity entity) {
		GoodsEntity retEntity = goodsBackMapper.selectRecordForDel(entity);
		return retEntity;
	}

	@Override
	public void saveDetailPath(GoodsEntity entity) {
		goodsBackMapper.updateDetailPath(entity);
	}

	@Override
	public GoodsEntity checkRecordForUpd(GoodsEntity entity) {
		GoodsEntity retEntity = goodsBackMapper.selectRecordForUpd(entity);
		return retEntity;
	}
}
