package com.zm.goods.seo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.dao.SEOMapper;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.enummodel.PublishType;
import com.zm.goods.enummodel.SystemEnum;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.feignclient.UserFeignClient;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.po.BigSalesGoodsRecord;
import com.zm.goods.pojo.po.ComponentDataPO;
import com.zm.goods.pojo.po.PagePO;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.GoodsVO;
import com.zm.goods.seo.model.CategoryPath;
import com.zm.goods.seo.model.SEOBase;
import com.zm.goods.seo.model.SEODetail;
import com.zm.goods.seo.model.SEOModel;
import com.zm.goods.seo.model.SEONavigation;
import com.zm.goods.seo.publish.PublishComponent;
import com.zm.goods.seo.service.SEOService;
import com.zm.goods.utils.JSONUtil;

@Service("seoService")
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class SEOServiceImpl implements SEOService {

	@Resource
	SEOMapper seoMapper;

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	GoodsTagMapper goodsTagMapper;

	@Resource
	GoodsService goodsTagDecorator;

	private static Integer CNCOOPBUY_ID = 2;
	private static String HTTP_STR = "http";

	private boolean publishAndHandle(StringBuilder sb, ResultModel result, SEOBase base, PublishType publishType) {
		ResultModel temp = PublishComponent.publish(JSONUtil.toJson(base), publishType);
		if (!temp.isSuccess()) {
			result.setSuccess(false);
			if (base instanceof SEODetail) {
				SEODetail tem = (SEODetail) base;
				sb.append(tem.getFile() + ":");
			}
			sb.append(temp.getErrorMsg());
		}
		return temp.isSuccess();
	}

	@Override
	public ResultModel navPublish() {
		ResultModel result = new ResultModel(true, "");
		StringBuilder sb = new StringBuilder();
		List<GoodsIndustryModel> list = goodsMapper.queryGoodsCategory();
		SEONavigation seoNav = new SEONavigation("nav-1", SystemEnum.PCMALL, list);
		publishAndHandle(sb, result, seoNav, PublishType.MODULE_CREATE);
		// seoNav = new SEONavigation("nav-1", SystemEnum.MPMALL, list);
		// publishAndHandle(sb, result, seoNav, PublishType.MODULE_CREATE);
		if (sb.length() > 0) {
			sb.append("发布失败");
			result.setErrorMsg(sb.toString());
		}
		return result;
	}

	@Override
	public ResultModel indexPublish(Integer id) {

		// 检索数据
		PagePO pagePo = retrievePageData(id);

		// 数据转化为接口结构
		SEODetail seoDetail = convertToSEODetail(pagePo);

		// 发布并操作返回结构
		StringBuilder sb = new StringBuilder();
		ResultModel result = new ResultModel(true, "");
		publishAndHandle(sb, result, seoDetail, PublishType.PAGE_CREATE);
		if (sb.length() > 0) {
			sb.append("发布失败");
			result.setErrorMsg(sb.toString());
		} else {
			seoMapper.updatePagePublishToSave(pagePo);
			seoMapper.updatePageSaveToPublish(id);
		}
		return result;
	}

	/**
	 * convertToSEODetail:数据转化为接口结构. <br/>
	 * 
	 * @author hebin
	 * @param pagePo
	 * @return
	 * @since JDK 1.7
	 */
	@Override
	public SEODetail convertToSEODetail(PagePO pagePo) {
		List<PagePO> pageList = new ArrayList<PagePO>();
		pageList.add(pagePo);
		SEODetail seoDetail = new SEODetail(null, pagePo.getsEOModel(), null, null,
				SystemEnum.getSystem(pagePo.getClient()), pageList);
		if (pagePo.getGradeId() != CNCOOPBUY_ID) {// 主站不需要
			String url = userFeignClient.getClientUrl(pagePo.getGradeId(), Constants.FIRST_VERSION);
			String region;
			if (url.startsWith(HTTP_STR)) {
				region = url.substring(url.indexOf("//") + 2, url.indexOf("."));
			} else {
				region = url.substring(0, url.indexOf("."));
			}
			seoDetail.setRegion(region);
		}
		return seoDetail;
	}

	/**
	 * retrievePageData:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	@Override
	public PagePO retrievePageData(Integer id) {
		PagePO page = seoMapper.getPageById(id);
		page.setsEOModel(seoMapper.getPageSEO(id));
		return page;
	}

	@Override
	public PagePO retrievePage(Integer id) throws Exception {
		PagePO page = seoMapper.getPageById(id);

		if (page == null) {
			throw new Exception("no page info!");
		}

		page.setsEOModel(seoMapper.getPageSEO(id));
		return page;
	}

	@Override
	public ResultModel getGoodsAccessPath(String goodsId, String specsTpId) {
		ResultModel result = new ResultModel();
		if (goodsId != null && !"".equals(goodsId)) {
			String path = seoMapper.getGoodsAccessPath(goodsId);
			result.setSuccess(true);
			result.setObj(path);
		} else if (specsTpId != null && !"".equals(specsTpId)) {
			goodsId = seoMapper.getGoodsIdByItemId(specsTpId);
			String path = seoMapper.getGoodsAccessPath(goodsId);
			result.setSuccess(true);
			result.setObj(path);
		} else {
			result.setSuccess(false);
			result.setErrorMsg("没有商品编号");
		}
		return result;
	}

	@Override
	public ResultModel publishGoods(List<String> specsTpIdList) {

		// 检索手机端及商场的页面组建数据
		List<PagePO> pageList = seoMapper.getGoodsDetailPageByPublish();
		if (pageList == null || pageList.size() != 2) {
			return new ResultModel(false, "请确认手机和pc商详模板是否都存在");
		}

		// 数据转化
		StringBuilder sb = new StringBuilder();
		ResultModel result = new ResultModel(true, "");
		SEODetail seoGoodsDetail;
		List<GoodsVO> voList = null;
		try {
			voList = goodsTagDecorator.listGoodsSpecs(specsTpIdList, 99, 0);
		} catch (WrongPlatformSource e) {
			e.printStackTrace();
		}
		if (voList != null && voList.size() > 0) {
			Map<String, Object> param = new HashMap<String, Object>();
			List<String> goodsIdList = voList.stream().map(vo -> vo.getGoodsId()).collect(Collectors.toList());
			// 获取seo信息
			List<SEOModel> seoModelList = seoMapper.listGoodsSEO(goodsIdList);
			Map<String, SEOModel> seoParam = new HashMap<String, SEOModel>();
			if (seoModelList != null && seoModelList.size() > 0) {
				for (SEOModel seoModel : seoModelList) {
					seoParam.put(seoModel.getGoodsId(), seoModel);
				}
			}
			// 获取商品路径
			List<String> thirdIdList = new ArrayList<String>();
			Map<String, String> pathParam = new HashMap<String, String>();
			for (GoodsVO item : voList) {
				thirdIdList.add(item.getThirdCategory());
			}
			List<CategoryPath> pathList = seoMapper.queryGoodsCategoryPath(thirdIdList);
			for (CategoryPath categoryPath : pathList) {
				pathParam.put(categoryPath.getThirdId(), categoryPath.getPath());
			}
			// 开始处理商品
			for (GoodsVO goods : voList) {
				String goodsId = goods.getGoodsId();
				String path = pathParam.get(goods.getThirdCategory());
				String href = "/" + path + "/" + goods.getGoodsId() + ".html";
				template.opsForValue().set("href:" + goodsId, href);
				goods.setHref(href);
				SEOModel seoModel = seoParam.get(goodsId);
				seoGoodsDetail = new SEODetail(goods, seoModel, goodsId + ".html", path, SystemEnum.PCMALL, pageList);

				publishAndHandle(sb, result, seoGoodsDetail, PublishType.PAGE_CREATE);
				seoGoodsDetail = new SEODetail(goods, seoModel, goodsId + ".html", path, SystemEnum.FMPMALL, pageList);
				publishAndHandle(sb, result, seoGoodsDetail, PublishType.PAGE_CREATE);
				param.put(goodsId, path);
			}
			if (param.size() > 0) {
				seoMapper.updateGoodsAccessPath(param);
			}
		}
		return result;
	}

	@Override
	public ResultModel delPublishGoods(List<String> specsTpIdList) {
		ResultModel result = new ResultModel(true, "");
		// 检索手机端及商场的页面组建数据
		List<PagePO> pageList = seoMapper.getGoodsDetailPageByPublish();
		if (pageList == null || pageList.size() != 2) {
			return new ResultModel(false, "请确认手机和pc商详模板是否都存在");
		}
		List<GoodsVO> voList = null;
		try {
			voList = goodsTagDecorator.listGoodsSpecs(specsTpIdList, 99, 0);
		} catch (WrongPlatformSource e) {
			e.printStackTrace();
		}
		if (voList != null && voList.size() > 0) {
			SEODetail seoGoodsDetail = null;
			StringBuilder sb = new StringBuilder();
			for (GoodsVO vo : voList) {
				String path = seoMapper.getGoodsAccessPath(vo.getGoodsId());
				seoGoodsDetail = new SEODetail(vo, null, vo.getGoodsId() + ".html", path, SystemEnum.PCMALL, pageList);
				publishAndHandle(sb, result, seoGoodsDetail, PublishType.PAGE_DELETE);
				seoGoodsDetail = new SEODetail(vo, null, vo.getGoodsId() + ".html", path, SystemEnum.FMPMALL, pageList);
				publishAndHandle(sb, result, seoGoodsDetail, PublishType.PAGE_DELETE);
			}
		}
		return result;
	}

	@Override
	public ResultModel addSitemap(List<String> domains) {
		return sitemapHandler(domains, PublishType.ADD_SITEMAP);
	}

	@Override
	public ResultModel delsitemap(List<String> domains) {
		return sitemapHandler(domains, PublishType.DEL_SITEMAP);
	}

	private ResultModel sitemapHandler(List<String> domains, PublishType type) {
		if (domains == null || domains.size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		Map<String, String> param = new HashMap<String, String>();
		for (String domain : domains) {
			param.put("domain", domain);
			PublishComponent.publish(JSONUtil.toJson(param), type);
		}
		return new ResultModel(true, "");
	}

	@Override
	public void insertComponentDataBatch(List<ComponentDataPO> dataList) {
		seoMapper.insertComponentDataBatch(dataList);
	}

	@Override
	public void deleteByIdList(List<Integer> idList) {
		seoMapper.deleteByIdList(idList);
	}

	@Override
	public List<BigSalesGoodsRecord> listRecord(Map<String, Integer> param) {
		return seoMapper.listRecord(param);
	}
}
