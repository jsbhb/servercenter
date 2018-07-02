package com.zm.goods.seo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.component.GoodsServiceUtil;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.dao.SEOMapper;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.PublishType;
import com.zm.goods.enummodel.SystemEnum;
import com.zm.goods.feignclient.UserFeignClient;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.ItemStockBO;
import com.zm.goods.pojo.po.PagePO;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.seo.model.SEOBase;
import com.zm.goods.seo.model.SEODetail;
import com.zm.goods.seo.model.SEOGoodsDel;
import com.zm.goods.seo.model.SEOModel;
import com.zm.goods.seo.model.SEONavigation;
import com.zm.goods.seo.publish.PublishComponent;
import com.zm.goods.seo.service.SEOService;
import com.zm.goods.utils.JSONUtil;

@Service("seoService")
public class SEOServiceImpl implements SEOService {

	@Resource
	SEOMapper seoMapper;

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	UserFeignClient userFeignClient;

	@Override
	public List<ItemStockBO> getGoodsStock(String goodsId, Integer centerId) {
		List<String> itemIds = seoMapper.listItemIdsByGoodsId(goodsId);
		if (itemIds != null && itemIds.size() > 0) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("list", itemIds);
			param.put("centerId", GoodsServiceUtil.judgeCenterId(centerId));
			return seoMapper.listStockByItemIds(param);
		}
		return null;
	}

	@Override
	public ResultModel publish(List<String> itemIdList, Integer centerId) {
		String centerIdStr = GoodsServiceUtil.judgeCenterId(centerId);
		List<PagePO> pageList = seoMapper.getGoodsDetailPageByPublish();
		if (pageList == null || pageList.size() != 2) {
			return new ResultModel(false, "请确认手机和pc商详模板是否都存在");
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", itemIdList);
		param.put("centerId", centerIdStr);
		List<String> goodsIdList = goodsMapper.getGoodsIdByItemId(param);
		StringBuilder sb = new StringBuilder();
		ResultModel result = new ResultModel(true, "");
		SEODetail seoGoodsDetail;
		if (goodsIdList != null && goodsIdList.size() > 0) {
			for (String goodsId : goodsIdList) {
				param.put("goodsId", goodsId);
				GoodsItem goodsItem = getGoods(param);
				if (goodsItem == null) {
					continue;
				}
				String path = seoMapper.queryGoodsCategoryPath(goodsItem.getThirdCategory());
				param.put("accessPath", path);
				SEOModel seoModel = seoMapper.getGoodsSEO(goodsId);
				seoGoodsDetail = new SEODetail(goodsItem, seoModel, goodsId + ".html", path, SystemEnum.PCMALL,
						pageList);
				publishAndHandle(sb, result, seoGoodsDetail, PublishType.TEST_PAGE_CREATE);
				seoGoodsDetail = new SEODetail(goodsItem, seoModel, goodsId + ".html", path, SystemEnum.MPMALL,
						pageList);
				publishAndHandle(sb, result, seoGoodsDetail, PublishType.TEST_PAGE_CREATE);
				seoMapper.updateGoodsAccessPath(param);
			}
			if (sb.length() > 0) {
				sb.append("发布失败");
				result.setErrorMsg(sb.toString());
			}
		}
		return result;
	}

	private void publishAndHandle(StringBuilder sb, ResultModel result, SEOBase base, PublishType publishType) {
		ResultModel temp = PublishComponent.publish(JSONUtil.toJson(base), publishType);
		if (!temp.isSuccess() && SystemEnum.PCMALL.getName().equals(base.getSystem())) {
			result.setSuccess(false);
			sb.append("PC端:" + temp.getErrorMsg() + ",");
		}

		if (!temp.isSuccess() && SystemEnum.MPMALL.getName().equals(base.getSystem())) {
			result.setSuccess(false);
			sb.append("H5端:" + temp.getErrorMsg() + ",");
		}
	}

	private GoodsItem getGoods(Map<String, Object> param) {
		GoodsItem goodsItem = seoMapper.getGoods(param);
		if (goodsItem == null) {
			return null;
		}
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> map = hashOperations.entries(Constants.POST_TAX + goodsItem.getSupplierId());
		if (map != null) {
			String post = map.get("post");
			String tax = map.get("tax");
			try {
				goodsItem.setFreePost(Integer.valueOf(post == null ? "0" : post));
				goodsItem.setFreeTax(Integer.valueOf(tax == null ? "0" : tax));
			} catch (Exception e) {
				LogUtil.writeErrorLog("【数字转换出错】" + post + "," + tax);
			}
		}
		return goodsItem;
	}

	@Override
	public ResultModel navPublish() {
		ResultModel result = new ResultModel(true, "");
		StringBuilder sb = new StringBuilder();
		List<GoodsIndustryModel> list = goodsMapper.queryGoodsCategory(null);
		SEONavigation seoNav = new SEONavigation("nav-1", SystemEnum.PCMALL, list);
		publishAndHandle(sb, result, seoNav, PublishType.TEST_MODULE_CREATE);
		seoNav = new SEONavigation("nav-1", SystemEnum.MPMALL, list);
		publishAndHandle(sb, result, seoNav, PublishType.TEST_MODULE_CREATE);
		if (sb.length() > 0) {
			sb.append("发布失败");
			result.setErrorMsg(sb.toString());
		}
		return result;
	}

	@Override
	public ResultModel delPublish(List<String> itemIdList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", itemIdList);
		ResultModel result = new ResultModel(true, "");
		List<String> goodsIdList = goodsMapper.getGoodsIdByItemId(param);
		StringBuilder sb = new StringBuilder();
		if (goodsIdList != null && goodsIdList.size() > 0) {
			SEOGoodsDel seoGoodsDel = null;
			for (String goodsId : goodsIdList) {
				String path = seoMapper.getGoodsAccessPath(goodsId);
				seoGoodsDel = new SEOGoodsDel(path, SystemEnum.PCMALL, goodsId + ".html");
				publishAndHandle(sb, result, seoGoodsDel, PublishType.TEST_PAGE_DELETE);
				seoGoodsDel = new SEOGoodsDel(path, SystemEnum.MPMALL, goodsId + ".html");
				publishAndHandle(sb, result, seoGoodsDel, PublishType.TEST_PAGE_DELETE);
			}
		}
		if (sb.length() > 0) {
			sb.append("发布失败");
			result.setErrorMsg(sb.toString());
		}
		return result;
	}

	@Override
	public ResultModel indexPublish(Integer id) {
		PagePO page = seoMapper.getPageById(id);
		SEOModel seoModel = seoMapper.getPageSEO(id);
		List<PagePO> pageList = new ArrayList<PagePO>();
		ResultModel result = new ResultModel(true, "");
		StringBuilder sb = new StringBuilder();
		String url = userFeignClient.getClientUrl(page.getGradeId(), Constants.FIRST_VERSION);
		String region;
		if (url.startsWith("http")) {
			region = url.substring(url.indexOf("//") + 2, url.indexOf("."));
		} else {
			region = url.substring(0, url.indexOf("."));
		}
		pageList.add(page);
		SEODetail seoDetail = new SEODetail(null, seoModel, null, null, SystemEnum.getSystem(page.getClient()),
				pageList);
		seoDetail.setRegion(region);
		publishAndHandle(sb, result, seoDetail, PublishType.TEST_PAGE_CREATE);
		if (sb.length() > 0) {
			sb.append("发布失败");
			result.setErrorMsg(sb.toString());
		} else {
			seoMapper.updatePagePublishToSave(page);
			seoMapper.updatePageSaveToPublish(id);
		}
		return result;
	}

	@Override
	public ResultModel getGoodsAccessPath(String goodsId, String itemId) {
		ResultModel result = new ResultModel();
		if(goodsId != null && !"".equals(goodsId)){
			String path = seoMapper.getGoodsAccessPath(goodsId);
			result.setSuccess(true);
			result.setObj(path);
		} else if(itemId != null && !"".equals(itemId)){
			goodsId = seoMapper.getGoodsIdByItemId(itemId);
			String path = seoMapper.getGoodsAccessPath(goodsId);
			result.setSuccess(true);
			result.setObj(path);
		} else {
			result.setSuccess(false);
			result.setErrorMsg("没有商品编号");
		}
		return result;
	}

}
