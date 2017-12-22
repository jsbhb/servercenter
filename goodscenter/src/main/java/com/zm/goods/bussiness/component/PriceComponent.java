package com.zm.goods.bussiness.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.vo.Coupon;
import com.zm.goods.pojo.vo.CouponGoodsbinding;
import com.zm.goods.utils.JSONUtil;

@Component
public class PriceComponent {

	@Resource
	ActivityComponent activityComponent;

	private final int FULL_RANGE = 0;
	private final int FIRST_RANGE = 1;
	private final int SECOND_RANGE = 2;
	private final int THIRD_RANGE = 3;
	private final int GIVEN_RANGE = 4;

	@SuppressWarnings("unchecked")
	public Double calPrice(List<OrderBussinessModel> list, Map<String, GoodsSpecs> specsMap, String couponIds,
			boolean activity, boolean vip, Integer centerId, ResultModel result, Integer userId) {

		Double totalAmount = 0.0;
		GoodsSpecs specs = null;
		if (couponIds == null) {
			for (OrderBussinessModel model : list) {
				specs = specsMap.get(model.getItemId());
				totalAmount += GoodsServiceUtil.getAmount(vip, specs, model, specs.getDiscount());
			}
		} else {
			// 获取优惠券并判断用户是否有该优惠券
			ResultModel resultModel = activityComponent.listCouponByCouponIds(centerId, couponIds, userId);
			if (!resultModel.isSuccess()) {
				result.setSuccess(false);
				result.setErrorMsg(resultModel.getErrorMsg());
				return totalAmount;
			}
			List<Map<String,Object>> mapList = (List<Map<String, Object>>) resultModel.getObj();
			List<Coupon> couponList = new ArrayList<Coupon>();
			for(Map<String,Object> map : mapList){
				Coupon c = JSONUtil.parse(JSONUtil.toJson(map), Coupon.class);
				couponList.add(c);
			}
			// 判断条件是否满足和是否叠加使用
			judgeCouponLegitimate(couponList, specsMap, list, result, vip);
			if (!result.isSuccess()) {
				return totalAmount;
			}
			totalAmount = doCalPrice(couponList, specsMap, list, vip, activity);
		}
		return totalAmount;
	}

	private Double doCalPrice(List<Coupon> couponList, Map<String, GoodsSpecs> specsMap, List<OrderBussinessModel> list,
			boolean vip, boolean activity) {

		Double couponAmount = 0.0;
		Double totalAmount = 0.0;
		GoodsSpecs specs = null;
		for (Coupon c : couponList) {
			couponAmount += c.getRule().getDeductibleValue();
		}
		for (OrderBussinessModel model : list) {
			specs = specsMap.get(model.getItemId());
			totalAmount += GoodsServiceUtil.getAmount(vip, specs, model, specs.getDiscount());
		}
		return totalAmount - couponAmount;
	}

	private final Integer UNSUPERPOSITION = 0;// 0不可叠加使用

	private void judgeCouponLegitimate(List<Coupon> couponList, Map<String, GoodsSpecs> specsMap,
			List<OrderBussinessModel> list, ResultModel result, boolean vip) {

		List<Integer> superposition = new ArrayList<Integer>();
		for (Coupon c : couponList) {
			if (UNSUPERPOSITION.equals(c.getRule().getSuperposition())) {
				if(superposition.contains(UNSUPERPOSITION)){
					result.setSuccess(false);
					result.setErrorMsg("不可叠加使用");
					return;
				} else {
					superposition.add(c.getRule().getSuperposition());
				}
			}
			Integer range = c.getRule().getRange();
			switch (range) {
			case FULL_RANGE:
				doJudgeFullRange(c, specsMap, list, result, vip);
				break;
			case FIRST_RANGE:
				doJudgeCategoryRange(c, specsMap, list, result, vip, FIRST_RANGE);
				break;
			case SECOND_RANGE:
				doJudgeCategoryRange(c, specsMap, list, result, vip, SECOND_RANGE);
				break;
			case THIRD_RANGE:
				doJudgeCategoryRange(c, specsMap, list, result, vip, THIRD_RANGE);
				break;
			case GIVEN_RANGE:
				doJudgeGivenRange(c, specsMap, list, result, vip);
				break;
			}
			if (!result.isSuccess()) {
				return;
			}
		}
	}

	private void doJudgeGivenRange(Coupon c, Map<String, GoodsSpecs> specsMap, List<OrderBussinessModel> list,
			ResultModel result, boolean vip) {
		Double totalAmount = 0.0;
		List<CouponGoodsbinding> bindList = c.getRule().getBindingList();
		List<String> goodsIdList = new ArrayList<String>();
		for (CouponGoodsbinding b : bindList) {
			goodsIdList.add(b.getGoodsId());
		}
		for (Map.Entry<String, GoodsSpecs> entry : specsMap.entrySet()) {
			if (goodsIdList.contains(entry.getValue().getGoodsId())) {
				for (OrderBussinessModel model : list) {
					if (model.getItemId().equals(entry.getValue().getItemId())) {
						totalAmount += GoodsServiceUtil.getAmount(vip, entry.getValue(), model,
								entry.getValue().getDiscount());
					}
				}
			}
		}
		if (totalAmount < c.getRule().getCondition()) {
			result.setErrorMsg("不符合优惠券使用条件");
			result.setSuccess(false);
		}
	}

	private void doJudgeCategoryRange(Coupon c, Map<String, GoodsSpecs> specsMap, List<OrderBussinessModel> list,
			ResultModel result, boolean vip, int cate) {

		Double totalAmount = 0.0;
		String category = null;

		for (Map.Entry<String, GoodsSpecs> entry : specsMap.entrySet()) {
			category = getCategory(cate, entry);
			if (category.equals(c.getRule().getCategory())) {
				for (OrderBussinessModel model : list) {
					if (model.getItemId().equals(entry.getValue().getItemId())) {
						totalAmount += GoodsServiceUtil.getAmount(vip, entry.getValue(), model,
								entry.getValue().getDiscount());
					}
				}
			}
		}
		if (totalAmount < c.getRule().getCondition()) {
			result.setErrorMsg("不符合优惠券使用条件");
			result.setSuccess(false);
		}
	}

	private String getCategory(int cate, Map.Entry<String, GoodsSpecs> entry) {
		switch (cate) {
		case FIRST_RANGE:
			return entry.getValue().getFirstCategory();
		case SECOND_RANGE:
			return entry.getValue().getSecondCategory();
		case THIRD_RANGE:
			return entry.getValue().getThirdCategory();
		}
		return null;
	}

	private void doJudgeFullRange(Coupon c, Map<String, GoodsSpecs> specsMap, List<OrderBussinessModel> list,
			ResultModel result, boolean vip) {

		Double totalAmount = 0.0;
		GoodsSpecs specs = null;
		for (OrderBussinessModel model : list) {
			specs = specsMap.get(model.getItemId());
			totalAmount += GoodsServiceUtil.getAmount(vip, specs, model, specs.getDiscount());
		}
		if (totalAmount < c.getRule().getCondition()) {
			result.setErrorMsg("不符合优惠券使用条件");
			result.setSuccess(false);
		}
	}
}
