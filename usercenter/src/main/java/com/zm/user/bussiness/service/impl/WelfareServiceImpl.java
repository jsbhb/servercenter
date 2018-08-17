package com.zm.user.bussiness.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.bussiness.dao.WelfareMapper;
import com.zm.user.bussiness.service.WelfareService;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.enummodel.NotifyTypeEnum;
import com.zm.user.feignclient.ThirdPartFeignClient;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.InviterEntity;
import com.zm.user.pojo.NotifyMsg;
import com.zm.user.pojo.bo.WelfareMembeStatistic;
import com.zm.user.utils.EncryptionUtil;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class WelfareServiceImpl implements WelfareService {

	@Resource
	WelfareMapper welfareMapper;

	@Resource
	GradeMapper<Grade> gradeMapper;

	@Resource
	ThirdPartFeignClient thirdPartFeignClient;

	@Override
	public ResultModel ImportInviterList(List<InviterEntity> importList) {
		for (InviterEntity ie : importList) {
			ie.setInvitationCode(EncryptionUtil.toSerialCode(Long.parseLong(ie.getPhone())));
			ie.setStatus(1);
		}
		welfareMapper.insertInviterInfo(importList);
		return new ResultModel(true, "");
	}

	@Override
	public Page<InviterEntity> queryForPage(InviterEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		Page<InviterEntity> page = welfareMapper.selectForPage(entity);
		return page;
	}

	@Override
	public ResultModel updateInviter(InviterEntity entity) {
		List<InviterEntity> list = new ArrayList<InviterEntity>();
		list.add(entity);
		welfareMapper.updateInviterInfo(list);
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel produceCode(InviterEntity entity) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entity", entity);
		if (entity.getIds() != null && !"".equals(entity.getIds())) {
			String[] tmpIds = entity.getIds().split(",");
			List<String> idList = Arrays.asList(tmpIds);
			param.put("list", idList);
		}
		List<InviterEntity> list = welfareMapper.selectInviterListByParam(param);
		if (list != null && list.size() > 0) {
			for (InviterEntity ie : list) {
				ie.setName(null);
				ie.setPhone(null);
				ie.setInvitationCode(EncryptionUtil.toSerialCode(ie.getId()));
				ie.setStatus(1);
				ie.setOpt(entity.getOpt());
			}
			welfareMapper.updateInviterInfo(list);
		}
		return new ResultModel(true, "");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel sendProduceCode(InviterEntity entity) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entity", entity);
		if (entity.getIds() != null && !"".equals(entity.getIds())) {
			String[] tmpIds = entity.getIds().split(",");
			List<String> idList = Arrays.asList(tmpIds);
			param.put("list", idList);
		}
		List<InviterEntity> list = welfareMapper.selectInviterListByParam(param);
		Map<Integer,String> gradeMap = new HashMap<Integer,String>();
		if (list != null && list.size() > 0) {
			List<NotifyMsg> sendList = new ArrayList<NotifyMsg>();
			NotifyMsg tmpMsg = null;
			Grade grade = null;
			for (InviterEntity ie : list) {
				if (!gradeMap.containsKey(ie.getGradeId())) {
					grade = gradeMapper.selectById(ie.getGradeId());
					gradeMap.put(ie.getGradeId(), grade.getGradeName());
				}
				tmpMsg = new NotifyMsg();
				tmpMsg.setType(NotifyTypeEnum.INVITATION_CODE);
				tmpMsg.setName(ie.getName());
				tmpMsg.setPhone(ie.getPhone());
				tmpMsg.setShopName(gradeMap.get(ie.getGradeId()));
				tmpMsg.setMsg(ie.getInvitationCode());
				sendList.add(tmpMsg);
			}

			// 调用三方接口发送邀请短信
			ResultModel thirdcenter_result = thirdPartFeignClient.sendCode(Constants.FIRST_VERSION, sendList);

			Map<String, String> remarkParam = (Map<String, String>) thirdcenter_result.getObj();

			for (InviterEntity ie : list) {
				if (remarkParam.containsKey(ie.getPhone())) {
					ie.setStatus(Constants.SEND_ERROR);
					ie.setRemark(remarkParam.get(ie.getPhone()));
					ie.setOpt(entity.getOpt());
				} else {
					ie.setStatus(Constants.SEND);
					ie.setOpt(entity.getOpt());
				}
			}

			welfareMapper.updateInviterStatus(list);
		}
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel inviterStatistic(Integer gradeId) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("gradeId", gradeId);
		List<WelfareMembeStatistic> statisticList = welfareMapper.inviterStatistic(param);
		return new ResultModel(true, statisticList);
	}
}
