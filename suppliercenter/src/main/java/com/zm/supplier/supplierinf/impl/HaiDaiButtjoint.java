package com.zm.supplier.supplierinf.impl;

import java.util.List;
import java.util.Set;

import org.mockito.internal.creation.util.SearchingClassLoader;
import org.springframework.stereotype.Component;

import com.seatent.opensdk.api.HDServiceProvider;
import com.seatent.opensdk.input.hdServiceProvider.CreateOrderInputDto;
import com.seatent.opensdk.input.hdServiceProvider.GetGoodsInfoApiInputDto;
import com.seatent.opensdk.input.hdServiceProvider.GetGoodsListBySolrInputDto;
import com.seatent.opensdk.input.hdServiceProvider.GetTopCategoryApiInputDto;
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
public class HaiDaiButtjoint extends AbstractSupplierButtJoint {

	private HDServiceProvider hd = new HDServiceProvider();

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user) {
		CreateOrderInputDto dto = ButtJointMessageUtils.getHaiDaiOrderDto(info, user, this);
		String result = hd.createOrders(dto);
		try {
			return renderResult(result, "JSON", SendOrderResult.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		
		return null;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		GetGoodsInfoApiInputDto dto = ButtJointMessageUtils.getHaiDaiGoodsInfoDto(itemCode, this);
		String result = hd.getGoodsInfo(dto);
		
		return null;
	}

	public static void main(String[] args) {
		String appKey = "93029834";
		String secret = "215146456bf94e0093540f3645610c95";
		String url = "http://api.pre.seatent.com";
		String accountId = "38d2a151fe4c4ad2b9e20c4bc6988112";
		String memberId = "38d2a151fe4c4ad2b9e20c4bc6988112";
		HDServiceProvider hd = new HDServiceProvider();

		GetGoodsInfoApiInputDto gdto = new GetGoodsInfoApiInputDto();
//		GetGoodsListBySolrInputDto dto = new GetGoodsListBySolrInputDto();
//		dto.setAccountId(accountId);
//		dto.setAppkey(appKey);
//		dto.setSecret(secret);
//		dto.setUrl(url);
//		dto.setMemberId(memberId);
//		dto.setKeyword("奶粉");
//		String str = hd.searchingGoods(dto);
//		System.out.println(str);
		
		gdto.setAccountId(accountId);
		gdto.setAppkey(appKey);
		gdto.setMemberId(memberId);
		gdto.setUrl(url);
		gdto.setSecret(secret);
		gdto.setGoodsId("8c97880d0ebb4183a182181170574311");
		gdto.setNeedSEO(0);
		gdto.setOnlyEnableStore(0);
		String str = hd.getGoodsInfo(gdto);
		System.out.println(str);
	}

}
