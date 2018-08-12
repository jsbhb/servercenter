/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service.impl  
 * Date:Nov 9, 20178:37:14 PM  
 *  
 */
package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.component.ThreadPoolComponent;
import com.zm.goods.bussiness.dao.GoodsBackMapper;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.service.GoodsItemService;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.GoodsStatusEnum;
import com.zm.goods.enummodel.PublishType;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.ERPGoodsTagBindEntity;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsExtensionEntity;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.GoodsPriceRatioEntity;
import com.zm.goods.pojo.GoodsRatioPlatformEntity;
import com.zm.goods.pojo.GoodsTagEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.seo.publish.PublishComponent;
import com.zm.goods.utils.JSONUtil;

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
public class GoodsItemServiceImpl implements GoodsItemService {
	@Resource
	GoodsBackMapper goodsBackMapper;
	
	@Resource
	GoodsItemMapper goodsItemMapper;
	
	@Resource
	GoodsTagMapper goodsTagMapper;
	
	@Resource
	ThreadPoolComponent threadPoolComponent;
	
	@Resource
	GoodsService goodsService;

	@Override
	public Page<GoodsItemEntity> queryByPage(GoodsItemEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Page<GoodsItemEntity> page = goodsItemMapper.selectForPage(entity);
		List<GoodsItemEntity> list = (List<GoodsItemEntity>) page;
		if (list.size() > 0) {
			List<String> ids = new ArrayList<String>();
			List<String> itemIds = new ArrayList<String>();
			for (GoodsItemEntity item : list) {
				ids.add(item.getGoodsId());
				itemIds.add(item.getItemId());
			}
			
			List<GoodsItemEntity> itemTagList = goodsTagMapper.listGoodsTagByItemId(itemIds);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("list", ids);
			List<GoodsFile> files = goodsBackMapper.selectGoodsFileByParam(param);
			for (GoodsItemEntity item : list) {
				for (GoodsFile file : files) {
					if (file.getGoodsId().equals(item.getGoodsId())) {
						List<GoodsFile> gfiles = new ArrayList<GoodsFile>();
						gfiles.add(file);
						item.getGoodsEntity().setFiles(gfiles);
						break;
					}
				}
				
				//默认设置空的标签列表
				item.setTagList(new ArrayList<GoodsTagEntity>());
				for(GoodsItemEntity gie:itemTagList) {
					if (item.getItemId().equals(gie.getItemId())) {
						item.setTagList(gie.getTagList());
						break;
					}
				}
			}
			page = (Page<GoodsItemEntity>) list;
		}
		return page;
	}

	@Override
	public GoodsItemEntity queryById(int id) {
		GoodsItemEntity entity = goodsItemMapper.selectById(id);
		ERPGoodsTagBindEntity tagBind = goodsBackMapper.selectGoodsTagBindByItemId(entity);
		entity.setTagBindEntity(tagBind);
		return entity;
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void save(GoodsItemEntity entity) {
		goodsItemMapper.insert(entity);
		goodsItemMapper.insertPrice(entity.getGoodsPrice());
		if (entity.getTagBindEntity() != null) {
			goodsBackMapper.insertTagBind(entity.getTagBindEntity());
		}
	}

	@Override
	public void notBeFx(GoodsItemEntity entity) {
		entity.setStatus(GoodsStatusEnum.NOTFX.getIndex()+"");
		goodsItemMapper.updateIsFXStatus(entity);
		List<String> itemIdList = new ArrayList<String>();
		itemIdList.add(entity.getItemId());
		//更新索引并发布
		updateLuceneAndPublish(itemIdList);
		threadPoolComponent.sendGoodsInfoDownShelves(itemIdList);//通知对接用户下架
	}

	private void updateLuceneAndPublish(List<String> itemIdList, PublishType publishType) {
		List<String> goodsIdList = new ArrayList<String>();
		Map<String,List<String>> publishMap = new HashMap<String,List<String>>();
		List<String> tempList = null;
		List<GoodsEntity> goodsList = goodsItemMapper.listGoodsIdByItemList(itemIdList);
		for(GoodsEntity temp : goodsList){
			goodsIdList.add(temp.getGoodsId());
			tempList = new ArrayList<String>();
			for(GoodsItemEntity itemEntity : temp.getItems()){
				tempList.add(itemEntity.getItemId());
			}
			publishMap.put(temp.getGoodsId(), tempList);
		}
		ResultModel result = PublishComponent.publish(JSONUtil.toJson(publishMap), publishType);//前端发布
		if(!result.isSuccess()){
			LogUtil.writeLog(publishType.getUrl() + "发布失败" + JSONUtil.toJson(publishMap));
		}
		goodsService.updateLuceneIndex(goodsIdList, Constants.CNCOOPBUY);//更新LUCENE
	}

	@Override
	public void beFx(GoodsItemEntity entity) {
		entity.setStatus(GoodsStatusEnum.FX.getIndex()+"");
		goodsItemMapper.updateIsFXStatus(entity);
		List<String> itemIdList = new ArrayList<String>();
		itemIdList.add(entity.getItemId());
		//更新索引并发布
		updateLuceneAndPublish(itemIdList);
		threadPoolComponent.sendGoodsInfo(itemIdList);//通知对接用户上架，可能修改了价格等信息
	}

	@Override
	public Page<GoodsItemEntity> queryCenterByPage(GoodsItemEntity entity, int centerId) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entity", entity);
		params.put("centerId", centerId);
		Page<GoodsItemEntity> page = goodsItemMapper.selectCenterForPage(params);
		List<GoodsItemEntity> list = (List<GoodsItemEntity>) page;
		if (list.size() > 0) {
			List<String> ids = new ArrayList<String>();
			for (GoodsItemEntity item : list) {
				ids.add(item.getGoodsId());
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("list", ids);
			List<GoodsFile> files = goodsBackMapper.selectGoodsFileByParam(param);
			for (GoodsItemEntity item : list) {
				for (GoodsFile file : files) {
					if (file.getGoodsId().equals(item.getGoodsId())) {
						List<GoodsFile> gfiles = new ArrayList<GoodsFile>();
						gfiles.add(file);
						item.getGoodsEntity().setFiles(gfiles);
						break;
					}
				}
			}
			page = (Page<GoodsItemEntity>) list;
		}
		return page;
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void update(GoodsItemEntity entity) {
		goodsItemMapper.update(entity);
		goodsItemMapper.updatePrice(entity.getGoodsPrice());
		//判断商品标签
		//增删改
		ERPGoodsTagBindEntity oldTag = goodsBackMapper.selectGoodsTagBindByItemId(entity);
		ERPGoodsTagBindEntity newTag = entity.getTagBindEntity();
		if (oldTag == null && newTag.getTagId() != 0) {
			goodsBackMapper.insertTagBind(newTag);
		} else if (oldTag != null && newTag.getTagId() == 0) {
			goodsBackMapper.deleteTagBind(newTag);
		} else if (oldTag != null && newTag.getTagId() != 0) {
			goodsBackMapper.updateTagBind(newTag);
		}
	}

	@Override
	public Page<GoodsItemEntity> queryPurchaseCenterItem(GoodsItemEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entity", entity);
		return goodsItemMapper.selectPurchaseCenterItem(params);
	}

	@Override
	public GoodsPrice queryPurchaseCenterItemForEdit(GoodsItemEntity entity) {
		return goodsItemMapper.selectPurchaseCenterItemForEdit(entity);
	}

	@Override
	public GoodsPrice queryItemPrice(String itemId) {
		return goodsItemMapper.selectItemPrice(itemId);
	}

	@Override
	public void updateItemPrice(GoodsPrice entity) {
		goodsItemMapper.updateItemPrice(entity);
	}

	@Override
	public Page<GoodsEntity> queryByPageDownload(GoodsItemEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsItemMapper.selectForPageDownload(entity);
	}

	@Override
	public Page<GoodsEntity> queryCenterByPageDownload(GoodsItemEntity entity, int centerId) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entity", entity);
		params.put("centerId", centerId);
		return goodsItemMapper.selectCenterForPageDownload(params);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void batchBeUse(GoodsItemEntity entity) {
		String[] arr = entity.getItemId().split(",");
		List<String> itemIdList = Arrays.asList(arr);
		goodsItemMapper.updateGoodsItemBeUseForBatch(itemIdList);
		goodsItemMapper.insertStockForBatch(itemIdList);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void batchBeFx(GoodsItemEntity entity) {
		String[] arr = entity.getItemId().split(",");
		List<String> itemIdList = Arrays.asList(arr);
		goodsItemMapper.updateGoodsItemBeFxForBatch(itemIdList);
		//更新索引并发布
		updateLuceneAndPublish(itemIdList, publishType);
		threadPoolComponent.sendGoodsInfo(itemIdList);//通知对接用户上架，可能修改了价格等信息
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void batchNotBeFx(GoodsItemEntity entity) {
		String[] arr = entity.getItemId().split(",");
		List<String> itemIdList = Arrays.asList(arr);
		goodsItemMapper.updateGoodsItemNotBeFxForBatch(itemIdList);
		//更新索引并发布
		updateLuceneAndPublish(itemIdList, publishType);
		threadPoolComponent.sendGoodsInfoDownShelves(itemIdList);//通知对接用户下架
	}

	@Override
	public Page<GoodsExtensionEntity> queryGoodsExtensionByPageDownload(GoodsItemEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsItemMapper.selectGoodsExtensionForPageDownload(entity);
	}

	@Override
	public Page<GoodsExtensionEntity> queryGoodsExtensionCenterByPageDownload(GoodsItemEntity entity, int centerId) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entity", entity);
		params.put("centerId", centerId);
		return goodsItemMapper.selectGoodsExtensionCenterForPageDownload(params);
	}

	@Override
	public GoodsExtensionEntity queryGoodsExtensionInfo(GoodsExtensionEntity entity) {
		return goodsItemMapper.selectGoodsExtensionInfo(entity);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void updateGoodsExtension(GoodsExtensionEntity entity) {
		goodsItemMapper.updateOrInsertGoodsExtension(entity);
	}

	@Override
	public List<GoodsPriceRatioEntity> queryGoodsPriceRatioListInfo(GoodsItemEntity entity) {
		return goodsItemMapper.selectGoodsPriceRatioListInfo(entity);
	}

	@Override
	public Page<GoodsRatioPlatformEntity> queryGoodsRatioPlanformPage(GoodsRatioPlatformEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsItemMapper.selectGoodsRatioPlatformForPage(entity);
	}

	@Override
	public GoodsRatioPlatformEntity queryGoodsRatioPlanformInfo(GoodsRatioPlatformEntity entity) {
		return goodsItemMapper.selectGoodsRatioPlatformForEdit(entity);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void createGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity) {
		goodsItemMapper.insertGoodsRatioPlatformInfo(entity);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void updateGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity) {
		goodsItemMapper.updateGoodsRatioPlatformInfo(entity);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public void syncGoodsPriceRatioInfo(List<GoodsPriceRatioEntity> list) {
		goodsItemMapper.syncGoodsPriceRatioInfo(list);
	}
}
