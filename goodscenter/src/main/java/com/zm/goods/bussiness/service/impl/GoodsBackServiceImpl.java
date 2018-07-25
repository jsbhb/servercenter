/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service.impl  
 * Date:Nov 9, 20178:37:14 PM  
 *  
 */
package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.component.GoodsServiceUtil;
import com.zm.goods.bussiness.dao.GoodsBackMapper;
import com.zm.goods.bussiness.dao.GoodsBaseMapper;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.service.GoodsBackService;
import com.zm.goods.constants.Constants;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.ERPGoodsTagBindEntity;
import com.zm.goods.pojo.ERPGoodsTagEntity;
import com.zm.goods.pojo.GoodsBaseEntity;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsFielsMaintainBO;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsInfoEntity;
import com.zm.goods.pojo.GoodsInfoListForDownload;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.GoodsListDownloadParam;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.GoodsRebateEntity;
import com.zm.goods.pojo.GoodsStockEntity;
import com.zm.goods.pojo.GoodsTagBindEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.TagFuncEntity;
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

	@Resource
	GoodsBaseMapper goodsBaseMapper;

	@Resource
	GoodsTagMapper goodsTagMapper;

	@Resource
	RedisTemplate<String, Object> template;

	@Override
	public Page<GoodsEntity> queryByPage(GoodsEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectForPage(entity);
	}

	@Override
	public GoodsEntity queryById(int id) {
		GoodsEntity entity = goodsBackMapper.selectById(id);
		List<GoodsFile> fileList = goodsBackMapper.selectGoodsFileByGoodsId(entity);
		GoodsEntity entityWithItem = goodsBackMapper.selectGoodsWithItem(id);
		ERPGoodsTagBindEntity tagBind = goodsBackMapper.selectGoodsTagBindByItemId(entityWithItem.getGoodsItem());
		entity.setFiles(fileList);
		entity.setGoodsTagBind(tagBind);
		return entity;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void save(GoodsEntity entity, String type) {
		goodsBackMapper.insert(entity);
		goodsItemMapper.insert(entity.getGoodsItem());
		goodsItemMapper.insertPrice(entity.getGoodsItem().getGoodsPrice());
		if (entity.getFiles() != null && entity.getFiles().size() > 0) {
			goodsItemMapper.insertFiles(entity.getFiles());
		}

		if ("sync".equals(type)) {
			goodsBackMapper.updateThirdStatus(entity.getThirdId());
		}
		if (entity.getGoodsTagBind() != null) {
			goodsBackMapper.insertTagBind(entity.getGoodsTagBind());
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
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void edit(GoodsEntity entity) {
		goodsBackMapper.update(entity);
		List<GoodsFile> isrFileList = new ArrayList<GoodsFile>();
		if (entity.getFiles() != null && entity.getFiles().size() > 0) {
			for (GoodsFile gf : entity.getFiles()) {
				if (gf.getId() != null) {
					goodsItemMapper.updateFiles(gf);
				} else {
					isrFileList.add(gf);
				}
			}
		}
		if (isrFileList != null && isrFileList.size() > 0) {
			goodsItemMapper.insertFiles(isrFileList);
		}
		// 判断商品标签
		// if (entity.getGoodsTagBind() != null) {
		// //增删改
		// ERPGoodsTagBindEntity oldTag =
		// goodsBackMapper.selectGoodsTagBindByGoodsId(entity.getGoodsItem());
		// ERPGoodsTagBindEntity newTag = entity.getGoodsTagBind();
		// if (oldTag == null && newTag.getTagId() != 0) {
		// goodsBackMapper.insertTagBind(newTag);
		// } else if (oldTag != null && newTag.getTagId() == 0) {
		// goodsBackMapper.deleteTagBind(newTag);
		// } else if (oldTag != null && newTag.getTagId() != 0) {
		// goodsBackMapper.updateTagBind(newTag);
		// }
		// }
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
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

	@Override
	public Page<GoodsRebateEntity> queryAllGoods(GoodsEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectAllGoodsForRebate(entity);
	}

	@Override
	public List<GoodsRebateEntity> queryById(String itemId) {
		return goodsBackMapper.selectGoodsRebateById(itemId);
	}

	@Override
	public GoodsRebateEntity checkRecordForRebate(GoodsRebateEntity entity) {
		return goodsBackMapper.selectRecordForRebate(entity);
	}

	@Override
	public void insertGoodsRebate(List<GoodsRebateEntity> entityList) {
		goodsBackMapper.insertGoodsRebate(entityList);
		setRedis(entityList);
	}

	private void setRedis(List<GoodsRebateEntity> entityList) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> result = null;
		Map<String, List<GoodsRebateEntity>> map = new HashMap<String, List<GoodsRebateEntity>>();
		List<GoodsRebateEntity> tempList = null;
		for (GoodsRebateEntity model : entityList) {
			if (map.get(model.getItemId()) == null) {
				tempList = new ArrayList<GoodsRebateEntity>();
				tempList.add(model);
				map.put(model.getItemId(), tempList);
			} else {
				map.get(model.getItemId()).add(model);
			}
		}
		for (Map.Entry<String, List<GoodsRebateEntity>> entry : map.entrySet()) {
			result = new HashMap<String, String>();
			for (GoodsRebateEntity entity : entry.getValue()) {
				result.put(entity.getGradeType() + "", entity.getProportion() + "");
			}
			hashOperations.putAll(Constants.GOODS_REBATE + entry.getKey(), result);
		}
	}

	@Override
	public void updateGoodsRebate(GoodsRebateEntity entity) {
		// goodsBackMapper.updateGoodsRebate(entity);
		// setRedis(entity);
	}

	@Override
	public Page<ERPGoodsTagEntity> queryTagForPage(ERPGoodsTagEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectTagForPage(entity);
	}

	@Override
	public void insertGoodsTag(ERPGoodsTagEntity entity) {
		goodsBackMapper.insertGoodsTag(entity);
	}

	@Override
	public void updateGoodsTag(ERPGoodsTagEntity entity) {
		goodsBackMapper.updateGoodsTag(entity);
	}

	@Override
	public void deleteGoodsTag(ERPGoodsTagEntity entity) {
		goodsBackMapper.deleteGoodsTag(entity);
	}

	@Override
	public ERPGoodsTagEntity queryTagInfo(ERPGoodsTagEntity entity) {
		return goodsBackMapper.selectTagInfo(entity);
	}

	@Override
	public List<ERPGoodsTagEntity> queryTagListInfo() {
		return goodsBackMapper.selectTagListInfo();
	}

	@Override
	public List<TagFuncEntity> queryTagFuncList() {
		return goodsBackMapper.selectTagFuncListInfo();
	}

	@Override
	public List<ERPGoodsTagBindEntity> queryGoodsTagBindListInfo(ERPGoodsTagBindEntity entity) {
		return goodsBackMapper.selectGoodsTagBindListInfo(entity);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public ResultModel saveGoodsInfo(GoodsInfoEntity entity) {
		StringBuilder sb = new StringBuilder();

		List<GoodsItemEntity> insItemList = new ArrayList<GoodsItemEntity>();
		List<GoodsPrice> insPriceList = new ArrayList<GoodsPrice>();
		List<String> insItemIdList = new ArrayList<String>();
		for (GoodsItemEntity gitem : entity.getGoods().getItems()) {
			insItemList.add(gitem);
			insPriceList.add(gitem.getGoodsPrice());
			insItemIdList.add(gitem.getItemId());
		}

		List<GoodsItemEntity> tempList = goodsItemMapper.listGoodsItemByParam(insItemList);
		if (tempList != null && tempList.size() > 0) {
			sb.append("以下自有编码和换算比例的组合已经存在，请核对----");
			for (GoodsItemEntity goodsItem : insItemList) {
				sb.append(goodsItem.getSku() + "," + goodsItem.getConversion() + ";");
			}
			return new ResultModel(false, sb.toString());
		}

		//设定商品的标签权重
		Integer tmpGoodsTagRatio = 0;
		if (entity.getGoods().getGoodsTagBindList() != null && entity.getGoods().getGoodsTagBindList().size() > 0) {
			goodsBackMapper.insertTagBindList(entity.getGoods().getGoodsTagBindList());
			tmpGoodsTagRatio = calcGoodsTagRatio(entity.getGoods().getGoodsTagBindList());
		}
		entity.getGoods().setGoodsTagRatio(tmpGoodsTagRatio);

		if (entity.getGoodsBase().getId() != 0) {
			goodsBaseMapper.insert(entity.getGoodsBase());
		}
		goodsBackMapper.insert(entity.getGoods());
		if (insItemList.size() > 0) {
			goodsItemMapper.insertBatch(insItemList);
		}
		if (insPriceList.size() > 0) {
			goodsItemMapper.insertPriceBatch(insPriceList);
		}
		if (insItemIdList.size() > 0) {
			goodsItemMapper.insertStockForBatch(insItemIdList);
		}
		if (entity.getGoods().getFiles() != null && entity.getGoods().getFiles().size() > 0) {
			goodsItemMapper.insertFiles(entity.getGoods().getFiles());
		}
		return new ResultModel(true, "");
	}

	@Override
	public GoodsInfoEntity queryGoodsInfoEntity(String itemId) {
		GoodsInfoEntity goodsInfo = new GoodsInfoEntity();
		// 查询商品信息
		GoodsItemEntity goodsItemEntity = goodsBackMapper.selectGoodsItemByItemId(itemId);
		GoodsPrice goodsPrice = goodsItemMapper.selectItemPrice(itemId);
		GoodsEntity goodsEntity = goodsBackMapper.selectGoodsEntityByItemId(goodsItemEntity.getGoodsId());
		List<GoodsFile> goodsFiles = goodsBackMapper.selectGoodsFileByGoodsId(goodsEntity);
		List<ERPGoodsTagBindEntity> erpGoodsTagBindList = goodsBackMapper.selectGoodsTagBindListByItemId(goodsItemEntity);
		GoodsBaseEntity goodsBaseEntity = goodsBaseMapper.selectById(goodsEntity.getBaseId());
		// 组装商品信息
		goodsItemEntity.setGoodsPrice(goodsPrice);
		List<GoodsItemEntity> items = new ArrayList<GoodsItemEntity>();
		items.add(goodsItemEntity);
		goodsEntity.setFiles(goodsFiles);
//		goodsEntity.setGoodsTagBind(erpGoodsTagBind);
		goodsEntity.setGoodsTagBindList(erpGoodsTagBindList);
		// goodsEntity.setGoodsItem(goodsItemEntity);
		goodsEntity.setItems(items);
		goodsInfo.setGoods(goodsEntity);
		goodsInfo.setGoodsBase(goodsBaseEntity);
		return goodsInfo;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public ResultModel updateGoodsInfo(GoodsInfoEntity entity) {
		StringBuilder sb = new StringBuilder();

		List<GoodsItemEntity> insItemList = new ArrayList<GoodsItemEntity>();
		List<GoodsItemEntity> updItemList = new ArrayList<GoodsItemEntity>();
		List<GoodsPrice> insPriceList = new ArrayList<GoodsPrice>();
		List<GoodsPrice> updPriceList = new ArrayList<GoodsPrice>();
		for (GoodsItemEntity gitem : entity.getGoods().getItems()) {
			if ("true".equals(gitem.getIsCreate())) {
				insItemList.add(gitem);
				insPriceList.add(gitem.getGoodsPrice());
			} else {
				updItemList.add(gitem);
				updPriceList.add(gitem.getGoodsPrice());
			}
		}

		for (GoodsItemEntity gie : updItemList) {
			List<GoodsItemEntity> tempList = goodsItemMapper.listGoodsItemForCheck(gie);
			if (tempList != null && tempList.size() > 0) {
				sb.append("以下自有编码和换算比例的组合已经存在，请核对----");
				sb.append(gie.getSku() + "," + gie.getConversion() + ";");
				return new ResultModel(false, sb.toString());
			}
		}
		if (insItemList.size() > 0) {
			goodsItemMapper.insertBatch(insItemList);
		}
		if (updItemList.size() > 0) {
			goodsItemMapper.updateBatch(updItemList);
		}
		if (insPriceList.size() > 0) {
			goodsItemMapper.insertPriceBatch(insPriceList);
		}
		if (updPriceList.size() > 0) {
			goodsItemMapper.updatePriceBatch(updPriceList);
		}

		if (entity.getGoods().getFiles() != null && entity.getGoods().getFiles().size() > 0) {
			// 商品编辑时，先查询原有的file数据进行比较，然后判断如何处理
			List<GoodsFile> oldFiles = goodsBackMapper.selectGoodsFileByGoodsId(entity.getGoods());
			List<GoodsFile> existFiles = new ArrayList<GoodsFile>();

			// 过滤相同文件列表
			for (GoodsFile ngf : entity.getGoods().getFiles()) {
				for (GoodsFile gf : oldFiles) {
					if (ngf.getGoodsId().equals(gf.getGoodsId()) && ngf.getPath().equals(gf.getPath())) {
						existFiles.add(gf);
						oldFiles.remove(gf);
						break;
					}
				}
			}
			// 挑出新增文件列表
			for (GoodsFile gf : existFiles) {
				for (GoodsFile ngf : entity.getGoods().getFiles()) {
					if (ngf.getGoodsId().equals(gf.getGoodsId()) && ngf.getPath().equals(gf.getPath())) {
						entity.getGoods().getFiles().remove(ngf);
						break;
					}
				}
			}

			if (entity.getGoods().getFiles().size() > 0) {
				goodsItemMapper.insertFiles(entity.getGoods().getFiles());
			}
			if (oldFiles.size() > 0) {
				goodsItemMapper.deleteListFiles(oldFiles);
			}
		} else {
			// 商品编辑时，如果没有传图片信息，则删除表中记录
			goodsItemMapper.deleteAllFiles(entity.getGoods());
		}
		
		//设定商品的标签权重
		Integer tmpGoodsTagRatio = 0;
		
		// 判断商品标签
		List<ERPGoodsTagBindEntity> oldTagList = goodsBackMapper.selectGoodsTagBindListByItemId(entity.getGoods().getItems().get(0));
		if (entity.getGoods().getGoodsTagBindList() != null && entity.getGoods().getGoodsTagBindList().size() > 0) {
			// 增删改
			List<ERPGoodsTagBindEntity> newTagList = entity.getGoods().getGoodsTagBindList();
			List<ERPGoodsTagBindEntity> existTagList = new ArrayList<ERPGoodsTagBindEntity>();
			for(ERPGoodsTagBindEntity netbe:newTagList) {
				for(ERPGoodsTagBindEntity oetbe:oldTagList) {
					if (oetbe.getItemId().equals(netbe.getItemId()) && oetbe.getTagId() == netbe.getTagId()) {
						existTagList.add(oetbe);
						oldTagList.remove(oetbe);
						break;
					}
				}
			}
			
			for(ERPGoodsTagBindEntity eetbe:existTagList) {
				for(ERPGoodsTagBindEntity netbe:newTagList) {
					if (eetbe.getItemId().equals(netbe.getItemId()) && eetbe.getTagId() == netbe.getTagId()) {
						newTagList.remove(netbe);
						break;
					}
				}
			}
			
			if (newTagList.size() > 0) {
				goodsBackMapper.insertTagBindList(newTagList);
				tmpGoodsTagRatio = calcGoodsTagRatio(newTagList);
			}
			if (oldTagList.size() > 0) {
				goodsBackMapper.deleteTagBindList(oldTagList);
				tmpGoodsTagRatio = tmpGoodsTagRatio - calcGoodsTagRatio(oldTagList);
			}
		} else {
			if (oldTagList.size() > 0) {
				goodsBackMapper.deleteTagBindList(oldTagList);
				tmpGoodsTagRatio = calcGoodsTagRatio(oldTagList);
			}
		}
		entity.getGoods().setGoodsTagRatio(tmpGoodsTagRatio);
		goodsBackMapper.updateGoodsEntity(entity.getGoods());
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel getGoodsRebate(String itemId) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		return new ResultModel(true, hashOperations.entries(Constants.GOODS_REBATE + itemId));
	}

	@Override
	public List<GoodsInfoListForDownload> queryGoodsListForDownload(GoodsListDownloadParam param) {
		if (param.getGradeType() != null && param.getGradeType() != -1) {
			return goodsBackMapper.selectGoodsListForDownload(param);
		} else {
			List<GoodsInfoListForDownload> partOne = goodsBackMapper.selectGoodsListForDownloadPartOne(param);
			List<GoodsInfoListForDownload> partTwo = goodsBackMapper.selectGoodsListForDownloadPartTwo(param);
			List<GoodsInfoListForDownload> partThree = goodsBackMapper.selectGoodsListForDownloadPartThree(param);
			LogUtil.writeLog("【数据拼接开始】" + System.currentTimeMillis() / 1000);
			for(GoodsInfoListForDownload partTwoEntity: partTwo) {
				for(GoodsInfoListForDownload partOneEntity: partOne) {
					if (partTwoEntity.getItemId().equals(partOneEntity.getItemId())) {
						partTwoEntity.setGoodsId(partOneEntity.getGoodsId());
						partTwoEntity.setSku(partOneEntity.getSku());
						partTwoEntity.setInfo(partOneEntity.getInfo());
						partTwoEntity.setGoodsName(partOneEntity.getGoodsName());
						partTwoEntity.setBrand(partOneEntity.getBrand());
						partTwoEntity.setOrigin(partOneEntity.getOrigin());
						partTwoEntity.setGoodsStatus(partOneEntity.getGoodsStatus());
						partTwoEntity.setItemStatus(partOneEntity.getItemStatus());
						partTwoEntity.setSupplierName(partOneEntity.getSupplierName());
						partTwoEntity.setFirstName(partOneEntity.getFirstName());
						partTwoEntity.setSecondName(partOneEntity.getSecondName());
						partTwoEntity.setThirdName(partOneEntity.getThirdName());
						partTwoEntity.setEncode(partOneEntity.getEncode());
						partTwoEntity.setCarton(partOneEntity.getCarton());
						partTwoEntity.setShelfLife(partOneEntity.getShelfLife());
						partTwoEntity.setGoodsType(partOneEntity.getGoodsType());
						break;
					}
				}
				for(GoodsInfoListForDownload partThreeEntity: partThree) {
					if (partTwoEntity.getItemId().equals(partThreeEntity.getItemId())) {
						partTwoEntity.setGoodsTagList(partThreeEntity.getGoodsTagList());
						partTwoEntity.setGoodsPriceRatioList(partThreeEntity.getGoodsPriceRatioList());
						break;
					}
				}
			}
			LogUtil.writeLog("【数据拼接结束】" + System.currentTimeMillis() / 1000);
			return partTwo;
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void maintainStockByItemId(List<GoodsStockEntity> stocks) {
		goodsItemMapper.updateGoodsStockByItemId(stocks);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public String maintainFiles(List<GoodsFielsMaintainBO> list) {
		StringBuilder sb = new StringBuilder();
		if (list != null && list.size() > 0) {
			GoodsEntity entity = null;
			GoodsFile goodsFile = null;
			List<GoodsFile> fileList = null;
			List<String> goodsIdList = null;
			for (GoodsFielsMaintainBO model : list) {
				goodsIdList = goodsBackMapper.listGoodsIdsByItemCode(model.getItemCode());
				if (goodsIdList != null && goodsIdList.size() > 0) {
					for (String goodsId : goodsIdList) {
						entity = new GoodsEntity();
						entity.setGoodsId(goodsId);
						entity.setDetailPath(model.getGoodsDetailPath());
						goodsBackMapper.updateDetailPath(entity);
						if (model.getPicPathList() != null && model.getPicPathList().size() > 0) {
							fileList = new ArrayList<GoodsFile>();
							for (String str : model.getPicPathList()) {
								goodsFile = new GoodsFile();
								goodsFile.setGoodsId(goodsId);
								goodsFile.setPath(str);
								goodsFile.setOpt("batch");
								fileList.add(goodsFile);
							}
							goodsItemMapper.insertFiles(fileList);
						}
					}
				} else {
					sb.append(model.getItemCode() + ",");
				}
			}
			if (sb.length() > 0) {
				sb.append("没有找到以上商家编码商品,请核对后重新上传以上商品");
			}
		}
		return sb.toString();
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public ResultModel importGoods(List<GoodsInfoEntity> list) {
		StringBuilder sb = new StringBuilder();
		List<GoodsBaseEntity> baseList = new ArrayList<GoodsBaseEntity>();
		List<GoodsEntity> goodsList = new ArrayList<GoodsEntity>();
		List<GoodsItemEntity> itemList = new ArrayList<GoodsItemEntity>();
		List<GoodsPrice> priceList = new ArrayList<GoodsPrice>();
		List<GoodsStockEntity> stockList = new ArrayList<GoodsStockEntity>();
		List<GoodsRebateEntity> rebateList = new ArrayList<GoodsRebateEntity>();
		if (list != null && list.size() > 0) {
			for (GoodsInfoEntity entity : list) {
				for (GoodsItemEntity goodsItem : entity.getGoods().getItems()) {
					itemList.add(goodsItem);
					priceList.add(goodsItem.getGoodsPrice());
					stockList.add(goodsItem.getStock());
					rebateList.addAll(goodsItem.getGoodsRebateList());
				}
				baseList.add(entity.getGoodsBase());
				goodsList.add(entity.getGoods());
			}
			List<GoodsItemEntity> tempList = goodsItemMapper.listGoodsItemByParam(itemList);
			if (tempList != null && tempList.size() > 0) {
				sb.append("以下自有编码和换算比例的组合已经存在，请核对----");
				for (GoodsItemEntity goodsItem : tempList) {
					sb.append(goodsItem.getSku() + "," + goodsItem.getConversion() + ";");
				}
				return new ResultModel(false, sb.toString());
			}
			if (baseList.size() > 0) {
				goodsBaseMapper.insertBatch(baseList);
			}
			if (goodsList.size() > 0) {
				goodsBackMapper.insertBatch(goodsList);
			}
			if (itemList.size() > 0) {
				goodsItemMapper.insertBatch(itemList);
			}
			if (priceList.size() > 0) {
				goodsItemMapper.insertPriceBatch(priceList);
			}
			if (stockList.size() > 0) {
				goodsItemMapper.insertStockBatch(stockList);
			}
			if (rebateList.size() > 0) {
				insertGoodsRebate(rebateList);
			}
		}
		return new ResultModel(true, "操作成功");
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public ResultModel tagBatchBind(List<GoodsTagBindEntity> list) {
		if (list != null && list.size() > 0) {
			List<String> itemList = new ArrayList<String>();
			for (GoodsTagBindEntity gtbe:list) {
				itemList.add(gtbe.getItemId());
			}
			//去除重复绑定的标签
			List<GoodsTagBindEntity> existTagList = goodsTagMapper.listGoodsTagBindByItemList(itemList);
			for (GoodsTagBindEntity gte:existTagList) {
				for (GoodsTagBindEntity gtbe:list) {
					if (gte.getItemId().equals(gtbe.getItemId()) && gte.getTagId() == gtbe.getTagId()) {
						list.remove(gtbe);
						break;
					}
				}
			}
			if (list.size() <= 0) {
				return new ResultModel(true, null);
			} else {
				for (GoodsTagBindEntity gtbe:list) {
					itemList.add(gtbe.getItemId());
				}
			}
			//批量打标签后更新商品明细的更新时间
			goodsTagMapper.batchInsert(list);
			goodsItemMapper.updateGoodsItemUpdateTimeByItemIdList(itemList);
			
			//设定权重
			List<GoodsEntity> goodsIdList = goodsTagMapper.listGoodsIdByItemList(itemList);
			List<ERPGoodsTagBindEntity> tmpGoodsTagBindEntityList = null;
			Map<String,List<ERPGoodsTagBindEntity>> tempMap = new HashMap<String,List<ERPGoodsTagBindEntity>>();
			for(GoodsTagBindEntity gtbe:list) {
				if(tempMap.get(gtbe.getItemId()) == null){
					tmpGoodsTagBindEntityList = new ArrayList<ERPGoodsTagBindEntity>();
					tmpGoodsTagBindEntityList.add(convertToERPGoodsTagBindEntity(gtbe));
					tempMap.put(gtbe.getItemId(), tmpGoodsTagBindEntityList);
				} else {
					tempMap.get(gtbe.getItemId()).add(convertToERPGoodsTagBindEntity(gtbe));
				}
			}
			for(GoodsEntity ge:goodsIdList) {
				tmpGoodsTagBindEntityList = new ArrayList<ERPGoodsTagBindEntity>();
				for(GoodsItemEntity gie:ge.getItems()) {
					if(tempMap.get(gie.getItemId()) != null){
						tmpGoodsTagBindEntityList.addAll(tempMap.get(gie.getItemId()));
					}
				}
				ge.setGoodsTagRatio(calcGoodsTagRatio(tmpGoodsTagBindEntityList));
			}
			goodsBackMapper.updateGoodsTagRatioByList(goodsIdList);
		}
		return new ResultModel(true, null);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public ResultModel saveItemInfo(GoodsInfoEntity entity) {
		StringBuilder sb = new StringBuilder();

		List<GoodsItemEntity> insItemList = new ArrayList<GoodsItemEntity>();
		List<GoodsPrice> insPriceList = new ArrayList<GoodsPrice>();
		List<String> insItemIdList = new ArrayList<String>();
		for (GoodsItemEntity gitem : entity.getGoods().getItems()) {
			insItemList.add(gitem);
			insPriceList.add(gitem.getGoodsPrice());
			insItemIdList.add(gitem.getItemId());
		}

		for (GoodsItemEntity gie : insItemList) {
			List<GoodsItemEntity> tempList = goodsItemMapper.listGoodsItemForCheck(gie);
			if (tempList != null && tempList.size() > 0) {
				sb.append("以下自有编码和换算比例的组合已经存在，请核对----");
				sb.append(gie.getSku() + "," + gie.getConversion() + ";");
				return new ResultModel(false, sb.toString());
			}
		}

		if (insItemList.size() > 0) {
			goodsItemMapper.insertBatch(insItemList);
		}
		if (insPriceList.size() > 0) {
			goodsItemMapper.insertPriceBatch(insPriceList);
		}
		if (insItemIdList.size() > 0) {
			goodsItemMapper.insertStockForBatch(insItemIdList);
		}

		if (entity.getGoods().getFiles() != null && entity.getGoods().getFiles().size() > 0) {
			// 商品编辑时，先查询原有的file数据进行比较，然后判断如何处理
			List<GoodsFile> oldFiles = goodsBackMapper.selectGoodsFileByGoodsId(entity.getGoods());
			List<GoodsFile> existFiles = new ArrayList<GoodsFile>();

			// 过滤相同文件列表
			for (GoodsFile ngf : entity.getGoods().getFiles()) {
				for (GoodsFile gf : oldFiles) {
					if (ngf.getGoodsId().equals(gf.getGoodsId()) && ngf.getPath().equals(gf.getPath())) {
						existFiles.add(gf);
						oldFiles.remove(gf);
						break;
					}
				}
			}
			// 挑出新增文件列表
			for (GoodsFile gf : existFiles) {
				for (GoodsFile ngf : entity.getGoods().getFiles()) {
					if (ngf.getGoodsId().equals(gf.getGoodsId()) && ngf.getPath().equals(gf.getPath())) {
						entity.getGoods().getFiles().remove(ngf);
						break;
					}
				}
			}

			if (entity.getGoods().getFiles().size() > 0) {
				goodsItemMapper.insertFiles(entity.getGoods().getFiles());
			}
			if (oldFiles.size() > 0) {
				goodsItemMapper.deleteListFiles(oldFiles);
			}
		} else {
			// 商品编辑时，如果没有传图片信息，则删除表中记录
			goodsItemMapper.deleteAllFiles(entity.getGoods());
		}
		//设定商品的标签权重
		Integer tmpGoodsTagRatio = 0;
		if (entity.getGoods().getGoodsTagBindList() != null && entity.getGoods().getGoodsTagBindList().size() > 0) {
			goodsBackMapper.insertTagBindList(entity.getGoods().getGoodsTagBindList());
			tmpGoodsTagRatio = calcGoodsTagRatio(entity.getGoods().getGoodsTagBindList());
		}
		entity.getGoods().setGoodsTagRatio(tmpGoodsTagRatio);
		goodsBackMapper.updateGoodsEntity(entity.getGoods());
		return new ResultModel(true, "");
	}

	private final Integer PUBLISH_ERROR = 1;
	private final Integer UN_PUBLISH_ERROR = 2;

	@Override
	public Page<GoodsEntity> listPublishExceptionGoods(Integer type, GoodsEntity entity, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", GoodsServiceUtil.judgeCenterId(centerId));
		param.put("entity", entity);
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		if (PUBLISH_ERROR == type) {
			return goodsBackMapper.listPublishError(param);
		}
		if(UN_PUBLISH_ERROR == type){
			return goodsBackMapper.listUnPublishError(param);
		}
		return null;
	}
	
	private Integer calcGoodsTagRatio(List<ERPGoodsTagBindEntity> checkList) {
		Integer tmpGoodsTagRatio = 0;
		
		List<ERPGoodsTagEntity> allTagList= goodsBackMapper.selectTagListInfo();
		for (ERPGoodsTagBindEntity gtbe : checkList) {
			for (ERPGoodsTagEntity gte : allTagList) {
				if (gtbe.getTagId() == gte.getId()) {
					tmpGoodsTagRatio += gte.getTagRatio();
					break;
				}
			}
		}
		
		return tmpGoodsTagRatio;
	}
	
	private ERPGoodsTagBindEntity convertToERPGoodsTagBindEntity(GoodsTagBindEntity temp){
		ERPGoodsTagBindEntity entity = new ERPGoodsTagBindEntity();
		entity.setItemId(temp.getItemId());
		entity.setTagId(temp.getTagId());
		return entity;
		
	}
}
