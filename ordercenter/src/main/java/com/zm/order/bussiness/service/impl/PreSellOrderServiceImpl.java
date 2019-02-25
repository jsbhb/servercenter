package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.order.bussiness.dao.PreSellOrderMapper;
import com.zm.order.bussiness.service.PreSellOrderService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;

@Service
public class PreSellOrderServiceImpl implements PreSellOrderService {

	@Resource
	PreSellOrderMapper preSellOrderMapper;
	
	@Resource
	GoodsFeignClient goodsFeignClient;

	@Override
	public Page<OrderInfo> queryByPage(OrderInfo entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Page<OrderInfo> page = preSellOrderMapper.selectForPage(entity);
		if (page != null && page.size() > 0) {
			List<String> orderIdList = new ArrayList<String>();
			Map<String,OrderInfo> tempOrderMap = new HashMap<String, OrderInfo>();
			for (OrderInfo info : page) {
				orderIdList.add(info.getOrderId());
				tempOrderMap.put(info.getOrderId(), info);
			}
			List<OrderGoods> goodsList = preSellOrderMapper.selectOrderGoodsByOrderIds(orderIdList);
			Map<String, List<String>> param = new HashMap<String, List<String>>();
			List<String> temp = null;
			for (OrderGoods goods : goodsList) {
				if (param.get(goods.getOrderId()) == null) {
					temp = new ArrayList<String>();
					temp.add(goods.getSpecsTpId());
					param.put(goods.getOrderId(), temp);
				} else {
					param.get(goods.getOrderId()).add(goods.getSpecsTpId());
				}
			}
			List<String> specsTpIds = goodsFeignClient.listPreSellItemIds(Constants.FIRST_VERSION);
			if(specsTpIds != null && specsTpIds.size() > 0){//有卡单的specsTpId判断哪些订单可以解除卡单
				for(Map.Entry<String, List<String>> entry : param.entrySet()){
					boolean flag = Collections.disjoint(entry.getValue(), specsTpIds);//判断两个list是否有相同的元素，没有返回true
					if(flag){
						tempOrderMap.get(entry.getKey()).setTagFun(0);
					}
				}
			} else {//没有卡单的specsTpIds则所有预售订单都可解除
				for(Map.Entry<String, OrderInfo> entry : tempOrderMap.entrySet()){
					entry.getValue().setTagFun(0);
				}
			}
		}
		return page;
	}

	@Override
	public boolean passPreSellOrder(List<String> orderIdList) {
		if(orderIdList != null && orderIdList.size() > 0){
			preSellOrderMapper.passPreSellOrder(orderIdList);
		}
		return true;
	}

}
