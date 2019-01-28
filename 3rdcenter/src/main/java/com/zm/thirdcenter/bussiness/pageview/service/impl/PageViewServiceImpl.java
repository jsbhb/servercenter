package com.zm.thirdcenter.bussiness.pageview.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.thirdcenter.bussiness.dao.PageViewMapper;
import com.zm.thirdcenter.bussiness.pageview.service.PageViewService;
import com.zm.thirdcenter.pojo.PageViewBO;
import com.zm.thirdcenter.utils.IPUtils;
import com.zm.thirdcenter.utils.LogUtil;

@Service
public class PageViewServiceImpl implements PageViewService {

	@Resource
	RedisTemplate<String, String> template;
	
	@Resource
	PageViewMapper pageViewMapper;

	private static final Integer INDEX = 0;// 访问页面
	private static final Integer ENTRY = 1;// 点击链接
	private static final String REDIS_PV_PRE = "pv:pre:";
	private static final String REDIS_PV_IP = "pv:ip";

	@Override
	public void pageviewStatistics(HttpServletRequest request, String pageName, Integer type) {
		String ip = IPUtils.getOriginIp(request);
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		if (INDEX.equals(type)) {
			boolean flag = template.hasKey(REDIS_PV_PRE + pageName);
			if (flag) {
				boolean existIp = template.opsForSet().isMember(REDIS_PV_IP, ip);
				if(!existIp){
					template.opsForSet().add(REDIS_PV_IP, ip);
					hashOperations.increment(REDIS_PV_PRE + pageName, "ipNum", 1);
				}
				hashOperations.increment(REDIS_PV_PRE + pageName, "indexNum", 1);
			} else {
				Map<String, String> param = new HashMap<String, String>();
				param.put("pageName", pageName);
				param.put("indexNum", "1");
				param.put("ipNum", "1");
				template.opsForSet().add(REDIS_PV_IP, ip);
				hashOperations.putAll(REDIS_PV_PRE + pageName, param);
			}
		}
		if (ENTRY.equals(type)) {
			hashOperations.increment(REDIS_PV_PRE + pageName, "entryNum", 1);
		}
	}

	@Override
	public void persistTask() {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Set<String> keys = template.keys(REDIS_PV_PRE + "*");
		if(keys != null && keys.size() > 0){
			Map<String,String> temp = null;
			List<PageViewBO> list = null;
			for(String key : keys){
				temp = hashOperations.entries(key);
				if(temp.size() > 0){
					list = packageDate(temp);
					pageViewMapper.savePageView(list);
					pageViewMapper.saveUniqueVisitor(temp);
				}
				template.delete(key);
			}
		}
		template.delete(REDIS_PV_IP);
	}
	
	private List<PageViewBO> packageDate(Map<String,String> temp){
		List<PageViewBO> list = new ArrayList<PageViewBO>();
		PageViewBO pageViewBO = null;
		pageViewBO = new PageViewBO();
		pageViewBO.setNum(Integer.valueOf(temp.get("indexNum") == null ? "0" : temp.get("indexNum")));
		pageViewBO.setType(0);
		pageViewBO.setPageName(temp.get("pageName"));
		list.add(pageViewBO);
		pageViewBO = new PageViewBO();
		pageViewBO.setNum(Integer.valueOf(temp.get("entryNum") == null ? "0" : temp.get("entryNum")));
		pageViewBO.setType(1);
		pageViewBO.setPageName(temp.get("pageName"));
		list.add(pageViewBO);
		return list;
	}

}
