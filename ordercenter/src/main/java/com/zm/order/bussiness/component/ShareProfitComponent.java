package com.zm.order.bussiness.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.order.bussiness.component.model.ShareProfitModel;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ProfitProportion;
import com.zm.order.pojo.UserInfo;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.JSONUtil;

@Component
public class ShareProfitComponent {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	OrderMapper orderMapper;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	GoodsFeignClient goodsFeignClient;

	private static Integer register_type = 0;// 注册地分润
	private static Integer consume_type = 1;// 消费地分润
	private static Integer register_consume_type = 3;// 注册地消费地相同

	public void calShareProfit(String orderId) {

		try {

			OrderInfo info = orderMapper.getOrderByOrderIdForRebate(orderId);
			if (Constants.PREDETERMINE_ORDER == info.getOrderSource()) {
				predetermineOrderProfit(info);
			} else {
				calCanBePresented(info);
			}

		} catch (Exception e) {
			LogUtil.writeErrorLog("【从待到账到已到账的返佣计算出错】订单号：" + orderId, e);
		}

	}

	/**
	 * @fun 计算待到账的金额
	 * @param orderId
	 */
	public void calShareProfitStayToAccount(String orderId) {

		try {
			// 判断redis里是否有该订单，有的话说明正在计算
			synchronized (ShareProfitComponent.class) {
				boolean flag = template.opsForSet().isMember(Constants.ORDER_REBATE, orderId);
				if (flag) {
					return;
				} else {
					template.opsForSet().add(Constants.ORDER_REBATE, orderId);
				}
			}

			OrderInfo info = orderMapper.getOrderByOrderIdForRebate(orderId);
			if (info == null) {
				return;
			}
			if (Constants.PREDETERMINE_ORDER == info.getOrderSource()) {
				predetermineOrderProfitStayToAccount(info);
			} else {
				orderProfitStayToAccount(info);
			}
			orderMapper.updateOrderRebate(orderId);
		} catch (Exception e) {
			template.opsForList().leftPush(Constants.EXCEPTION_PROFIT, orderId);
			LogUtil.writeErrorLog("【待到账返佣计算出错】订单号：" + orderId, e);
		}

	}

	/**
	 * @fun 退款时扣除待到账金额
	 * @param orderId
	 */
	public void calRefundShareProfit(String orderId) {
		OrderInfo info = orderMapper.getOrderByOrderIdForRebate(orderId);
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Rebate rebate = new Rebate();
		template.opsForSet().remove(Constants.ORDER_REBATE, orderId);
		calRebate(info, rebate, hashOperations);
		hashOperations.increment(Constants.CENTER_ORDER_REBATE + info.getCenterId(), "StayToAccount",
				CalculationUtils.sub(0, rebate.getCenterRebate()));
		if (rebate.getShopRebate() > 0) {
			hashOperations.increment(Constants.SHOP_ORDER_REBATE + info.getShopId(), "StayToAccount",
					CalculationUtils.sub(0, rebate.getShopRebate()));
		}
		if (rebate.getPushUserRebate() > 0) {
			hashOperations.increment(Constants.PUSHUSER_ORDER_REBATE + info.getPushUserId(), "StayToAccount",
					CalculationUtils.sub(0, rebate.getPushUserRebate()));
		}
	}

	/**
	 * @fun 订货平台分润
	 * @param info
	 */
	private void predetermineOrderProfit(OrderInfo info) {
		// TODO Auto-generated method stub

	}

	/**
	 * @fun 订货平台待到账分润
	 * @param info
	 */
	private void predetermineOrderProfitStayToAccount(OrderInfo info) {
		// TODO Auto-generated method stub

	}

	/**
	 * @fun 消费订单分润计算(原先有消费地和注册地之分)
	 * @param info
	 * @deprecated
	 */
	private void consumOrderProfit(OrderInfo info) {
		if (info.getOrderGoodsList() != null) {
			List<OrderBussinessModel> list = new ArrayList<OrderBussinessModel>();
			OrderBussinessModel model = null;
			for (OrderGoods goods : info.getOrderGoodsList()) {
				model = new OrderBussinessModel();
				model.setItemId(goods.getItemId());
				model.setQuantity(goods.getItemQuantity());
				list.add(model);
			}
			Double cost = goodsFeignClient.getCostPrice(Constants.FIRST_VERSION, list);
			// TODO 结算邮费

			Double totalProfit = 0.0;
			if (cost != null && cost > 0) {
				totalProfit = CalculationUtils.sub(info.getOrderDetail().getPayment(),
						info.getOrderDetail().getTaxFee());
				totalProfit = CalculationUtils.sub(totalProfit, cost);
			}

			UserInfo user = userFeignClient.getUser(Constants.FIRST_VERSION, info.getUserId());
			ProfitProportion profitProportion = orderMapper.getProfitProportion(info.getCenterId());

			// 获取用于分润的钱
			Double totalShareProfit = CalculationUtils.mul(totalProfit, profitProportion.getTotal_profit_proportion());

			if (info.getShopId() == null && user.getShopId() == null) {
				return;
			}
			boolean crossArea = false;
			if (!info.getCenterId().equals(user.getCenterId())) {
				crossArea = true;
			}
			if (info.getShopId() == null && user.getShopId() != null) {
				setShareProfit(totalProfit, profitProportion, totalShareProfit, info, register_type, crossArea,
						user.getShopId());
			}
			if (info.getShopId() != null && user.getShopId() == null) {
				setShareProfit(totalProfit, profitProportion, totalShareProfit, info, consume_type, crossArea,
						info.getShopId());
			}
			if (info.getShopId() != null && user.getShopId() != null) {
				if (info.getShopId().equals(user.getShopId())) {
					setShareProfit(totalProfit, profitProportion, totalShareProfit, info, register_consume_type,
							crossArea, user.getShopId());
				} else {
					setShareProfit(totalProfit, profitProportion, totalShareProfit, info, crossArea, user.getShopId(),
							info.getShopId());
				}
			}

		}
	}

	private void setShareProfit(Double totalProfit, ProfitProportion profitProportion, Double totalShareProfit,
			OrderInfo info, boolean crossArea, Integer shopId, Integer shopId2) {

		ShareProfitModel shareProfitModel = null;
		shareProfitModel = new ShareProfitModel();
		shareProfitModel.setOrderAmount(info.getOrderDetail().getPayment());
		shareProfitModel.setOrderId(info.getOrderId());
		shareProfitModel.setOrderProfit(totalProfit);
		shareProfitModel.setRegisterProfit(
				CalculationUtils.mul(totalShareProfit, profitProportion.getRegister_profit_proportion()));
		shareProfitModel.setCrossArea(crossArea);
		template.opsForList().rightPush(Constants.PROFIT + shopId, shareProfitModel);

		shareProfitModel = new ShareProfitModel();
		shareProfitModel.setOrderAmount(info.getOrderDetail().getPayment());
		shareProfitModel.setOrderId(info.getOrderId());
		shareProfitModel.setOrderProfit(totalProfit);
		shareProfitModel.setConsumeProfit(
				CalculationUtils.mul(totalShareProfit, profitProportion.getConsume_profit_proportion()));
		shareProfitModel.setCrossArea(crossArea);
		template.opsForList().rightPush(Constants.PROFIT + shopId2, shareProfitModel);
	}

	/**
	 * 
	 * @param totalProfit
	 * @param profitProportion
	 * @param totalShareProfit
	 * @param info
	 * @param type
	 *            0注册地分润；1消费地分润
	 * @param crossArea
	 * @param shopId
	 */
	private void setShareProfit(Double totalProfit, ProfitProportion profitProportion, Double totalShareProfit,
			OrderInfo info, Integer type, boolean crossArea, Integer shopId) {

		ShareProfitModel shareProfitModel = new ShareProfitModel();
		shareProfitModel.setOrderAmount(info.getOrderDetail().getPayment());
		shareProfitModel.setOrderId(info.getOrderId());
		shareProfitModel.setOrderProfit(totalProfit);
		if (register_type.equals(type)) {
			shareProfitModel.setRegisterProfit(
					CalculationUtils.mul(totalShareProfit, profitProportion.getRegister_profit_proportion()));
		}
		if (consume_type.equals(type)) {
			shareProfitModel.setConsumeProfit(
					CalculationUtils.mul(totalShareProfit, profitProportion.getConsume_profit_proportion()));
		}
		if (register_consume_type.equals(type)) {
			shareProfitModel.setConsumeProfit(
					CalculationUtils.mul(totalShareProfit, profitProportion.getConsume_profit_proportion()));
			shareProfitModel.setRegisterProfit(
					CalculationUtils.mul(totalShareProfit, profitProportion.getRegister_profit_proportion()));
		}
		shareProfitModel.setCrossArea(crossArea);

		template.opsForList().rightPush(Constants.PROFIT + shopId, shareProfitModel);
	}

	/**
	 * @fun 订单返佣计算，放入待到账中
	 * @param orderInfo
	 */
	private void orderProfitStayToAccount(OrderInfo orderInfo) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Rebate rebate = new Rebate();
		calRebate(orderInfo, rebate, hashOperations);
		hashOperations.increment(Constants.CENTER_ORDER_REBATE + orderInfo.getCenterId(), "StayToAccount",
				rebate.getCenterRebate());
		if (rebate.getShopRebate() > 0) {
			hashOperations.increment(Constants.SHOP_ORDER_REBATE + orderInfo.getShopId(), "StayToAccount",
					rebate.getShopRebate());
		}
		if (rebate.getPushUserRebate() > 0) {
			hashOperations.increment(Constants.PUSHUSER_ORDER_REBATE + orderInfo.getPushUserId(), "StayToAccount",
					rebate.getPushUserRebate());
		}
	}

	/**
	 * @fun 计算可提现金额（从待到账金额转到可提现金额）
	 */
	private void calCanBePresented(OrderInfo orderInfo) {
		try {
			Long count = template.opsForSet().remove(Constants.ORDER_REBATE, orderInfo.getOrderId());
			if (count > 0) {
				HashOperations<String, String, String> hashOperations = template.opsForHash();
				Rebate rebate = new Rebate();
				calRebate(orderInfo, rebate, hashOperations);
				hashOperations.increment(Constants.CENTER_ORDER_REBATE + orderInfo.getCenterId(), "CaBePresented",//可提现字段
						rebate.getCenterRebate());
				hashOperations.increment(Constants.CENTER_ORDER_REBATE + orderInfo.getCenterId(), "StayToAccount",//待到账字段
						CalculationUtils.sub(0, rebate.getCenterRebate()));
				if (rebate.getShopRebate() > 0) {
					hashOperations.increment(Constants.SHOP_ORDER_REBATE + orderInfo.getShopId(), "CaBePresented",
							rebate.getShopRebate());
					hashOperations.increment(Constants.SHOP_ORDER_REBATE + orderInfo.getShopId(), "StayToAccount",
							CalculationUtils.sub(0, rebate.getShopRebate()));
				}
				if (rebate.getPushUserRebate() > 0) {
					hashOperations.increment(Constants.PUSHUSER_ORDER_REBATE + orderInfo.getPushUserId(),
							"CaBePresented", rebate.getPushUserRebate());
					hashOperations.increment(Constants.PUSHUSER_ORDER_REBATE + orderInfo.getPushUserId(),
							"StayToAccount", CalculationUtils.sub(0, rebate.getPushUserRebate()));
				}
				Map<String,String> result = packageDetailMap(orderInfo, rebate);
				template.opsForList().leftPush(Constants.REBATE_DETAIL, JSONUtil.toJson(result));
			}
		} catch (Exception e) {
			template.opsForSet().add(Constants.ORDER_REBATE, orderInfo.getOrderId());
			LogUtil.writeErrorLog("【从待到账金额转到可提现金额出错】订单号:" + orderInfo.getOrderId(), e);
		}

	}

	private Map<String, String> packageDetailMap(OrderInfo orderInfo, Rebate rebate) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("orderId", orderInfo.getOrderId());
		result.put("centerId", orderInfo.getCenterId().toString());
		result.put("centerRebateMoney", rebate.getCenterRebate() + "");
		if (orderInfo.getShopId() != null) {
			result.put("shopId", orderInfo.getShopId().toString());
			result.put("shopRebateMoney", rebate.getShopRebate() + "");
		}
		if (orderInfo.getPushUserId() != null) {
			result.put("userId", orderInfo.getPushUserId().toString());
			result.put("userRebateMoney", rebate.getPushUserRebate() + "");
		}
		return result;
	}

	private void calRebate(OrderInfo orderInfo, Rebate rebate, HashOperations<String, String, String> hashOperations) {
		List<OrderGoods> goodsList = orderInfo.getOrderGoodsList();
		if (goodsList != null) {
			double centerRebate = 0;
			double shopRebate = 0;
			double pushUserRebate = 0;
			Map<String, String> rebateMap = new HashMap<String, String>();
			for (OrderGoods goods : goodsList) {
				rebateMap = hashOperations.entries(Constants.GOODS_REBATE + goods.getGoodsId());
				if (rebateMap == null) {
					continue;
				}
				try {
					double amount = CalculationUtils.mul(goods.getItemPrice(), goods.getItemQuantity());
					centerRebate = CalculationUtils.add(centerRebate,
							CalculationUtils.mul(amount, Double.valueOf(rebateMap.get("first"))));
					if (orderInfo.getShopId() != null) {
						shopRebate = CalculationUtils.add(shopRebate,
								CalculationUtils.mul(amount, Double.valueOf(rebateMap.get("second"))));
					}
					if (orderInfo.getPushUserId() != null) {
						pushUserRebate = CalculationUtils.add(pushUserRebate,
								CalculationUtils.mul(amount, Double.valueOf(rebateMap.get("third"))));
					}
				} catch (Exception e) {
					LogUtil.writeErrorLog("【单个商品返佣计算出错】商品ID:" + goods.getGoodsId(), e);
				}
			}
			rebate.setCenterRebate(CalculationUtils.round(2, centerRebate));
			rebate.setPushUserRebate(CalculationUtils.round(2, pushUserRebate));
			rebate.setShopRebate(CalculationUtils.round(2, shopRebate));
		}
	}

	class Rebate {
		private double centerRebate;
		private double shopRebate;
		private double pushUserRebate;

		public double getCenterRebate() {
			return centerRebate;
		}

		public void setCenterRebate(double centerRebate) {
			this.centerRebate = centerRebate;
		}

		public double getShopRebate() {
			return shopRebate;
		}

		public void setShopRebate(double shopRebate) {
			this.shopRebate = shopRebate;
		}

		public double getPushUserRebate() {
			return pushUserRebate;
		}

		public void setPushUserRebate(double pushUserRebate) {
			this.pushUserRebate = pushUserRebate;
		}

	}
}
