package com.zm.supplier.supplierinf.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.HttpClientUtil;

@Component
public class ZhengZhengButtjoint extends AbstractSupplierButtJoint {

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user) {
		String msg = ButtJointMessageUtils.getZhengZhengOrderMsg(info, user, accountId, memberId);
		return sendZhengZhengWarehouse(url, msg, SendOrderResult.class);
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private <T> Set<T> sendZhengZhengWarehouse(String url, String msg, Class<T> clazz) {

		String result = HttpClientUtil.post(url, msg,"application/x-www-form-urlencoded");
		logger.info("返回：===" + result);

		try {
			return renderResult(result, "JSON", clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
