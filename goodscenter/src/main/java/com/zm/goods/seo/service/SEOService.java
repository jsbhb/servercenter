package com.zm.goods.seo.service;

import java.util.List;
import java.util.Map;

import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.po.BigSalesGoodsRecord;
import com.zm.goods.pojo.po.ComponentDataPO;
import com.zm.goods.pojo.po.PagePO;
import com.zm.goods.seo.model.SEODetail;

public interface SEOService {

	ResultModel navPublish();

	ResultModel indexPublish(Integer id);
	
	PagePO retrievePageData(Integer id);
	
	SEODetail convertToSEODetail(PagePO pagePo);

	/**
	 * 
	 * retrievePage:检索页面数据. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	PagePO retrievePage(Integer id) throws Exception;

	ResultModel getGoodsAccessPath(String goodsId, String itemId);

	/**
	 * @fun sitemap
	 * @param domains
	 * @return
	 */
	ResultModel addSitemap(List<String> domains);

	/**
	 * @fun sitemap
	 * @param domains
	 * @return
	 */
	ResultModel delsitemap(List<String> domains);

	void insertComponentDataBatch(List<ComponentDataPO> dataList);

	void deleteByIdList(List<Integer> idList);

	List<BigSalesGoodsRecord> listRecord(Map<String, Integer> param);
	/**
	 * @fun 根据specsTpIds 发布商品
	 * @param specsTpIdList
	 * @param centerId
	 * @param b
	 * @return
	 */
	ResultModel publishGoods(List<String> specsTpIdList);
	/**
	 * @fun 根据specsTpIds 删除发布的商品
	 * @param specsTpIdList
	 * @param centerId
	 * @return
	 */
	ResultModel delPublishGoods(List<String> specsTpIdList);
}
