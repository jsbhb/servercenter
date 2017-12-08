package com.zm.order.bussiness.component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.order.bussiness.component.model.ShareProfitModel;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ProfitProportion;
import com.zm.order.pojo.UserInfo;
import com.zm.order.utils.CalculationUtils;

@Component
public class ShareProfitComponent {

	@Resource
	RedisTemplate<String, Object> redisTemplate;

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

			OrderInfo info = orderMapper.getOrderByOrderId(orderId);

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
				Double totalShareProfit = CalculationUtils.mul(totalProfit,
						profitProportion.getTotal_profit_proportion());

				if (info.getShopId() == null && user.getShopId() == null) {
					return;
				}
				boolean crossArea = false;
				if (!user.getCenterId().equals(info.getCenterId())) {
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
						setShareProfit(totalProfit, profitProportion, totalShareProfit, info, crossArea,
								user.getShopId(), info.getShopId());
					}
				}

			}

		} catch (Exception e) {
			redisTemplate.opsForList().leftPush(Constants.EXCEPTION_PROFIT, orderId);
			e.printStackTrace();
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
		redisTemplate.opsForList().rightPush(Constants.PROFIT + shopId, shareProfitModel);

		shareProfitModel = new ShareProfitModel();
		shareProfitModel.setOrderAmount(info.getOrderDetail().getPayment());
		shareProfitModel.setOrderId(info.getOrderId());
		shareProfitModel.setOrderProfit(totalProfit);
		shareProfitModel.setConsumeProfit(
				CalculationUtils.mul(totalShareProfit, profitProportion.getConsume_profit_proportion()));
		shareProfitModel.setCrossArea(crossArea);
		redisTemplate.opsForList().rightPush(Constants.PROFIT + shopId2, shareProfitModel);
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

		redisTemplate.opsForList().rightPush(Constants.PROFIT + shopId, shareProfitModel);
	}
}
