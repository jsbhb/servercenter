package com.zm.order.bussiness.component;

import java.util.List;

import com.zm.order.constants.Constants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ResultModel;

public class OrderGoodsDealByCreateType {

	private GoodsFeignClient goodsFeignClient;

	public OrderGoodsDealByCreateType(GoodsFeignClient goodsFeignClient) {

		this.goodsFeignClient = goodsFeignClient;
	}

	/**
	 * @fun 根据不同的创建类型获取订单商品的金额税费等信息
	 * @param info
	 * @param list
	 * @param vip
	 * @param fx
	 * @return
	 */
	public ResultModel doOrderGoodsDeal(OrderInfo info, List<OrderBussinessModel> list, boolean vip, boolean fx) {
		switch (info.getCreateType()) {
		case Constants.TO_B_ORDER:
			return getNormal(info, list, vip, fx);
		case Constants.NORMAL_ORDER:
			return getNormal(info, list, vip, fx);
		case Constants.OPEN_INTERFACE_TYPE:
			return getNormal(info, list, vip, fx);
		case Constants.BARGAIN_ORDER:
			return getBargain(info, list, vip, fx);
		default:
			return new ResultModel(false, "创建类型有误");
		}
	}
	
	private ResultModel getNormal(OrderInfo info, List<OrderBussinessModel> list, boolean vip, boolean fx){
		int centerId = getCenterId(info);
		return goodsFeignClient.getPriceAndDelStock(Constants.FIRST_VERSION, list, info.getSupplierId(), vip,
				centerId, info.getOrderFlag(), info.getCouponIds(), info.getUserId(), fx,
				info.getOrderSource(), info.getShopId());
	}
	
	private ResultModel getBargain(OrderInfo info, List<OrderBussinessModel> list, boolean vip, boolean fx){
		if(list.size() > 1){
			return new ResultModel(false,"砍价类订单，每个订单只能有一件商品");
		}
		int id;
		try {
			id = Integer.valueOf(info.getCouponIds().split(",")[0]);
		} catch (Exception e) {
			return new ResultModel(false,"优惠列表参数有误");
		}
		return goodsFeignClient.getBargainGoodsInfo(Constants.FIRST_VERSION, list, id, info.getUserId());
	}
	
	private int getCenterId(OrderInfo info){
		int centerId;
		if (Constants.PREDETERMINE_ORDER == info.getOrderSource()) {// 如果是订货平台订单，默认centerId
			centerId = -1;
		} else {
			centerId = info.getCenterId();
		}
		return centerId;
	}

	public GoodsFeignClient getGoodsFeignClient() {
		return goodsFeignClient;
	}

	public void setGoodsFeignClient(GoodsFeignClient goodsFeignClient) {
		this.goodsFeignClient = goodsFeignClient;
	}
}
