package com.zm.goods.bussiness.service;

import java.util.List;
import java.util.Map;

import com.zm.goods.pojo.Activity;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.PageModule;

public interface GoodsService {

	/**
	 * listBigTradeGoods:获取商品. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	List<GoodsItem> listGoods(Map<String, Object> param);

	/**
	 * listBigTradeGoods:获取大贸海蒸鲜商品比价信息. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	List<PriceContrast> listPriceContrast(Map<String, Object> param);

	/**
	 * listBigTradeGoods:获取大贸海蒸鲜商品菜谱. <br/>
	 * 
	 * @author wqy
	 * @param goodsId
	 * @return
	 * @since JDK 1.7
	 */
	List<GoodsFile> listGoodsCookFile(String goodsId);

	/**
	 * listBigTradeGoods:获取大贸海蒸鲜商品购买也信息. <br/>
	 * 
	 * @author wqy
	 * @param itemId
	 * @return
	 * @since JDK 1.7
	 */
	Map<String, Object> tradeGoodsDetail(String itemId, Integer centerId);

	/**
	 * getPriceAndDelStock:处理订单信息. <br/>
	 * 
	 * @author wqy
	 * @param list，delStock
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel getPriceAndDelStock(List<OrderBussinessModel> list, boolean delStock, boolean vip, Integer centerId,
			Integer orderFlag);

	/**
	 * listGoodsSpecs:获取规格信息. <br/>
	 * 
	 * @author wqy
	 * @param list
	 * @return
	 * @since JDK 1.7
	 */
	Map<String, Object> listGoodsSpecs(List<String> list, Integer centerId);

	/**
	 * getActivity:获取活动信息. <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	Activity getActivity(Map<String, Object> param);

	/**
	 * getModular:获取模块数据. <br/>
	 * 
	 * @author wqy
	 * @param centerID,page
	 * @return
	 * @since JDK 1.7
	 */
	List<Layout> getModular(String page, Integer centerId);

	/**
	 * createTable:创建区域中心数据表. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void createTable(Integer centerId);

	/**
	 * getModularData:获取模块数据. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	List<PageModule> getModularData(String page, Layout layout, Integer centerId);

	/**
	 * updateActiveStart:开始活动. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void updateActiveStart(Integer centerId, Integer activeId);
	
	/**
	 * updateActiveEnd:结束活动. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void updateActiveEnd(Integer centerId, Integer activeId);

	/**
	 * getEndActive:获取已经结束的活动. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	Map<String, Object> getEndActive();
	
	/**
	 * createGoodsLucene:创建商品lucene索引数据. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void createGoodsLucene(Integer centerId);

	/**
	 * queryMember:商城搜索lucene. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList, Pagination pagination);

	/**
	 * loadIndexNavigation:获取导航栏. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	List<GoodsIndustryModel> loadIndexNavigation(Integer centerId);

	/**
	 * stockBack:库存回滚. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void stockBack(List<OrderBussinessModel> list, Integer centerId, Integer orderFlag);
}
