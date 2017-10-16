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
	List<GoodsFile> listGoodsFile(Integer goodsId);

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
	Map<String, Object> listGoodsSpecs(List<String> list, String centerId);

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
	Map<Object, Object> getModularData(String page, Layout layout, Integer centerId);

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
	List<Map<String, Object>> getEndActive();
}
