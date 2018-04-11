package com.zm.user.bussiness.component;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.pojo.Grade;

@Component
public class UserComponent {

	@Resource
	GradeMapper<Grade> gradeMapper;
	
	public Integer getMallId(Integer gradeId) {
		if (gradeId != null) {
			List<Grade> list = gradeMapper.listParentGradeById(gradeId);
			if(list != null && list.size() > 0){
				for(Grade grade : list){
					if(grade.getParentId() == null || grade.getParentId() == 0){
						return grade.getId();
					}
				}
			}
		}
		return null;
	}
}
