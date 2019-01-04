package com.zm.user.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.service.UserFeignService;
import com.zm.user.constants.Constants;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.bo.GradeBO;
import com.zm.user.pojo.bo.UserBO;
import com.zm.user.utils.ConvertUtil;
import com.zm.user.utils.JSONUtil;
import com.zm.user.utils.TreePackUtil;

@Service
public class UserFeignServiceImpl implements UserFeignService {

	@Resource
	GradeMapper<Grade> gradeMapper;
	
	@Resource
	RedisTemplate<String, String> template;
	
	@Resource
	UserMapper userMapper;

	@Override
	public List<GradeBO> listGradeBO() {
		return getListGradeBo();
	}
	
	private List<GradeBO> getListGradeBo(){
		List<Grade> list = gradeMapper.listGrade();
		List<GradeBO> gradeList = new ArrayList<GradeBO>();
		if (list != null && list.size() > 0) {
			for (Grade grade : list) {
				gradeList.add(ConvertUtil.converToGradeBO(grade));
			}
		}
		return gradeList;
	}

	@Override
	public List<Integer> listChildrenGrade(Integer gradeId) {
//		String parentIdStr = gradeMapper.listChildrenGrade(gradeId);
//		List<Integer> list = new ArrayList<Integer>();
//		if(parentIdStr == null){
//			return list;
//		}
//		String[] parentIdArr = parentIdStr.split(",");
//		for (String str : parentIdArr) {
//			if (!"$".equals(str)) {
//				try {
//					list.add(Integer.valueOf(str));
//				} catch (NumberFormatException e) {
//					LogUtil.writeErrorLog("【封装下级ID出错】===ID：" + str, e);
//				}
//			}
//		}
		List<Integer> gradeIdList = new ArrayList<Integer>();
		List<GradeBO> gradeList = getListGradeBo();
		List<GradeBO> result = new ArrayList<GradeBO>();
		TreePackUtil.packGradeChildren(gradeList, result, gradeId);
		gradeIdList.add(gradeId);
		for (GradeBO gbo :result) {
			gradeIdList.add(gbo.getId());
		}
		return gradeIdList;
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

	@Override
	public List<UserBO> listUserByUserId(List<Integer> userIdList) {
		List<UserBO> list = userMapper.listUserByUserId(userIdList);
		return list;
	}

	@Override
	public boolean verifyUserId(Integer userId) {
		int count = userMapper.verifyUserId(userId);
		if(count == 0){
			return false;
		}
		return true;
	}
}
