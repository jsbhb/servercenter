package com.zm.goods.bussiness.component;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zm.goods.bussiness.service.GoodsOpenInterfaceService;
import com.zm.goods.seo.service.SEOService;

@Component
public class ThreadPoolComponent {

	@Resource
	SEOService seoService;
	
	@Resource
	GoodsOpenInterfaceService goodsOpenInterfaceService;
	
	@Async("myAsync")
	public void publish(List<String> itemIdList, Integer centerId){
		seoService.publish(itemIdList, centerId, true);
	}

	@Async("myAsync")
	public void delPublish(List<String> itemIdList, Integer centerId) {
		seoService.delPublish(itemIdList, centerId);
	}
	
	@Async("myAsync")
	public void sendGoodsInfo(List<String> itemIds){
		goodsOpenInterfaceService.sendGoodsInfo(itemIds);
	}
	
	@Async("myAsync")
	public void sendGoodsInfoDownShelves(List<String> itemIds){
		goodsOpenInterfaceService.sendGoodsDownShelves(itemIds);
	}
}
