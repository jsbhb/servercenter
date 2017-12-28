package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.goods.bussiness.component.ActivityComponent;
import com.zm.goods.bussiness.component.GoodsServiceUtil;
import com.zm.goods.bussiness.component.PriceComponent;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.feignclient.SupplierFeignClient;
import com.zm.goods.feignclient.UserFeignClient;
import com.zm.goods.pojo.Activity;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.PopularizeDict;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.PriceModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.Tax;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.PageModule;
import com.zm.goods.pojo.vo.TimeLimitActive;
import com.zm.goods.pojo.vo.TimeLimitActiveData;
import com.zm.goods.processWarehouse.ProcessWarehouse;
import com.zm.goods.utils.CalculationUtils;
import com.zm.goods.utils.lucene.AbstractLucene;
import com.zm.goods.utils.lucene.LuceneFactory;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	private static final Integer PICTURE_TYPE = 0;

	private static final Integer COOK_BOOK_TYPE = 1;

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	ProcessWarehouse processWarehouse;

	@Resource
	SupplierFeignClient supplierFeignClient;

	@Resource
	ActivityComponent activityComponent;
	
	@Resource
	PriceComponent priceComponent;

	@Override
	public List<GoodsItem> listGoods(Map<String, Object> param, Integer centerId, Integer userId) {

		if (param.get("itemId") != null) {
			String goodsId = goodsMapper.getGoodsIdByItemId((String) param.get("itemId"));
			param.put("goodsId", goodsId);
		}

		List<GoodsItem> goodsList = goodsMapper.listGoods(param);

		List<String> idList = new ArrayList<String>();

		if (goodsList == null || goodsList.size() == 0) {
			return null;
		}

		for (GoodsItem item : goodsList) {
			idList.add(item.getGoodsId());
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);
		parameter.put("centerId", param.get("centerId"));
		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);
		if (param.get("goodsId") != null) {
			parameter.put("goodsType", goodsList.get(0).getType());
			List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(parameter);
			GoodsServiceUtil.packageGoodsItem(goodsList, fileList, specsList, true);
			activityComponent.doPackCoupon(centerId, userId, goodsList);
		} else {
			List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(parameter);
			GoodsServiceUtil.packageGoodsItem(goodsList, fileList, specsList, false);
		}

		return goodsList;
	}

	@Override
	public List<PriceContrast> listPriceContrast(Map<String, Object> param) {

		return goodsMapper.listPriceContrast(param);
	}

	@Override
	public List<GoodsFile> listGoodsCookFile(String goodsId) {

		List<String> idList = new ArrayList<String>();
		idList.add(goodsId);
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", COOK_BOOK_TYPE);

		return goodsMapper.listGoodsFile(parameter);
	}

	@Override
	public Map<String, Object> tradeGoodsDetail(String itemId, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();

		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("itemId", itemId);
		GoodsSpecs specs = goodsMapper.getGoodsSpecs(param);
		if (specs == null) {
			return null;
		}
		GoodsServiceUtil.getPriceInterval(specs, specs.getDiscount());

		List<String> idList = new ArrayList<String>();
		idList.add(specs.getGoodsId());
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);

		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("specs", specs);
		result.put("pic", fileList);

		return result;
	}

	@Override
	public Map<String, Object> listGoodsSpecs(List<String> list, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecsByItemId(param);
		if (specsList == null || specsList.size() == 0) {
			return null;
		}

		for (GoodsSpecs specs : specsList) {
			GoodsServiceUtil.getPriceInterval(specs, specs.getDiscount());
		}

		List<String> idList = new ArrayList<String>();
		for (GoodsSpecs model : specsList) {
			idList.add(model.getGoodsId());
		}

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("centerId", param.get("centerId"));
		parameter.put("type", PICTURE_TYPE);

		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("specsList", specsList);
		result.put("pic", fileList);

		return result;
	}

	@Override
	public ResultModel getPriceAndDelStock(List<OrderBussinessModel> list, Integer supplierId, boolean vip,
			Integer centerId, Integer orderFlag, String couponIds, Integer userId) {

		ResultModel result = new ResultModel(true, "");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<Tax, Double> taxMap = new HashMap<Tax, Double>();
		Map<String, Object> param = new HashMap<String, Object>();
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("orderFlag", orderFlag);
		GoodsSpecs specs = null;
		Double totalAmount = 0.0;
		Integer weight = 0;
		Map<String, GoodsSpecs> specsMap = new HashMap<String, GoodsSpecs>();

		for (OrderBussinessModel model : list) {
			param.put("itemId", model.getItemId());
			specs = goodsMapper.getGoodsSpecsForOrder(param);
			weight += specs.getWeight() * model.getQuantity();
			Double amount = GoodsServiceUtil.judgeQuantityRange(vip, result, specs, model);
			if (Constants.O2O_ORDER.equals(orderFlag)) {
				Tax tax = goodsMapper.getTax(param);
				if (taxMap.get(tax) == null) {
					taxMap.put(tax, amount);
				} else {
					taxMap.put(tax, taxMap.get(tax) + amount);
				}
			}
			if (!result.isSuccess()) {
				return result;
			}
			specsMap.put(model.getItemId(),specs);
		}

		totalAmount = priceComponent.calPrice(list, specsMap, couponIds, false, vip, centerId, result, userId);
		if(!result.isSuccess()){
			return result;
		}
		map.put("tax", taxMap);

		if (supplierId != null && Constants.O2O_ORDER.equals(orderFlag)) {
			supplierFeignClient.checkStock(Constants.FIRST_VERSION, supplierId, list);
		}

		boolean enough = processWarehouse.processWarehouse(orderFlag, list);
		if (!enough) {
			result.setSuccess(false);
			result.setErrorMsg("库存不足");
			return result;
		}

		map.put("weight", weight);
		map.put("totalAmount", totalAmount);
		result.setSuccess(true);
		result.setObj(map);
		return result;
	}

	@Override
	public Activity getActivity(Map<String, Object> param) {

		return goodsMapper.getActivity(param);
	}

	@Override
	public List<Layout> getModular(String page, Integer centerId, Integer pageType) {
		Map<String, Object> param = new HashMap<String, Object>();
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("page", page);
		param.put("pageType", pageType);
		return goodsMapper.listLayout(param);
	}

	@Override
	public void createTable(Integer centerId) {
		goodsMapper.createGoodsItem(centerId);
		goodsMapper.createGoods(centerId);
		goodsMapper.createGoodsFile(centerId);
		goodsMapper.createFirstCategory(centerId);
		goodsMapper.createSecondCategory(centerId);
		goodsMapper.createThirdCategory(centerId);
		goodsMapper.createGoodsPrice(centerId);
		goodsMapper.createLayout(centerId);
		goodsMapper.createPopularizeDict(centerId);
		goodsMapper.createPopularizeData(centerId);
		goodsMapper.createActivity(centerId);
		goodsMapper.createActivityData(centerId);
		goodsMapper.createPriceContrast(centerId);
	}

	@Override
	public List<PageModule> getModularData(Integer pageType, String page, Layout layout, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<PageModule> result = new ArrayList<PageModule>();
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("page", page);
		param.put("pageType", pageType);
		if (layout.getId() == null) {
			List<Layout> layoutList = goodsMapper.listLayout(param);
			if (layoutList != null && layoutList.size() > 0) {
				for (Layout temp : layoutList) {
					packageData(param, result, temp);
				}
			}
		} else {
			packageData(param, result, layout);
		}

		return result;
	}

	private void packageData(Map<String, Object> param, List<PageModule> result, Layout temp) {
		param.put("layoutId", temp.getId());
		if (Constants.ACTIVE_MODEL.equals(temp.getType())) {
			List<Activity> activityList = goodsMapper.listActivityByLayoutId(param);
			if (activityList == null) {
				return;
			}
			for (Activity activity : activityList) {
				if (Constants.ACTIVE_START.equals(activity.getStatus())
						|| Constants.ACTIVE_UNSTART.equals(activity.getStatus())) {
					param.put("activeId", activity.getId());
					activity.setCode(temp.getCode());
					result.add(new PageModule(activity, goodsMapper.listActiveData(param)));
					break;
				}
			}
		} else {
			PopularizeDict dict = goodsMapper.getDictByLayoutId(param);
			if (dict == null) {
				return;
			}
			param.put("dictId", dict.getId());
			dict.setCode(temp.getCode());
			result.add(new PageModule(dict, goodsMapper.listDictData(param)));
		}
	}

	@Override
	public void updateActiveStart(Integer centerId, Integer activeId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("activeId", activeId);
		goodsMapper.updateActivitiesStart(param);

	}

	@Override
	public void updateActiveEnd(Integer centerId, Integer activeId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("activeId", activeId);
		goodsMapper.updateActivitiesEnd(param);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getEndActive() {
		Map<String, Object> result = new HashMap<String, Object>();
		ResultModel resultModel = userFeignClient.getCenterId(Constants.FIRST_VERSION);
		if (resultModel.isSuccess()) {
			List<Integer> list = (List<Integer>) resultModel.getObj();
			for (Integer centerId : list) {
				List<Integer> activeIdList = goodsMapper.listEndActiveId("_" + centerId);
				result.put(centerId + "", activeIdList);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createGoodsLucene(Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<GoodsSearch> searchList = new ArrayList<GoodsSearch>();
		if (centerId == null) {
			ResultModel resultModel = userFeignClient.getCenterId(Constants.FIRST_VERSION);
			if (resultModel.isSuccess()) {
				List<Integer> list = (List<Integer>) resultModel.getObj();
				for (Integer id : list) {
					renderLuceneModel(param, searchList, id);
				}
			}
		} else {
			renderLuceneModel(param, searchList, centerId);
		}
	}

	private void renderLuceneModel(Map<String, Object> param, List<GoodsSearch> searchList, Integer id) {
		GoodsSearch searchModel;
		String centerId = GoodsServiceUtil.judgeCenterId(id);
		param.put("centerId", centerId);
		List<GoodsItem> itemList = goodsMapper.listGoodsForLucene(param);
		for (GoodsItem item : itemList) {
			searchModel = new GoodsSearch();
			searchModel.setGoodsId(item.getGoodsId());
			searchModel.setBrand(item.getBrand());
			searchModel.setStatus(item.getStatus());
			searchModel.setOrigin(item.getOrigin());
			searchModel.setFirstCategory(item.getFirstCategory());
			searchModel.setThirdCategory(item.getThirdCategory());
			searchModel.setSecondCategory(item.getSecondCategory());
			searchModel.setGoodsName(item.getCustomGoodsName());
			param.put("goodsId", item.getGoodsId());
			List<GoodsSpecs> specsList = goodsMapper.listSpecsForLucene(param);
			if (specsList != null && specsList.size() > 0) {
				searchModel.setPrice(specsList.get(0).getMinPrice());
			}
			searchList.add(searchModel);
		}
		AbstractLucene lucene = LuceneFactory.get(id);
		lucene.writerIndex(searchList);
		goodsMapper.updateGoodsUpShelves(param);
	}

	private final String GOODS_LIST = "goodsList";
	private final String PAGINATION = "pagination";

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList, Pagination pagination) {
		Map<String, Object> resultMap = new HashMap<String, Object>(16);
		Map<String, Object> luceneMap = new HashMap<String, Object>();

		try {
			luceneMap = LuceneFactory.get(searchModel.getCenterId()).search(searchModel, pagination, sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer total = (Integer) luceneMap.get(Constants.TOTAL);
		List<String> goodsIdList = (List<String>) luceneMap.get(Constants.ID_LIST);
		pagination.setTotalRows((long) total);
		if (goodsIdList != null && goodsIdList.size() > 0) {
			List<GoodsItem> goodsList = new ArrayList<GoodsItem>();
			// 设置高亮
			Map<String, GoodsSearch> highlighterModel = (Map<String, GoodsSearch>) luceneMap
					.get(Constants.HIGHLIGHTER_MODEL);
			Map<String, Object> searchParm = new HashMap<String, Object>();
			searchParm.put("list", goodsIdList);
			if (sortList != null && sortList.getSortList() != null && sortList.getSortList().size() > 0) {
				searchParm.put("sortList", sortList.getSortList());
			}
			String centerId = GoodsServiceUtil.judgeCenterId(searchModel.getCenterId());

			searchParm.put("centerId", centerId);
			goodsList = goodsMapper.queryGoodsItem(searchParm);
			if (highlighterModel != null && highlighterModel.size() > 0) {
				for (GoodsItem model : goodsList) {
					if (highlighterModel.get(model.getGoodsId()).getGoodsName() != null
							&& !"".equals(highlighterModel.get(model.getGoodsId()).getGoodsName())) {

						model.setCustomGoodsName(highlighterModel.get(model.getGoodsId()).getGoodsName());
					}
				}

			}

			List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(searchParm);
			Map<String, GoodsSpecs> temp = new HashMap<String, GoodsSpecs>();
			for (GoodsSpecs specs : specsList) {
				temp.put(specs.getGoodsId(), specs);
			}
			Set<String> specsSet = null;
			for (GoodsItem model : goodsList) {
				GoodsSpecs specs = temp.get(model.getGoodsId());
				if (specs != null) {
					// String specsInfo = specs.getInfo();
					specsSet = new HashSet<>();
					// if(specsInfo != null){
					// Map<String, String> specsMap = JSONUtil.parse(specsInfo,
					// Map.class);
					// for (Map.Entry<String, String> entry :
					// specsMap.entrySet()) {
					// specsSet.add(entry.getValue());
					// }
					// }
					model.setSpecsInfo(specsSet);
					if (specs.getPriceList() != null && specs.getPriceList().size() > 0) {
						Double discount = (specs.getDiscount() == null || specs.getDiscount() == 0) ? 10.0
								: specs.getDiscount();
						discount = CalculationUtils.div(discount, 10.0);
						model.setPrice(specs.getPriceList().get(0).getPrice());
						model.setRealPrice(CalculationUtils.mul(specs.getPriceList().get(0).getPrice(), discount));
					}
				}
			}

			searchParm.put("type", PICTURE_TYPE);

			List<GoodsFile> fileList = goodsMapper.listGoodsFile(searchParm);
			Map<String, List<GoodsFile>> Filetemp = new HashMap<String, List<GoodsFile>>();
			List<GoodsFile> tempList = null;
			for (GoodsFile file : fileList) {
				if (Filetemp.get(file.getGoodsId()) != null) {
					continue;
				}
				tempList = new ArrayList<GoodsFile>();
				tempList.add(file);
				Filetemp.put(file.getGoodsId(), tempList);
			}

			for (GoodsItem model : goodsList) {
				model.setGoodsFileList(Filetemp.get(model.getGoodsId()));
			}

			resultMap.put(GOODS_LIST, goodsList);
		}

		resultMap.put(PAGINATION, pagination.webListConverter());
		resultMap.put(Constants.BRAND, luceneMap.get(Constants.BRAND));
		resultMap.put(Constants.ORIGIN, luceneMap.get(Constants.ORIGIN));

		return resultMap;
	}

	@Override
	public List<GoodsIndustryModel> loadIndexNavigation(Integer centerId) {
		String id = GoodsServiceUtil.judgeCenterId(centerId);

		return goodsMapper.queryGoodsCategory(id);
	}

	@Override
	public void stockBack(List<OrderBussinessModel> list, Integer orderFlag) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		param.put("orderFlag", orderFlag);
		goodsMapper.updateStockBack(param);
	}

	@Override
	public List<TimeLimitActive> getTimelimitGoods(Integer centerId) {
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		List<TimeLimitActive> list = goodsMapper.listLimitTimeData(id);
		Map<String, Object> searchParm = new HashMap<String, Object>();
		if (list == null || list.size() == 0) {
			return null;
		}
		for (TimeLimitActive active : list) {
			if (active.getDataList() != null && active.getDataList().size() > 0) {
				List<String> idList = new ArrayList<String>();
				for (TimeLimitActiveData data : active.getDataList()) {
					idList.add(data.getGoodsId());
				}
				searchParm.put("list", idList);
				searchParm.put("centerId", id);
				List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(searchParm);
				Map<String, GoodsSpecs> temp = new HashMap<String, GoodsSpecs>();
				for (GoodsSpecs specs : specsList) {
					temp.put(specs.getGoodsId(), specs);
				}
				for (TimeLimitActiveData data : active.getDataList()) {
					GoodsSpecs specs = temp.get(data.getGoodsId());
					if (specs != null) {
						if (specs.getPriceList() != null && specs.getPriceList().size() > 0) {
							Double discount = specs.getDiscount() == null ? 10.0 : specs.getDiscount();
							discount = CalculationUtils.div(discount, 10.0);
							data.setPrice(specs.getPriceList().get(0).getPrice());
							data.setRealPrice(CalculationUtils.mul(specs.getPriceList().get(0).getPrice(), discount));
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<GoodsItem> listSpecialGoods(Integer centerId, Integer type) {
		String id = GoodsServiceUtil.judgeCenterId(centerId);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", id);
		param.put("type", type);
		return goodsMapper.listSpecialGoods(param);
	}

	@Override
	public ResultModel stockJudge(List<OrderBussinessModel> list, Integer orderFlag, Integer supplierId) {
		supplierFeignClient.checkStock(Constants.FIRST_VERSION, supplierId, list);
		boolean enough = processWarehouse.processWarehouse(orderFlag, list);
		if (!enough) {
			return new ResultModel(false, "库存不足");
		}
		return new ResultModel(true, "");
	}

	@Override
	public boolean updateThirdWarehouseStock(List<WarehouseStock> list) {
		goodsMapper.updateThirdWarehouseStock(list);
		return true;
	}

	@Override
	public boolean saveThirdGoods(List<ThirdWarehouseGoods> list) {
		goodsMapper.saveThirdGoods(list);
		return true;
	}

	@Override
	public Double getCostPrice(List<OrderBussinessModel> list) {
		List<PriceModel> priceList = goodsMapper.getCostPrice(list);
		Double totalAmount = 0.0;
		if (priceList != null) {
			for (PriceModel model : priceList) {
				for (OrderBussinessModel temp : list) {
					if (model.getItemId().equals(temp.getItemId())) {
						if (temp.getQuantity() >= model.getMin()
								&& (model.getMax() == null || temp.getQuantity() <= model.getMax())) {
							totalAmount = CalculationUtils.add(totalAmount,
									CalculationUtils.mul(temp.getQuantity(), model.getPrice()));
						}
					}
				}
			}
		}

		return totalAmount;
	}

	@Override
	public List<OrderBussinessModel> checkStock() {
		return goodsMapper.checkStock();
	}

	@Override
	public ResultModel upShelves(List<String> goodsIdList, Integer centerId) {
		if(goodsIdList != null && goodsIdList.size() > 0){
			Map<String, Object> param = new HashMap<String, Object>();
			List<GoodsSearch> searchList = new ArrayList<GoodsSearch>();
			param.put("list", goodsIdList);
			renderLuceneModel(param, searchList, centerId);
		}
		return new ResultModel(true,"");
	}

	@Override
	public ResultModel downShelves(List<String> goodsIdList, Integer centerId) {
		if(goodsIdList != null && goodsIdList.size() > 0){
			deleteLuceneAndDownShelves(goodsIdList, centerId);
		}
		return new ResultModel(true,"");
	}

	private void deleteLuceneAndDownShelves(List<String> goodsIdList, Integer id) {
		String centerId = GoodsServiceUtil.judgeCenterId(id);
		AbstractLucene lucene = LuceneFactory.get(id);
		lucene.deleteIndex(goodsIdList);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("list", goodsIdList);
		goodsMapper.updateGoodsDownShelves(param);
	}

}
