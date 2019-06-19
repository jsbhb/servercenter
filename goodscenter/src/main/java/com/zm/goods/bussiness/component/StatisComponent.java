package com.zm.goods.bussiness.component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.goods.utils.IPUtil;

@Component
public class StatisComponent {

	@Resource
	RedisTemplate<String, Object> template;

	private final String GOODS_VISIT_STATISTICS_DAY = "goodsview:day:visit:";
	private final String GOODS_PAGE_STATISTICS_DAY = "goodsview:day:page:";

	public void statisGoodsView(HttpServletRequest request, String goodsId) {
		String ip = IPUtil.getOriginIp(request);
		template.opsForHash().increment(GOODS_VISIT_STATISTICS_DAY + goodsId, ip, 1);
		template.opsForValue().increment(GOODS_PAGE_STATISTICS_DAY + goodsId, 1);
	}
}
