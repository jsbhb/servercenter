package com.zm.goods.bussiness.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zm.goods.bussiness.component.base.IindexAutoConfig;
import com.zm.goods.bussiness.service.GoodsBackService;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.SystemEnum;
import com.zm.goods.feignclient.UserFeignClient;
import com.zm.goods.feignclient.model.RebateFormula;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsRebateEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.RebateFormulaBO;
import com.zm.goods.pojo.po.BigSalesGoodsRecord;
import com.zm.goods.pojo.po.ComponentDataPO;
import com.zm.goods.pojo.po.ComponentPagePO;
import com.zm.goods.seo.service.SEOService;
import com.zm.goods.utils.FormulaUtil;
import com.zm.goods.utils.JSONUtil;
import com.zm.goods.utils.SpringContextUtil;

public class BigSalesPerWeekModule implements IindexAutoConfig {

	@Override
	public boolean autoConfig(ComponentPagePO cp, int week, String client) {
		SEOService seoService = (SEOService) SpringContextUtil.getBean("seoService");
		// 获取本周特卖商品
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int year = Integer.valueOf(sdf.format(new Date()));
		Map<String, Integer> param = new HashMap<>();
		param.put("year", year);
		param.put("week", week);
		List<BigSalesGoodsRecord> newRecordList = seoService.listRecord(param);
		if (newRecordList == null || newRecordList.size() == 0) {
			LogUtil.writeLog("本周没有特卖商品，沿用上周");
			return true;
		}
		// 更新本周价格、返佣比例、首页数据、上下架/根据client发布首页
		updateNewDate(newRecordList, client, cp, seoService);
		// 获取上一周的
		if (week == 1) {// 第一周
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			int oldYear = year - 1;
			Date date = null;
			try {
				date = sdf.parse(oldYear + "-12-31");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar c = Calendar.getInstance();
			c.setFirstDayOfWeek(Calendar.MONDAY);
			c.setTime(date);
			int oldWeek = c.get(Calendar.WEEK_OF_YEAR);
			param.put("year", oldYear);
			param.put("week", oldWeek);
		} else {
			param.put("year", year);
			param.put("week", week - 1);
		}
		List<BigSalesGoodsRecord> oldRecordList = seoService.listRecord(param);
		if (oldRecordList != null && oldRecordList.size() > 0) {
			// 更新上周价格、返佣比例、上下架
			updateOldDate(oldRecordList, client, cp, seoService);
		}
		return true;
	}

	private void updateOldDate(List<BigSalesGoodsRecord> oldRecordList, String client, ComponentPagePO cp,
			SEOService seoService) {

		updateGoodsData(oldRecordList, false);
		// 上下架
		GoodsService goodsService = (GoodsService) SpringContextUtil.getBean("goodsService");
		List<String> itemIdList = oldRecordList.stream().map(record -> record.getItemId()).collect(Collectors.toList());
		goodsService.downShelves(itemIdList, 2);
		goodsService.upShelves(itemIdList, 2);
	}

	@SuppressWarnings("unchecked")
	private void updateNewDate(List<BigSalesGoodsRecord> newRecordList, String client, ComponentPagePO cp,
			SEOService seoService) {
		// 更新商品数据
		updateGoodsData(newRecordList, true);
		// 更新模块数据
		List<ComponentDataPO> dataList = cp.getList();
		if (dataList != null && dataList.size() > 0) {
			List<Integer> idList = dataList.stream().map(po -> po.getId()).collect(Collectors.toList());
			seoService.deleteByIdList(idList);
		}
		GoodsService goodsService = (GoodsService) SpringContextUtil.getBean("goodsService");
		List<String> goodsIdList = newRecordList.stream().map(record -> record.getGoodsId())
				.collect(Collectors.toList());
		List<GoodsItem> goodsList = goodsService.listGoodsByGoodsIds(goodsIdList);
		Map<String, GoodsItem> goodsMap = goodsList.stream()
				.collect(Collectors.toMap(GoodsItem::getGoodsId, goodsItem -> goodsItem));
		ComponentDataPO data = null;
		dataList = new ArrayList<>();
		GoodsItem item = null;
		for (BigSalesGoodsRecord record : newRecordList) {
			item = goodsMap.get(record.getGoodsId());
			data = new ComponentDataPO();
			data.setCpId(cp.getId());
			data.setEnname(record.getLinePrice() + "");
			data.setGoodsId(record.getGoodsId());
			data.setTitle(item.getCustomGoodsName());
			data.setGoodsType(item.getType());
			data.setOrigin(item.getOrigin());
			data.setPicPath(record.getPicPath());
			data.setPrice(record.getNewRetailPrice());
			data.setType(1);
			data.setSort(record.getSort());
			switch (SystemEnum.getSystem(client)) {
			case MPMALL:
				data.setHref("/" + item.getAccessPath() + "/" + record.getGoodsId() + ".html");
				break;
			case PCMALL:
				data.setHref("/" + item.getAccessPath() + "/" + record.getGoodsId() + ".html");
				break;
			case APPLET:
				data.setHref("/web/goodsDetail/goodsDetail?goodsId=" + record.getGoodsId());
				break;
			default:
				break;
			}
			dataList.add(data);
		}
		seoService.insertComponentDataBatch(dataList);
		// 根据client发布
		switch (SystemEnum.getSystem(client)) {
		case MPMALL:
			seoService.indexPublish(cp.getPageId());
			break;
		case PCMALL:
			seoService.indexPublish(cp.getPageId());
			break;
		default:
			break;
		}
		// 放入redis活动list
		List<String> itemIdList = newRecordList.stream().map(record -> record.getItemId()).collect(Collectors.toList());
		RedisTemplate<String, String> template = (RedisTemplate<String, String>) SpringContextUtil
				.getBean("stringRedisTemplate");
		template.delete(Constants.BIG_SALES_PRE);
		template.opsForValue().set(Constants.BIG_SALES_PRE, JSONUtil.toJson(itemIdList));
		// 上下架
		goodsService.downShelves(itemIdList, 2);
		goodsService.upShelves(itemIdList, 2);
	}

	private void updateGoodsData(List<BigSalesGoodsRecord> recordList, boolean isNew) {
		GoodsBackService goodsBackService = (GoodsBackService) SpringContextUtil.getBean("goodsBackServiceImpl");
		// 更新零售价
		Map<String, Object> param = recordList.stream().collect(Collectors.toMap(BigSalesGoodsRecord::getItemId,
				record -> isNew ? record.getNewRetailPrice() : record.getOldRetailPrice()));
		goodsBackService.updateRetailPrice(param);
		// 更新返佣
		List<GoodsRebateEntity> entityList = new ArrayList<>();
		UserFeignClient userFeignClient = (UserFeignClient) SpringContextUtil
				.getBean("com.zm.goods.feignclient.UserFeignClient");
		ResultModel result = userFeignClient.listGradeTypeRebateFormula(Constants.FIRST_VERSION, false,
				new RebateFormula());
		if (!result.isSuccess()) {
			throw new RuntimeException("获取分级返佣公式出错");
		}
		String json = JSONUtil.toJson(result.getObj());
		List<RebateFormulaBO> list = null;
		if (json != null) {
			list = JSONUtil.parse(json, new TypeReference<List<RebateFormulaBO>>() {
			});
		}
		GoodsRebateEntity entity = null;
		try {
			for (BigSalesGoodsRecord record : recordList) {
				if (list != null) {
					configAreaCenterRebate(entityList, record, isNew);
					for (RebateFormulaBO bo : list) {
						entity = new GoodsRebateEntity();
						entity.setItemId(record.getItemId());
						entity.setProportion(FormulaUtil.calRebate(bo.getFormula(),
								isNew ? record.getNewRebate() : record.getOldRebate()));
						entity.setGradeType(bo.getGradeTypeId());
						entityList.add(entity);
					}
				} else {
					configAreaCenterRebate(entityList, record, isNew);
				}
			}
			goodsBackService.insertGoodsRebate(entityList);// 更新返佣
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("计算返佣出错");
		}
	}

	private void configAreaCenterRebate(List<GoodsRebateEntity> entityList, BigSalesGoodsRecord record, boolean isNew) {
		GoodsRebateEntity entity;
		entity = new GoodsRebateEntity();
		entity.setItemId(record.getItemId());
		entity.setProportion(isNew ? record.getNewRebate() : record.getOldRebate());
		entity.setGradeType(2);// 区域中心
		entityList.add(entity);
	}
}
