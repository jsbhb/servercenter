package com.zm.user.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.bussiness.service.UserFeignService;
import com.zm.user.constants.Constants;
import com.zm.user.log.LogUtil;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.bo.GradeBO;
import com.zm.user.utils.ConvertUtil;
import com.zm.user.utils.JSONUtil;

@Service
public class UserFeignServiceImpl implements UserFeignService {

	@Resource
	GradeMapper<Grade> gradeMapper;
	
	@Resource
	RedisTemplate<String, String> template;

	@Override
	public List<GradeBO> listGradeBO() {
		List<Grade> list = gradeMapper.listGrade();
		List<GradeBO> gradeList = new ArrayList<GradeBO>();
		if (list != null && list.size() > 0) {
			GradeBO gradeBO = null;
			for (Grade grade : list) {
				gradeBO = new GradeBO();
				gradeBO.setId(grade.getId());
				gradeBO.setGradeType(grade.getGradeType());
				gradeBO.setParentId(grade.getParentId());
				gradeBO.setName(grade.getGradeName());
				gradeBO.setCompany(grade.getCompany());
				gradeBO.setGradeTypeName(grade.getGradeTypeName());
				gradeBO.setType(grade.getType());
				gradeList.add(gradeBO);
			}
		}
		return gradeList;
	}

	@Override
	public List<Integer> listChildrenGrade(Integer gradeId) {
		String parentIdStr = gradeMapper.listChildrenGrade(gradeId);
		List<Integer> list = new ArrayList<Integer>();
		if(parentIdStr == null){
			return list;
		}
		String[] parentIdArr = parentIdStr.split(",");
		for (String str : parentIdArr) {
			if (!"$".equals(str)) {
				try {
					list.add(Integer.valueOf(str));
				} catch (NumberFormatException e) {
					LogUtil.writeErrorLog("【封装下级ID出错】===ID：" + str, e);
				}
			}
		}
		return list;
	}

	@Override
	public boolean initButtjoint() {
		List<Grade> list = gradeMapper.listButtjointGrade();
		if(list != null && list.size() > 0){
			for(Grade grade : list){
				template.opsForSet().add(Constants.BUTT_JOINT_USER_PREFIX,
						JSONUtil.toJson(ConvertUtil.converToButtjoinUserBO(grade)));
			}
		}
		return true;
	}
}
