package com.zm.goods.bussiness.decorator;

import java.util.List;
import java.util.Map;

import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.po.GoodsItem;
import com.zm.goods.pojo.vo.GoodsIndustryModel;

/**
 * @fun 装饰器抽象类，把不需要装饰的方法在抽象类里默认实现
 * @author user
 *
 */
public abstract class GoodsServiceDecoratorAbstract implements GoodsService {


	@Override
	public abstract Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList,
			Pagination pagination, int gradeId, boolean welfare) throws WrongPlatformSource;

	@Override
	public abstract Map<String, Object> listGoodsSpecs(List<String> list, int platformSource,
			int gradeId) throws WrongPlatformSource;

	@Override
	public List<GoodsFile> listGoodsCookFile(String goodsId) {
		return null;
	}

	@Override
	public void updateLuceneIndex(List<String> updateTagList, Integer centerId) {

	}

	@Override
	public List<GoodsIndustryModel> loadIndexNavigation(Integer centerId) {
		return null;
	}

	@Override
	public ResultModel tradeGoodsUpShelves(List<String> specsTpIdList, Integer centerId, int display){
		return null;
	}

	@Override
	public ResultModel downShelves(List<String> itemIdList, Integer centerId) {
		return null;
	}

	@Override
	public ResultModel syncStock(List<String> itemIdList) {
		return null;
	}

	@Override
	public List<GoodsItem> listGoodsByGoodsIds(List<String> goodsIdList) {
		return null;
	}

}
