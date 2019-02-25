package com.zm.goods.bussiness.service;

import java.util.List;
import java.util.Map;

import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.bo.AutoSelectionBO;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.po.Goods;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.GoodsVO;
import com.zm.goods.pojo.vo.SpecsTpStockVO;

public interface GoodsService {
	
	/**
	 * @fun 获取specsTp的stock
	 * @param version
	 * @param goodsId
	 * @return
	 */
	List<SpecsTpStockVO> getGoodsStock(String goodsId, Integer centerId);

	/**
	 * @fun 获取商品
	 * @param isApplet
	 *            是否是小程序端
	 * @return
	 */
	GoodsVO listGoods(String goodsId, String specsTpId, boolean isApplet);

	/**
	 * listGoodsSpecs:购物车接口获取规格信息（内部调用）.
	 * 
	 * @param list
	 * @param centerId
	 * @return
	 */
	List<GoodsVO> listGoodsSpecs(List<String> list, int platformSource, int gradeId)
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
	/**
	 * @fun 跨境商品单个上架
	 * @param bo
	 * @param centerId
	 * @param display
	 *            0:不显示;1:前端显示;2:后台显示;3:前后台都显示
	 * @return
	 */
	ResultModel signalKjGoodsUpShelves(AutoSelectionBO bo, Integer centerId, int display);
	/**
	 * @fun 跨境商品批量上架
	 * @param specsTpIdList
	 * @param centerId
	 * @param display
	 *            0:不显示;1:前端显示;2:后台显示;3:前后台都显示
	 * @return
	 */
	ResultModel batchKjGoodsUpShelves(List<String> specsTpIdList, Integer centerId, int display);
	/**
	 * @fun 商品下架
	 * @param specsTpIdList
	 * @param centerId
	 * @return
	 */
	ResultModel downShelves(List<String> specsTpIdList, Integer centerId);
	/**
	 * @fun 同步供应商库存
	 * @param itemIdList
	 * @return
	 */
	ResultModel syncStock(List<String> itemIdList);

	/**
	 * @fun 更新lucene索引
	 * @param updateTagList
	 * @param centerId
	 */
	void updateLuceneIndex(List<String> updateTagList, Integer centerId);

	List<Goods> listGoodsByGoodsIds(List<String> goodsIdList);
}
