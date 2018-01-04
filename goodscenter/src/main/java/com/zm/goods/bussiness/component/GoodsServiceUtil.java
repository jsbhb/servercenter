package com.zm.goods.bussiness.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.utils.CalculationUtils;
import com.zm.goods.utils.CommonUtils;
import com.zm.goods.utils.JSONUtil;

public class GoodsServiceUtil {

	//计算价格
	public static Double getAmount(boolean vip, GoodsSpecs specs, OrderBussinessModel model, Double promotion) {
		Double totalAmount = 0.0;
		getPriceInterval(specs, promotion);
		boolean calculation = false;
		Double discount = 10.0;
		if (promotion != null && promotion != 0.0) {
			discount = promotion;
		}
		discount = CalculationUtils.div(discount, 10.0);
		for (GoodsPrice price : specs.getPriceList()) {
			boolean flag = model.getQuantity() >= price.getMin()
					&& (price.getMax() == null || model.getQuantity() <= price.getMax());
			if (flag) {
				if (model.getDeliveryPlace() != null) {
					if (model.getDeliveryPlace().equals(price.getDeliveryPlace())) {
						totalAmount += model.getQuantity()
								* (vip ? (price.getVipPrice() == null ? 0 : price.getVipPrice()) : price.getPrice());
						calculation = true;
						break;
					}
				} else {
					totalAmount += model.getQuantity()
							* (vip ? (price.getVipPrice() == null ? 0 : price.getVipPrice()) : price.getPrice());
					calculation = true;
					break;
				}
			}
		}
		if (!calculation) {
			return null;
		}

		return CalculationUtils.mul(totalAmount, discount);
	}

	//封装商品属性
	@SuppressWarnings("unchecked")
	public static void packageGoodsItem(List<GoodsItem> goodsList, List<GoodsFile> fileList, List<GoodsSpecs> specsList,
			boolean flag) {

		Map<String, GoodsItem> goodsMap = new HashMap<String, GoodsItem>();
		if (goodsList == null || goodsList.size() == 0) {
			return;
		}

		for (GoodsItem item : goodsList) {
			goodsMap.put(item.getGoodsId(), item);
		}

		GoodsItem item = null;
		List<GoodsFile> tempFileList = null;
		List<GoodsSpecs> tempSpecsList = null;
		if (fileList != null && fileList.size() > 0) {
			for (GoodsFile file : fileList) {
				item = goodsMap.get(file.getGoodsId());
				if (item == null) {
					continue;
				}
				if (item.getGoodsFileList() == null) {
					tempFileList = new ArrayList<GoodsFile>();
					tempFileList.add(file);
					item.setGoodsFileList(tempFileList);
				} else {
					item.getGoodsFileList().add(file);
				}
			}
		}

		if (specsList != null && specsList.size() > 0) {
			Map<String, String> temp = null;
			Set<String> tempSet = null;
			Double discount = null;
			for (GoodsSpecs specs : specsList) {
				item = goodsMap.get(specs.getGoodsId());
				if (item == null) {
					continue;
				}
				if (flag) {
					if (item.getGoodsSpecsList() == null) {
						tempSpecsList = new ArrayList<GoodsSpecs>();
						tempSpecsList.add(specs);
						item.setGoodsSpecsList(tempSpecsList);
					} else {
						item.getGoodsSpecsList().add(specs);
					}
					discount = specs.getDiscount();
					getPriceInterval(specs, discount);
				} else {
					temp = JSONUtil.parse(specs.getInfo(), Map.class);
					for (Map.Entry<String, String> entry : temp.entrySet()) {
						if (item.getSpecsInfo() == null) {
							tempSet = new HashSet<String>();
							tempSet.add(entry.getValue());
							item.setSpecsInfo(tempSet);
						} else {
							item.getSpecsInfo().add(entry.getKey());
						}
					}
				}
			}
		}
	}

	// 封装价格区间
	public static void getPriceInterval(GoodsSpecs specs, Double discountParam) {
		List<GoodsPrice> priceList = specs.getPriceList();
		specs.infoFilter();
		if (priceList == null) {
			return;
		}
		Double discount = 10.0;
		if (discountParam != null && discountParam != 0.0) {
			discount = discountParam;
		}
		discount = CalculationUtils.div(discount, 10.0);
		for (int i = 0; i < priceList.size(); i++) {
			if (i == 0) {
				specs.setMinPrice(priceList.get(i).getPrice());
				specs.setMaxPrice(priceList.get(i).getPrice());
				specs.setRealMinPrice(CalculationUtils.mul(priceList.get(i).getPrice(), discount));
				specs.setRealMaxPrice(CalculationUtils.mul(priceList.get(i).getPrice(), discount));
				if (priceList.get(i).getVipPrice() != null) {
					specs.setVipMinPrice(priceList.get(i).getVipPrice());
					specs.setVipMaxPrice(priceList.get(i).getVipPrice());
					specs.setRealVipMinPrice(CalculationUtils.mul(priceList.get(i).getVipPrice(), discount));
					specs.setRealVipMaxPrice(CalculationUtils.mul(priceList.get(i).getVipPrice(), discount));
				}

			} else {
				specs.setRealMinPrice(CalculationUtils
						.mul(CommonUtils.getMinDouble(specs.getMinPrice(), priceList.get(i).getPrice()), discount));
				specs.setMinPrice(CommonUtils.getMinDouble(specs.getMinPrice(), priceList.get(i).getPrice()));
				specs.setRealMaxPrice(CalculationUtils
						.mul(CommonUtils.getMaxDouble(specs.getMaxPrice(), priceList.get(i).getPrice()), discount));
				specs.setMaxPrice(CommonUtils.getMaxDouble(specs.getMaxPrice(), priceList.get(i).getPrice()));
				if (priceList.get(i).getVipPrice() != null) {
					specs.setRealVipMinPrice(CalculationUtils.mul(
							CommonUtils.getMinDouble(specs.getVipMinPrice(), priceList.get(i).getVipPrice()),
							discount));
					specs.setVipMinPrice(
							CommonUtils.getMinDouble(specs.getVipMinPrice(), priceList.get(i).getVipPrice()));
					specs.setRealVipMaxPrice(CalculationUtils.mul(
							CommonUtils.getMaxDouble(specs.getVipMaxPrice(), priceList.get(i).getVipPrice()),
							discount));
					specs.setVipMaxPrice(
							CommonUtils.getMaxDouble(specs.getVipMaxPrice(), priceList.get(i).getVipPrice()));
				}
			}
		}
	}
	
	public static String judgeCenterId(Integer id) {
		String centerId = "_" + id;
		return centerId;
	}
	
	public static Double judgeQuantityRange(boolean vip, ResultModel result, GoodsSpecs specs, OrderBussinessModel model) {
		Double amount = getAmount(vip, specs, model, 10.0);
		if (amount == null) {
			result.setSuccess(false);
			result.setErrorMsg("购买数量不在指定范围内");
		}
		return amount;
	}
	
	public static Map<String,Double> getMinPrice(List<GoodsSpecs> specsList){
		Map<String,Double> result = new HashMap<String,Double>();
		if(specsList == null || specsList.size() == 0){
			result.put("price", 0.0);
			result.put("realPrice", 0.0);
			return result;
		}
		for(GoodsSpecs specs : specsList){
			getPriceInterval(specs, specs.getDiscount());
		}
		int len = specsList.size();
		
		for(int i=0;i<len;i++){
			if(i == 0){
				result.put("price", specsList.get(i).getMinPrice());
				result.put("realPrice", specsList.get(i).getRealMinPrice());
			} else {
				if(specsList.get(i).getMinPrice() < result.get("price")){
					result.put("price", specsList.get(i).getMinPrice());
				}
				if(specsList.get(i).getRealMinPrice() < result.get("realPrice")){
					result.put("realPrice", specsList.get(i).getRealMinPrice());
				}
			}
		}
		return result;
	} 
}
