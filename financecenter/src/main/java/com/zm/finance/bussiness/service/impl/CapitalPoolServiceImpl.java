package com.zm.finance.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.finance.bussiness.dao.CapitalPoolMapper;
import com.zm.finance.bussiness.service.CapitalPoolService;
import com.zm.finance.constants.Constants;
import com.zm.finance.feign.UserFeignClient;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.capitalpool.CapitalPool;
import com.zm.finance.pojo.capitalpool.CapitalPoolDetail;
import com.zm.finance.util.JSONUtil;

@Service
public class CapitalPoolServiceImpl implements CapitalPoolService {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	UserFeignClient userFeignClient;
	
	@Resource
	CapitalPoolMapper capitalPoolMapper;

	@SuppressWarnings("unchecked")
	@Override
	public void updateCapitalPoolTask() {
		ResultModel resultModel = userFeignClient.getCenterId(Constants.FIRST_VERSION);
		if (resultModel.isSuccess()) {
			List<Integer> centerIdList = (List<Integer>) resultModel.getObj();
			if (centerIdList != null) {
				Map<String, String> result = new HashMap<String, String>();
				List<CapitalPool> poolList = new ArrayList<CapitalPool>();
				List<CapitalPoolDetail> detailList = new ArrayList<CapitalPoolDetail>();
				HashOperations<String, String, String> hashOperations = template.opsForHash();
				for (Integer centerId : centerIdList) {
					result = hashOperations.entries(Constants.CAPITAL_PERFIX + centerId);
					if(result != null){
						poolList.add(JSONUtil.parse(JSONUtil.toJson(result), CapitalPool.class));
					}
				}
				
				List<Object> poolDetailList = template.opsForList().range(Constants.CAPITAL_DETAIL, 0, -1);
				if(poolDetailList != null){
					for(Object obj : poolDetailList){
						detailList.add(JSONUtil.parse(obj.toString(), CapitalPoolDetail.class));
					}
				}
				capitalPoolMapper.insertCapitalPoolDetail(detailList);
				capitalPoolMapper.updateCapitalPool(poolList);
				template.opsForList().trim(Constants.CAPITAL_DETAIL, poolDetailList.size(), -1);//删除以保存的记录
			}
		}

	}

}
