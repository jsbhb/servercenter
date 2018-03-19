package com.zm.finance.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.finance.bussiness.dao.RebateMapper;
import com.zm.finance.bussiness.service.RebateService;
import com.zm.finance.constants.Constants;
import com.zm.finance.log.LogUtil;
import com.zm.finance.pojo.rebate.CenterRebate;
import com.zm.finance.pojo.rebate.PushUserRebate;
import com.zm.finance.pojo.rebate.RebateDetail;
import com.zm.finance.pojo.rebate.ShopRebate;
import com.zm.finance.util.JSONUtil;

@Service
public class RebateServiceImpl implements RebateService {

	@Resource
	RedisTemplate<String, Object> template;
	
	@Resource
	RebateMapper rebateMapper;

	@Override
	public void updateRebateTask() {

		List<CenterRebate> centerRebateList = new ArrayList<CenterRebate>();
		List<ShopRebate> shopRebateList = new ArrayList<ShopRebate>();
		List<PushUserRebate> pushUserRebateList = new ArrayList<PushUserRebate>();
		List<RebateDetail> detailList = new ArrayList<RebateDetail>();
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		packageCenterRebate(centerRebateList, hashOperations);//获取区域中心返佣
		packageShopRebate(shopRebateList, hashOperations);//获取店铺返佣
		packagePushUserRebate(pushUserRebateList, hashOperations);//获取推手返佣
		List<Object> rebateDetailList = template.opsForList().range(Constants.REBATE_DETAIL, 0, -1);
		if (rebateDetailList != null) {
			for (Object obj : rebateDetailList) {
				detailList.add(JSONUtil.parse(obj.toString(), RebateDetail.class));
			}
		}
		rebateMapper.insertCenterRebate(centerRebateList);
		rebateMapper.insertPushUserRebate(pushUserRebateList);
		rebateMapper.insertShopRebate(shopRebateList);
		rebateMapper.insertRebateDetail(detailList);
		
		template.opsForList().trim(Constants.REBATE_DETAIL, rebateDetailList.size(), -1);// 删除以保存的记录

	}

	private void packageShopRebate(List<ShopRebate> shopRebateList,
			HashOperations<String, String, String> hashOperations) {
		Map<String, String> result;
		Set<String> shopkeys = template.keys(Constants.SHOP_ORDER_REBATE + "*");
		String[] arr = null;
		if (shopkeys != null) {// 店铺返佣
			ShopRebate temp = null;
			for (String key : shopkeys) {
				result = hashOperations.entries(key);
				if (result != null) {
					try {
						temp = JSONUtil.parse(JSONUtil.toJson(result), ShopRebate.class);
						arr = key.split(":");
						temp.setShopId(Integer.valueOf(arr[arr.length - 1]));
						shopRebateList.add(temp);
					} catch (Exception e) {
						LogUtil.writeErrorLog("【从redis中获取数据转换出错】", e);
					}

				}
			}
		}
	}
	
	private void packagePushUserRebate(List<PushUserRebate> pushUserRebateList,
			HashOperations<String, String, String> hashOperations) {
		Map<String, String> result;
		Set<String> userkeys = template.keys(Constants.PUSHUSER_ORDER_REBATE + "*");
		String[] arr = null;
		if (userkeys != null) {// 店铺返佣
			PushUserRebate temp = null;
			for (String key : userkeys) {
				result = hashOperations.entries(key);
				if (result != null) {
					try {
						temp = JSONUtil.parse(JSONUtil.toJson(result), PushUserRebate.class);
						arr = key.split(":");
						temp.setUserId(Integer.valueOf(arr[arr.length - 1]));
						pushUserRebateList.add(temp);
					} catch (Exception e) {
						LogUtil.writeErrorLog("【从redis中获取数据转换出错】", e);
					}

				}
			}
		}
	}

	private void packageCenterRebate(List<CenterRebate> centerRebateList,
			HashOperations<String, String, String> hashOperations) {
		Map<String, String> result;
		String[] arr = null;
		Set<String> centerkeys = template.keys(Constants.CENTER_ORDER_REBATE + "*");
		if (centerkeys != null) {// 区域中心返佣
			CenterRebate temp = null;
			for (String key : centerkeys) {
				result = hashOperations.entries(key);
				if (result != null) {
					try {
						temp = JSONUtil.parse(JSONUtil.toJson(result), CenterRebate.class);
						arr = key.split(":");
						temp.setCenterId(Integer.valueOf(arr[arr.length - 1]));
						centerRebateList.add(temp);
					} catch (Exception e) {
						LogUtil.writeErrorLog("【从redis中获取数据转换出错】", e);
					}

				}
			}
		}
	}

}
