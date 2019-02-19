package com.zm.goods.bussiness.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.goods.exception.OriginalPriceUnEqual;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.vo.Coupon;
import com.zm.goods.pojo.vo.CouponGoodsbinding;
import com.zm.goods.utils.JSONUtil;

@Component
public class PriceComponent {

	@Resource
	ActivityComponent activityComponent;

	@Resource
	GoodsServiceComponent goodsServiceComponent;

	private final int FULL_RANGE = 0;
	private final int FIRST_RANGE = 1;
	private final int SECOND_RANGE = 2;
	private final int THIRD_RANGE = 3;
	private final int GIVEN_RANGE = 4;

	/**
	 * @fun 获取商品订单价格，判断优惠券是否可用，调用getAmount方法获取订单价格
	 * @param list
	 *            订单商品
	 * @param specsMap
	 *            规格Map
	 * @param couponIds
	 *            优惠券
	 * @param vip
	 *            用户是否vip
	 * @param centerId
	 *            商城ID
	 * @param result
	 *            返回是否成功结果
	 * @param userId
	 *            用户ID
	 * @param platformSource
	 *            平台类型 0普通；1福利
	 * @param gradeId
	 *            分级ID
	 * @return
	 * @throws WrongPlatformSource
	 * @throws OriginalPriceUnEqual
	 */
	@SuppressWarnings("unchecked")
	public Double calPrice(List<OrderBussinessModel> list, Map<String, GoodsSpecs> specsMap, String couponIds,
			boolean vip, Integer centerId, ResultModel result, Integer userId, int platformSource, int gradeId)
					throws WrongPlatformSource, OriginalPriceUnEqual {

		Double totalAmount = 0.0;
		GoodsSpecs specs = null;
		if (couponIds == null) {
			for (OrderBussinessModel model : list) {
				specs = specsMap.get(model.getItemId());
				totalAmount += goodsServiceComponent.getAmount(vip, specs, model, specs.getDiscount(), platformSource,
						gradeId);
			}
		} else {
			// 获取优惠券并判断用户是否有该优惠券
			ResultModel resultModel = activityComponent.listCouponByCouponIds(centerId, couponIds, userId);
			if (!resultModel.isSuccess()) {
				result.setSuccess(false);
				result.setErrorMsg(resultModel.getErrorMsg());
				return null;
			}
			List<Map<String, Object>> mapList = (List<Map<String, Object>>) resultModel.getObj();
			List<Coupon> couponList = new ArrayList<Coupon>();
			for (Map<String, Object> map : mapList) {
				Coupon c = JSONUtil.parse(JSONUtil.toJson(map), Coupon.class);
				couponList.add(c);
			}
			// 判断条件是否满足和是否叠加使用
			totalAmount = judgeCouponLegitimate(couponList, specsMap, list, result, vip, platformSource, gradeId);
			if (!result.isSuccess()) {
				return null;
			}
			totalAmount = doCalPrice(couponList, totalAmount);
		}
		return totalAmount;
	}

	/**
	 * @fun 获取扣除优惠券后的订单价格
	 * @param couponList
	 * @param totalAmount
	 * @return
	 */
	private Double doCalPrice(List<Coupon> couponList, Double totalAmount) {

		Double couponAmount = 0.0;
		for (Coupon c : couponList) {
			couponAmount += c.getRule().getDeductibleValue();
		}
		return totalAmount - couponAmount;
	}

	private final Integer UNSUPERPOSITION = 0;// 0不可叠加使用

	/**
	 * @fun 判断优惠券是否满足使用条件
	 * @param couponList
	 * @param specsMap
	 * @param list
	 * @param result
	 * @param vip
	 * @param platformSource
	 * @param gradeId
	 * @return
	 * @throws WrongPlatformSource
	 * @throws OriginalPriceUnEqual 
	 */
	private Double judgeCouponLegitimate(List<Coupon> couponList, Map<String, GoodsSpecs> specsMap,
			List<OrderBussinessModel> list, ResultModel result, boolean vip, int platformSource, int gradeId)
					throws WrongPlatformSource, OriginalPriceUnEqual {

		List<Integer> superposition = new ArrayList<Integer>();
		for (Coupon c : couponList) {
			if (UNSUPERPOSITION.equals(c.getRule().getSuperposition())) {
				if (superposition.contains(UNSUPERPOSITION)) {
					result.setSuccess(false);
					result.setErrorMsg("不可叠加使用");
					return null;
				} else {
					superposition.add(c.getRule().getSuperposition());
				}
			}
			Integer range = c.getRule().getRange();
			switch (range) {
			case FULL_RANGE:
				return doJudgeFullRange(c, specsMap, list, result, vip, platformSource, gradeId);
			case FIRST_RANGE:
				return doJudgeCategoryRange(c, specsMap, list, result, vip, FIRST_RANGE, platformSource, gradeId);
			case SECOND_RANGE:
				return doJudgeCategoryRange(c, specsMap, list, result, vip, SECOND_RANGE, platformSource, gradeId);
			case THIRD_RANGE:
				return doJudgeCategoryRange(c, specsMap, list, result, vip, THIRD_RANGE, platformSource, gradeId);
			case GIVEN_RANGE:
				return doJudgeGivenRange(c, specsMap, list, result, vip, platformSource, gradeId);
			}
		}
		return null;
	}

	/**
	 * @fun 判断是否指定商品的优惠券，成功返回总计商品总价
	 * @param c
	 * @param specsMap
	 * @param list
	 * @param result
	 * @param vip
	 * @param platformSource
	 * @param gradeId
	 * @return
	 * @throws WrongPlatformSource
	 * @throws OriginalPriceUnEqual 
	 */
	private Double doJudgeGivenRange(Coupon c, Map<String, GoodsSpecs> specsMap, List<OrderBussinessModel> list,
			ResultModel result, boolean vip, int platformSource, int gradeId) throws WrongPlatformSource, OriginalPriceUnEqual {
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
						totalAmount += goodsServiceComponent.getAmount(vip, entry.getValue(), model,
								entry.getValue().getDiscount(), platformSource, gradeId);
					}
				}
			}
		}
		if (totalAmount < c.getRule().getCondition()) {
			result.setErrorMsg("不符合优惠券使用条件");
			result.setSuccess(false);
			return null;
		}
		return totalAmount;
	}

	/**
	 * @fun 判断优惠券根据分类使用是否成功，成功返回商品总价
	 * @param c
	 * @param specsMap
	 * @param list
	 * @param result
	 * @param vip
	 * @param cate
	 * @param platformSource
	 * @param gradeId
	 * @return
	 * @throws WrongPlatformSource
	 * @throws OriginalPriceUnEqual 
	 */
	private Double doJudgeCategoryRange(Coupon c, Map<String, GoodsSpecs> specsMap, List<OrderBussinessModel> list,
			ResultModel result, boolean vip, int cate, int platformSource, int gradeId) throws WrongPlatformSource, OriginalPriceUnEqual {

		Double totalAmount = 0.0;
		String category = null;

		for (Map.Entry<String, GoodsSpecs> entry : specsMap.entrySet()) {
			category = getCategory(cate, entry);
			if (category.equals(c.getRule().getCategory())) {
				for (OrderBussinessModel model : list) {
					if (model.getItemId().equals(entry.getValue().getItemId())) {
						totalAmount += goodsServiceComponent.getAmount(vip, entry.getValue(), model,
								entry.getValue().getDiscount(), platformSource, gradeId);
					}
				}
			}
		}
		if (totalAmount < c.getRule().getCondition()) {
			result.setErrorMsg("不符合优惠券使用条件");
			result.setSuccess(false);
			return null;
		}
		return totalAmount;
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

	/**
	 * @fun 判断优惠券全场使用是否符合，返回商品订单总计
	 * @param c
	 * @param specsMap
	 * @param list
	 * @param result
	 * @param vip
	 * @param platformSource
	 * @param gradeId
	 * @return
	 * @throws WrongPlatformSource
	 * @throws OriginalPriceUnEqual 
	 */
	private Double doJudgeFullRange(Coupon c, Map<String, GoodsSpecs> specsMap, List<OrderBussinessModel> list,
			ResultModel result, boolean vip, int platformSource, int gradeId) throws WrongPlatformSource, OriginalPriceUnEqual {

		Double totalAmount = 0.0;
		GoodsSpecs specs = null;
		for (OrderBussinessModel model : list) {
			specs = specsMap.get(model.getItemId());
			totalAmount += goodsServiceComponent.getAmount(vip, specs, model, specs.getDiscount(), platformSource,
					gradeId);
		}
		if (totalAmount < c.getRule().getCondition()) {
			result.setErrorMsg("不符合优惠券使用条件");
			result.setSuccess(false);
			return null;
		}
		return totalAmount;
	}
}
