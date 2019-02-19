package com.zm.goods.bussiness.service;

import java.util.List;
import java.util.Map;

import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.po.GoodsItem;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.GoodsVO;

public interface GoodsService {

	/**
	 * @fun 获取商品
	 * @param isApplet
	 *            是否是小程序端
	 * @return
	 */
	GoodsVO listGoods(String goodsId, String specsTpId, boolean isApplet);

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
	 * listGoodsSpecs:获取规格信息.
	 * 
	 * @param list
	 * @param centerId
	 * @return
	 */
	Map<String, Object> listGoodsSpecs(List<String> list, int platformSource, int gradeId)
			throws WrongPlatformSource;

	/**
	 * queryMember:商城搜索lucene. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList, Pagination pagination, int gradeId,
			boolean welfare) throws WrongPlatformSource;

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
	 * @fun 一般贸易商品上架
	 * @param specsTpIdList
	 * @param centerId
	 * @param display
	 *            0:不显示;1:前端显示;2:后台显示;3:前后台都显示
	 * @return
	 */
	ResultModel tradeGoodsUpShelves(List<String> specsTpIdList, Integer centerId, int display);

	ResultModel downShelves(List<String> itemIdList, Integer centerId);

	ResultModel syncStock(List<String> itemIdList);

	/**
	 * @fun 更新lucene索引
	 * @param updateTagList
	 * @param centerId
	 */
	void updateLuceneIndex(List<String> updateTagList, Integer centerId);

	List<GoodsItem> listGoodsByGoodsIds(List<String> goodsIdList);

}
