package com.zm.thirdcenter.bussiness.express.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.thirdcenter.bussiness.dao.ExpressInfoMapper;
import com.zm.thirdcenter.bussiness.express.service.ExpressInfoService;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.expressinf.AbstractExpressButtJoint;
import com.zm.thirdcenter.pojo.ExpressInfoResult;
import com.zm.thirdcenter.pojo.ExpressInterface;
import com.zm.thirdcenter.pojo.OrderInfo;
import com.zm.thirdcenter.utils.SpringContextUtil;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ExpressInfoServiceImpl implements ExpressInfoService {
	
	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	ExpressInfoMapper expressInfoMapper;

	@Override
	public Map<String, Object> createExpressInfoByExpressCode(List<OrderInfo> infoList, String expressCode) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		AbstractExpressButtJoint buttJoint = getTargetInterface(expressCode.toLowerCase());
		if (buttJoint == null) {
			return null;
		}
		Set<ExpressInfoResult> set = buttJoint.createExpressInfo(infoList);
		if (set == null || set.size() == 0) {
			return null;
		}
		for (ExpressInfoResult expressInfo : set) {
			resultMap.put(expressInfo.getOrderId(), expressInfo.getExpressNo());
		}
		return resultMap;
	}
	
	private AbstractExpressButtJoint getTargetInterface(String expressCode) {
		ExpressInterface inf = (ExpressInterface) template.opsForValue().get(Constants.EXPRESS_INTERFACE + expressCode);
		if (inf == null) {
			inf = expressInfoMapper.getExpressInterface(expressCode);
			if (inf == null) {
				return null;
			}
			template.opsForValue().set(Constants.EXPRESS_INTERFACE + expressCode, inf);
		}
		AbstractExpressButtJoint buttJoint = (AbstractExpressButtJoint) SpringContextUtil
				.getBean(inf.getTargetObject());
		if (buttJoint == null) {
			return null;
		}
		buttJoint.init(inf);
		return buttJoint;
	}
}
