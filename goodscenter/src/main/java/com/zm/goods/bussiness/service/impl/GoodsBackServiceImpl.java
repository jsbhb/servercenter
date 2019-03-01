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
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.dao.GoodsBackMapper;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.dao.GoodsPropertyMapper;
import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.service.GoodsBackService;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsInfoListForDownload;
import com.zm.goods.pojo.GoodsListDownloadParam;
import com.zm.goods.pojo.GoodsRebateEntity;
import com.zm.goods.pojo.GoodsTagBindEntity;
import com.zm.goods.pojo.GoodsTagEntity;
import com.zm.goods.pojo.GuidePropertyEntity;
import com.zm.goods.pojo.GuidePropertyValueEntity;
import com.zm.goods.pojo.PropertyEntity;
import com.zm.goods.pojo.PropertyValueEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.TagFuncEntity;
import com.zm.goods.pojo.bo.GoodsRender4New;
import com.zm.goods.pojo.po.BackGoodsPO;
import com.zm.goods.pojo.po.Goods;
import com.zm.goods.pojo.po.GoodsPricePO;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.po.GuidePropertyBindGoods;
import com.zm.goods.pojo.po.Items;
import com.zm.goods.pojo.vo.BackGoodsItemVO;
import com.zm.goods.pojo.vo.BackGoodsVO;
import com.zm.goods.pojo.vo.BackItemsVO;
import com.zm.goods.pojo.vo.GoodsSpecsVO;
import com.zm.goods.processWarehouse.model.WarehouseModel;

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
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GoodsBackServiceImpl implements GoodsBackService {

	@Resource
	GoodsBackMapper goodsBackMapper;

	@Resource
	GoodsItemMapper goodsItemMapper;

	@Resource
	GoodsTagMapper goodsTagMapper;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	GoodsService goodsService;

	@Resource
	GoodsPropertyMapper goodsPropertyMapper;

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void save(BackGoodsPO entity) {
		if (entity.getGoods() != null) {
			goodsBackMapper.insertGoods(entity.getGoods());
			if (entity.getGoods().getGoodsFileList() != null && entity.getGoods().getGoodsFileList().size() > 0) {
				goodsItemMapper.insertFiles(entity.getGoods().getGoodsFileList());
			}
		}
		if (entity.getGoodsSpecsTradePattern() != null) {
			goodsBackMapper.insertSpecsTp(entity.getGoodsSpecsTradePattern());
		}
		if (entity.getItemsList() != null && entity.getItemsList().size() > 0) {
			goodsBackMapper.insertItemBatch(entity.getItemsList());
		}
		if (entity.getSpecs() != null) {
			goodsBackMapper.insertSpecs(entity.getSpecs());
		}
		if (entity.getPriceList() != null && entity.getPriceList().size() > 0) {
			goodsBackMapper.insertItemPriceBatch(entity.getPriceList());
		}
		if (entity.getStockList() != null && entity.getStockList().size() > 0) {
			goodsBackMapper.insertStockBatch(entity.getStockList());
		}
	}

	@Override
	public BackGoodsPO queryByItemdId(String itemId) {
		Items item = goodsMapper.getItemsByItemIds(itemId);
		if (item == null) {
			return null;
		}
		List<Items> itemList = new ArrayList<>();
		itemList.add(item);
		GoodsSpecsTradePattern specsTp = goodsMapper.getGoodsSpecsTpBySpecsTpId(item.getSpecsTpId());
		Goods goods = goodsMapper.getGoodsByGoodsId(specsTp.getGoodsId());
		GoodsSpecs specs = goodsMapper.getGoodsSpecsBySpecsId(specsTp.getSpecsId());
		List<String> itemIds = new ArrayList<>();
		itemIds.add(item.getItemId());
		List<GoodsPricePO> priceList = goodsMapper.listGoodsPriceByItemIds(itemIds);
		List<WarehouseModel> stockList = goodsMapper.listWarehouse(itemIds);
		List<String> specsTpIds = new ArrayList<>();
		specsTpIds.add(specsTp.getSpecsTpId());
		List<GoodsTagBindEntity> bindList = goodsTagMapper.listGoodsTagBindBySpecsTpIdList(specsTpIds);
		List<GoodsRebateEntity> rebateList = goodsBackMapper.selectGoodsRebateBySpecsTpId(specsTp.getSpecsTpId());
		// 创建返回对象
		BackGoodsPO backGoods = new BackGoodsPO();
		backGoods.setGoods(goods);
		backGoods.setGoodsSpecsTradePattern(specsTp);
		backGoods.setItemsList(itemList);
		backGoods.setPriceList(priceList);
		backGoods.setSpecs(specs);
		backGoods.setStockList(stockList);
		backGoods.setTagList(bindList);
		backGoods.setRebateList(rebateList);
		return backGoods;
	}

	@Override
	public BackGoodsPO queryBySpecsTpId(String specsTpId) {
		List<String> specsTpIds = new ArrayList<>();
		specsTpIds.add(specsTpId);
		List<Items> itemList = goodsMapper.listItemsBySpecsTpIds(specsTpIds);
		GoodsSpecsTradePattern specsTp = goodsMapper.getGoodsSpecsTpBySpecsTpId(specsTpId);
		Goods goods = goodsMapper.getGoodsByGoodsId(specsTp.getGoodsId());
		GoodsSpecs specs = goodsMapper.getGoodsSpecsBySpecsId(specsTp.getSpecsId());
		List<String> itemIds = itemList.stream().map(i -> i.getItemId()).collect(Collectors.toList());
		List<GoodsPricePO> priceList = goodsMapper.listGoodsPriceByItemIds(itemIds);
		List<WarehouseModel> stockList = goodsMapper.listWarehouse(itemIds);
		List<GoodsTagBindEntity> bindList = goodsTagMapper.listGoodsTagBindBySpecsTpIdList(specsTpIds);
		List<GoodsRebateEntity> rebateList = goodsBackMapper.selectGoodsRebateBySpecsTpId(specsTp.getSpecsTpId());
		// 创建返回对象
		BackGoodsPO backGoods = new BackGoodsPO();
		backGoods.setGoods(goods);
		backGoods.setGoodsSpecsTradePattern(specsTp);
		backGoods.setItemsList(itemList);
		backGoods.setPriceList(priceList);
		backGoods.setSpecs(specs);
		backGoods.setStockList(stockList);
		backGoods.setTagList(bindList);
		backGoods.setRebateList(rebateList);
		return backGoods;
	}

	@Override
	public void updateGoods(BackGoodsPO entity) {
		if (entity.getGoods() != null) {
			goodsBackMapper.updateGoods(entity.getGoods());
			if (entity.getGoods().getGoodsFileList() != null && entity.getGoods().getGoodsFileList().size() > 0) {
				List<GoodsFile> initFileList = entity.getGoods().getGoodsFileList();
				List<GoodsFile> insertFileList = initFileList.stream().filter(file -> file.getId() == null)
						.collect(Collectors.toList());
				initFileList.removeAll(insertFileList);
				if (insertFileList.size() > 0) {
					goodsItemMapper.insertFiles(insertFileList);
				}
				if (initFileList.size() > 0) {
					goodsItemMapper.updateFilesBatch(initFileList);
				}
			}
		}
		if (entity.getGoodsSpecsTradePattern() != null) {
			List<String> specsTpIds = new ArrayList<>();
			specsTpIds.add(entity.getGoodsSpecsTradePattern().getSpecsTpId());
			// 标签处理
			List<GoodsTagBindEntity> bindList = goodsTagMapper.listGoodsTagBindBySpecsTpIdList(specsTpIds);
			if (bindList.size() > 0) {// 先全部清除
				List<Integer> idList = bindList.stream().map(t -> t.getId()).collect(Collectors.toList());
				goodsTagMapper.deleteGoodsTagBindByIds(idList);
			}
			if (entity.getTagList() != null && entity.getTagList().size() > 0) {
				List<Integer> tagIdList = entity.getTagList().stream().map(t -> t.getTagId())
						.collect(Collectors.toList());
				List<GoodsTagEntity> tagList = goodsTagMapper.listGoodsTagByTagIds(tagIdList);
				int tagRatio = tagList.stream().mapToInt(t -> t.getTagRatio()).sum();
				entity.getGoodsSpecsTradePattern().setTagRatio(tagRatio);
				goodsTagMapper.batchInsert(entity.getTagList());
			} else {
				entity.getGoodsSpecsTradePattern().setTagRatio(0);
			}
			goodsBackMapper.updateSpecsTp(entity.getGoodsSpecsTradePattern());
		}
		if (entity.getItemsList() != null && entity.getItemsList().size() > 0) {
			goodsBackMapper.updateItemBatch(entity.getItemsList());
		}
		if (entity.getSpecs() != null) {
			goodsBackMapper.updateSpecs(entity.getSpecs());
		}
		if (entity.getPriceList() != null && entity.getPriceList().size() > 0) {
			goodsBackMapper.updateItemPriceBatch(entity.getPriceList());
		}
		if (entity.getStockList() != null && entity.getStockList().size() > 0) {
			goodsBackMapper.updateStockBatch(entity.getStockList());
		}
		if (entity.getRebateList() != null && entity.getRebateList().size() > 0) {
			goodsBackMapper.insertGoodsRebate(entity.getRebateList());
			setRedis(entity.getRebateList());
		}
	}

	@Override
	public void saveDetailPath(Goods entity) {
		goodsBackMapper.updateDetailPath(entity);
	}

	private void setRedis(List<GoodsRebateEntity> entityList) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> result = null;
		Map<String, List<GoodsRebateEntity>> map = new HashMap<String, List<GoodsRebateEntity>>();
		List<GoodsRebateEntity> tempList = null;
		for (GoodsRebateEntity model : entityList) {
			if (map.get(model.getSpecsTpId()) == null) {
				tempList = new ArrayList<GoodsRebateEntity>();
				tempList.add(model);
				map.put(model.getSpecsTpId(), tempList);
			} else {
				map.get(model.getSpecsTpId()).add(model);
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
	public Page<GoodsTagEntity> queryTagForPage(GoodsTagEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectTagForPage(entity);
	}

	@Override
	public void insertGoodsTag(GoodsTagEntity entity) {
		goodsBackMapper.insertGoodsTag(entity);
	}

	@Override
	public void updateGoodsTag(GoodsTagEntity entity) {
		goodsBackMapper.updateGoodsTag(entity);
	}

	@Override
	public ResultModel deleteGoodsTag(GoodsTagEntity entity) {
		int count = goodsBackMapper.checkGoodsTagUsing(entity.getId());
		if (count > 0) {
			return new ResultModel(false, "该标签有商品绑定，请先解绑");
		}
		goodsBackMapper.deleteGoodsTag(entity);
		return new ResultModel(true, null);
	}

	@Override
	public List<GoodsTagEntity> queryTagListInfo() {
		return goodsBackMapper.selectTagListInfo();
	}

	@Override
	public List<TagFuncEntity> queryTagFuncList() {
		return goodsBackMapper.selectTagFuncListInfo();
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
			for (GoodsInfoListForDownload partTwoEntity : partTwo) {
				for (GoodsInfoListForDownload partOneEntity : partOne) {
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
				for (GoodsInfoListForDownload partThreeEntity : partThree) {
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
	public ResultModel tagBatchBind(List<GoodsTagBindEntity> list) {
		if (list != null && list.size() > 0) {
			List<String> specsTpIds = new ArrayList<String>();
			for (GoodsTagBindEntity gtbe : list) {
				specsTpIds.add(gtbe.getSpecsTpId());
			}
			// 去除重复绑定的标签
			List<GoodsTagBindEntity> existTagList = goodsTagMapper.listGoodsTagBindBySpecsTpIdList(specsTpIds);
			for (GoodsTagBindEntity gte : existTagList) {
				for (GoodsTagBindEntity gtbe : list) {
					if (gte.getSpecsTpId().equals(gtbe.getSpecsTpId()) && gte.getTagId() == gtbe.getTagId()) {
						list.remove(gtbe);
						break;
					}
				}
			}
			if (list.size() <= 0) {
				return new ResultModel(true, null);
			} else {
				specsTpIds.clear();
				for (GoodsTagBindEntity gtbe : list) {
					specsTpIds.add(gtbe.getSpecsTpId());
				}
			}
			// 批量打标签后更新商品明细的更新时间
			goodsTagMapper.batchInsert(list);
			goodsItemMapper.updateSpecsTpUpdateTimeBySpecsTpIdList(specsTpIds);
			List<Integer> tagIdList = list.stream().map(t -> t.getTagId()).collect(Collectors.toList());
			List<GoodsTagEntity> tagList = goodsTagMapper.listGoodsTagByTagIds(tagIdList);
			Map<Integer, GoodsTagEntity> tagMap = tagList.stream()
					.collect(Collectors.toMap(GoodsTagEntity::getId, t -> t));
			Map<String, List<GoodsTagBindEntity>> map = list.stream()
					.collect(Collectors.groupingBy(t -> t.getSpecsTpId()));
			List<GoodsSpecsTradePattern> specsTpList = new ArrayList<>();
			for (Map.Entry<String, List<GoodsTagBindEntity>> entry : map.entrySet()) {
				specsTpList.add(calTagRatio(entry.getKey(), entry.getValue(), tagMap));
			}
			// 设定权重
			goodsBackMapper.updateSpecsTpTagRatioByList(specsTpList);
		}
		return new ResultModel(true, null);
	}

	private GoodsSpecsTradePattern calTagRatio(String key, List<GoodsTagBindEntity> value,
			Map<Integer, GoodsTagEntity> tagMap) {
		int ratio = 0;
		for (GoodsTagBindEntity bind : value) {
			ratio += tagMap.get(bind.getTagId()).getTagRatio();
		}
		GoodsSpecsTradePattern sp = new GoodsSpecsTradePattern();
		sp.setSpecsTpId(key);
		sp.setTagRatio(ratio);
		return sp;
	}

	@Override
	public List<String> getGoodsPicPath(String goodsId) {

		return goodsBackMapper.getGoodsPicPath(goodsId);
	}

	@Override
	public void updateRetailPrice(Map<String, Object> param) {
		goodsBackMapper.updateRetailPrice(param);
	}

	@Override
	public void insertGoodsRebate(List<GoodsRebateEntity> entityList) {
		goodsBackMapper.insertGoodsRebate(entityList);
		setRedis(entityList);
	}

	@Override
	public List<GoodsRender4New> queryByEnCode(String encode, int type) {
		String[] arr = encode.split(",");
		// 获取规格
		List<GoodsSpecs> specsList = goodsBackMapper.listGoodsSpecsByEnCodeList(Arrays.asList(arr));
		if (specsList.size() == 0) {
			return null;
		}
		List<String> specsIdList = specsList.stream().map(s -> s.getSpecsId()).collect(Collectors.toList());
		Map<String, Object> param = new HashMap<>();
		param.put("list", specsIdList);
		param.put("type", type);
		// 获取specsTp
		List<GoodsSpecsTradePattern> specsTpList = goodsBackMapper.listGoodsSpecsTradPatternByParam(param);
		List<String> goodsIdList = specsTpList.stream().map(tp -> tp.getGoodsId()).distinct()
				.collect(Collectors.toList());
		List<String> specsTpIdList = specsTpList.stream().map(tp -> tp.getSpecsTpId()).collect(Collectors.toList());
		// 获取供应商商品
		List<Items> itemList = goodsMapper.listItemsBySpecsTpIds(specsTpIdList);
		// 根据specsTpiD分组
		Map<String, List<Items>> itemMap = itemList.stream().collect(Collectors.groupingBy(Items::getSpecsTpId));
		// 获取商品
		List<Goods> goodsList = goodsMapper.listGoodsItemByGoodsIds(goodsIdList);
		GoodsRender4New render = null;
		List<GoodsSpecsTradePattern> specsTpTmp = null;
		List<GoodsSpecs> specsTmp = null;
		List<Items> itemTmp = null;
		List<GoodsRender4New> result = new ArrayList<>();
		Map<String, GoodsSpecs> specsMap = specsList.stream().collect(Collectors.toMap(GoodsSpecs::getSpecsId, s -> s));
		for (Goods goods : goodsList) {
			render = new GoodsRender4New();
			specsTmp = new ArrayList<>();
			specsTpTmp = new ArrayList<>();
			itemTmp = new ArrayList<>();
			render.setGoods(goods);
			specsTpTmp = specsTpList.stream().filter(tp -> tp.getGoodsId().equals(goods.getGoodsId()))
					.collect(Collectors.toList());
			for (GoodsSpecsTradePattern tp : specsTpTmp) {
				specsTmp.add(specsMap.get(tp.getSpecsId()));
				itemTmp.addAll(itemMap.get(tp.getSpecsTpId()));
			}
			render.setSpecsList(specsTmp);
			render.setSpecsTpList(specsTpTmp);
			render.setItemList(itemTmp);
			result.add(render);
		}
		return result;
	}

	@Override
	public List<Goods> queryGoods4Tips(String thirdCategory, String brandId) {
		Map<String, String> param = new HashMap<>();
		param.put("thirdCategory", thirdCategory);
		param.put("brandId", brandId);
		List<Goods> goodsList = goodsBackMapper.listGoodsByCategoryAndBrand(param);
		return goodsList;
	}

	@Override
	public void itemAudit(Items item) {
		if (item.getStatus() == 2) {// 审核通过
			goodsBackMapper.updateItemStatusPass(item);// 更新item状态
			goodsBackMapper.updateSpecsTpCanBeUpShelf(item.getSpecsTpId());// 更新specsTp为下架状态（如果是初始化状态的话）
		}
		if (item.getStatus() == 11) {// 审核未通过
			goodsBackMapper.updateItemStatusUnPass(item);// 更新item状态
			int count = goodsMapper.countItemPassBySpecsTpId(item.getSpecsTpId());
			if (count == 0) {// 全部没有了需要下架，并把商品状态置为初始化
				GoodsSpecsTradePattern specsTp = goodsMapper.getGoodsSpecsTpBySpecsTpId(item.getSpecsTpId());
				if (specsTp.getStatus() == 2 || specsTp.getStatus() == 3) {// 上架状态需要先下架
					List<String> specsTpIdList = new ArrayList<>();
					specsTpIdList.add(item.getSpecsTpId());
					goodsService.downShelves(specsTpIdList, 2);
				}
				goodsBackMapper.updateSpecsTpInit(item.getSpecsTpId());
			}
		}

	}

	@Override
	public void welfareDisplay(String specsTpId, int welfare) {
		Map<String, Object> param = new HashMap<>();
		param.put("specsTpId", specsTpId);
		param.put("welfare", welfare);
		goodsBackMapper.updateWelfareDisplay(param);
	}

	@Override
	public List<PropertyEntity> listSpecsPropertyName(String thirdCategory) {
		List<PropertyEntity> list = goodsPropertyMapper.listSpecsPropertyNameByThirdCategory(thirdCategory);
		return list;
	}

	@Override
	public List<PropertyValueEntity> listSpecsPropertyValue(String nameId) {
		List<PropertyValueEntity> valueList = goodsPropertyMapper.listSpecsPropertyValueByNameId(nameId);
		return valueList;
	}

	@Override
	public List<PropertyEntity> listAllSpecsPropertyName() {
		List<PropertyEntity> list = goodsPropertyMapper.listAllSpecsPropertyName();
		return list;
	}

	@Override
	public List<GuidePropertyEntity> listGuidePropertyName(String thirdCategory) {
		List<GuidePropertyEntity> guideList = goodsPropertyMapper.listGuidePropertyNameByThirdCategory(thirdCategory);
		return guideList;
	}

	@Override
	public List<GuidePropertyValueEntity> listGuidePropertyValue(String nameId) {
		List<GuidePropertyValueEntity> valueList = goodsPropertyMapper.listGuidePropertyValueByNameId(nameId);
		return valueList;
	}

	@Override
	public List<GuidePropertyEntity> listAllGuidePropertyName() {
		List<GuidePropertyEntity> propertyList = goodsPropertyMapper.listAllGuidePropertyName();
		return propertyList;
	}

	@Override
	public void guidePropertyBind(List<GuidePropertyBindGoods> bindList) {
		goodsPropertyMapper.insertIntoGoodsGuidePropertyBindBatch(bindList);
	}

	@Override
	public Page<BackGoodsItemVO> listItems4Page(BackGoodsItemVO item) {
		PageHelper.startPage(item.getCurrentPage(), item.getNumPerPage(), true);
		Page<BackGoodsItemVO> page = goodsBackMapper.listItems4Page(item);
		return page;
	}

	@Override
	public Page<BackGoodsVO> listGoods4Page(BackGoodsVO item, Integer status) {
		PageHelper.startPage(item.getCurrentPage(), item.getNumPerPage(), true);
		Map<String,Object> param = new HashMap<>();
		param.put("item", item);
		param.put("status", status);
		Page<BackGoodsVO> page = goodsBackMapper.listGoods4Page(param);
		return page;
	}

	@Override
	public List<GoodsSpecsVO> listGoodsSpecsTp(String goodsId, Integer status) {
		Map<String,Object> param = new HashMap<>();
		param.put("goodsId", goodsId);
		param.put("status", status);
		List<GoodsSpecsVO> list = goodsBackMapper.listGoodsSpecsTpByGoodsIdAndStatus(param);
		return list;
	}

	@Override
	public List<BackItemsVO> listItemBySpecsTpId(String specsTpId) {
		List<BackItemsVO> list = goodsBackMapper.listItemBySpecsTpId(specsTpId);
		return list;
	}
}
