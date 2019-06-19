package com.zm.finance.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.finance.bussiness.dao.RebateConsumeMapper;
import com.zm.finance.bussiness.dao.RebateMapper;
import com.zm.finance.bussiness.service.RebateService;
import com.zm.finance.constants.Constants;
import com.zm.finance.log.LogUtil;
import com.zm.finance.pojo.RebateDownload;
import com.zm.finance.pojo.RebateSearchModel;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.rebate.Rebate;
import com.zm.finance.pojo.rebate.Rebate4Order;
import com.zm.finance.pojo.rebate.RebateDetail;
import com.zm.finance.util.JSONUtil;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class RebateServiceImpl implements RebateService {

	@Resource
	RedisTemplate<String, String> template;

	@Resource
	RebateMapper rebateMapper;
	
	@Resource
	RebateConsumeMapper rebateConsumeMapper;

	@Override
	public void updateRebateTask() {

		List<Rebate> rebateList = new ArrayList<Rebate>();
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Set<String> keys = template.keys(Constants.GRADE_ORDER_REBATE + "*");
		if (keys != null && keys.size() > 0) {
			Rebate rebate = null;
			Map<String, String> result = null;
			String[] arr = null;
			for (String key : keys) {
				result = hashOperations.entries(key);
				if (result != null) {
					try {
						rebate = JSONUtil.parse(JSONUtil.toJson(result), Rebate.class);
						arr = key.split(":");
						rebate.setGradeId(Integer.valueOf(arr[arr.length - 1]));
						rebateList.add(rebate);
					} catch (Exception e) {
						LogUtil.writeErrorLog("【从redis中获取数据转换出错】", e);
					}
				}
			}
		}
		if (rebateList.size() > 0) {
			rebateMapper.insertRebate(rebateList);
		}
	}

	@Override
	public void saveRebateDetail(Map<String, String> map) {
		if (map != null && map.size() > 0) {
			List<RebateDetail> detailList = new ArrayList<RebateDetail>();
			RebateDetail rebateDetail = null;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if ("orderId".equals(entry.getKey()) || "orderFlag".equals(entry.getKey())) {
					continue;
				}
				if (entry.getValue() != null && Double.valueOf(entry.getValue()) > 0) {
					rebateDetail = new RebateDetail();
					rebateDetail.setGradeId(Integer.valueOf(entry.getKey()));
					rebateDetail.setRebateMoney(Double.valueOf(entry.getValue()));
					rebateDetail.setOrderId(map.get("orderId"));
					rebateDetail
							.setOrderFlag(map.get("orderFlag") == null ? -1 : Integer.valueOf(map.get("orderFlag")));
					detailList.add(rebateDetail);
				}
			}
			if (detailList.size() > 0) {
				rebateMapper.insertRebateDetail(detailList);
			}
		}

	}

	@Override
	public ResultModel getRebate(Integer gradeId) {
		Map<String, String> result = new HashMap<String, String>();
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		result = hashOperations.entries(Constants.GRADE_ORDER_REBATE + gradeId);
		if (result == null) {
			return new ResultModel(true, null);
		}
		Rebate rebate = JSONUtil.parse(JSONUtil.toJson(result), Rebate.class);
		rebate.setGradeId(gradeId);
		rebate.init();//把科学计数法转为正常显示
		return new ResultModel(true, rebate);
	}

	@Override
	public Page<RebateDetail> getRebateDetail(RebateDetail entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return rebateMapper.selectRebateDetailById(entity);
	}

	@Override
	public Page<Rebate> listRebate(RebateSearchModel search) {
		PageHelper.startPage(search.getCurrentPage(), search.getNumPerPage(), true);
		return rebateMapper.listRebate(search);
	}

	@Override
	public void updateRebateDetail(String orderId, Integer status) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderId", orderId);
		param.put("status", status);
		rebateMapper.updateRebateDetail(param);
	}

	@Override
	public void redisTool(String key, Map<String, String> map) {
		template.opsForHash().putAll(key, map);
	}

	@Override
	public void redisToolList(String key, String value) {
		template.opsForSet().add(key, value);
	}

	@Override
	public List<RebateDownload> listRebateDetailForDownload(Map<String,Object> param) {
		return rebateMapper.listRebateDetailForDownload(param);
	}

	@Override
	public ResultModel saveRebate4order(Rebate4Order rebate4Order) {
		orderConsumAdd(rebate4Order);
		rebateConsumeMapper.insertRebateConsume(rebate4Order);
		return new ResultModel(true);
	}

	@Override
	public void rebate4orderBatch(List<Rebate4Order> rebate4OrderList) {
		if (rebate4OrderList != null && rebate4OrderList.size() > 0){
			for (Rebate4Order rebate4Order : rebate4OrderList) {
				orderConsumAdd(rebate4Order);
			}
			rebateConsumeMapper.insertRebateConsumeBatch(rebate4OrderList);
		}
	}

	private void orderConsumAdd(Rebate4Order rebate4Order) {
		template.opsForHash().increment(Constants.GRADE_ORDER_REBATE + rebate4Order.getGradeId(),
				Constants.ORDER_CONSUME, rebate4Order.getMoney());// 增加订单消费金额
	}

}
