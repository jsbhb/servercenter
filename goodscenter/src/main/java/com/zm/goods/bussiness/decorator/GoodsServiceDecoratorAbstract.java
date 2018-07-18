package com.zm.goods.bussiness.decorator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.pojo.Activity;
import com.zm.goods.pojo.GoodsConvert;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.PageModule;

/**
 * @fun 装饰器抽象类，把不需要装饰的方法在抽象类里默认实现
 * @author user
 *
 */
public abstract class GoodsServiceDecoratorAbstract implements GoodsService{

	
	@Override
	public abstract Object listGoods(Map<String, Object> param, Integer centerId, Integer userId, boolean proportion);
	
	@Override
	public abstract Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList, Pagination pagination);

	@Override
	public abstract Map<String, Object> listGoodsSpecs(List<String> list, Integer centerId, String source);
	
	@Override
	public List<GoodsFile> listGoodsCookFile(String goodsId) {
		return null;
	}

	@Override
	public Map<String, Object> tradeGoodsDetail(String itemId, Integer centerId) {
		return null;
	}

	@Override
	public ResultModel getPriceAndDelStock(List<OrderBussinessModel> list, Integer supplierId, boolean vip,
			Integer centerId, Integer orderFlag, String couponIds, Integer userId) {
		return null;
	}

	@Override
	public Activity getActivity(Map<String, Object> param) {
		return null;
	}

	@Override
	public List<Layout> getModular(String page, Integer centerId, Integer pageType) {
		return null;
	}

	@Override
	public List<PageModule> getModularData(Integer pageType, String page, Layout layout, Integer centerId) {
		return null;
	}

	@Override
	public void updateActiveStart(Integer centerId, Integer activeId) {
		
	}

	@Override
	public void updateActiveEnd(Integer centerId, Integer activeId) {
		
	}

	@Override
	public Map<String, Object> getEndActive() {
		return null;
	}

	@Override
	public void createGoodsLucene(Integer centerId) {
		
	}

	@Override
	public List<GoodsIndustryModel> loadIndexNavigation(Integer centerId) {
		return null;
	}

	@Override
	public void stockBack(List<OrderBussinessModel> list, Integer orderFlag) {
		
	}

	@Override
	public ResultModel stockJudge(List<OrderBussinessModel> list, Integer orderFlag, Integer supplierId) {
		return null;
	}

	@Override
	public boolean updateThirdWarehouseStock(List<WarehouseStock> list) {
		return false;
	}

	@Override
	public boolean saveThirdGoods(List<ThirdWarehouseGoods> list) {
		return false;
	}

	@Override
	public Double getCostPrice(List<OrderBussinessModel> list) {
		return null;
	}

	@Override
	public List<OrderBussinessModel> checkStock() {
		return null;
	}

	@Override
	public ResultModel upShelves(List<String> itemIdList, Integer centerId) {
		return null;
	}

	@Override
	public ResultModel downShelves(List<String> itemIdList, Integer centerId) {
		return null;
	}

	@Override
	public ResultModel unDistribution(List<String> itemIdList) {
		return null;
	}

	@Override
	public ResultModel syncStock(List<String> itemIdList) {
		return null;
	}

	@Override
	public ResultModel delButtjoinOrderStock(List<OrderBussinessModel> list, Integer supplierId, Integer orderFlag) {
		return null;
	}
	
	@Override
	public Map<String, GoodsConvert> listSkuAndConversionByItemId(Set<String> set){
		return null;
	}
	
	@Override
	public ResultModel calStock(List<OrderBussinessModel> list, Integer supplierId, Integer orderFlag){
		return null;
	}

}
