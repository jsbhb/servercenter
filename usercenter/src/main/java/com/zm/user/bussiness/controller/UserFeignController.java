package com.zm.user.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.user.bussiness.service.UserFeignService;
import com.zm.user.constants.Constants;
import com.zm.user.pojo.bo.GradeBO;
import com.zm.user.pojo.bo.UserBO;
/**
 * @fun 用户中心控制器类，供feign内部调用
 * @author user
 *
 */
@RestController
public class UserFeignController {

	@Resource
	UserFeignService userFeignService;
	
	@RequestMapping(value = "{version}/user/feign/list-grade", method = RequestMethod.GET)
	public List<GradeBO> listGradeBO(@PathVariable("version") Double version){
		if(Constants.FIRST_VERSION.equals(version)){
			return userFeignService.listGradeBO();
		}
		return null;
	}
	
	@RequestMapping(value = "{version}/user/feign/grade/children/{gradeId}", method = RequestMethod.GET)
	public List<Integer> listChildrenGrade(@PathVariable("version") Double version,@PathVariable("gradeId") Integer gradeId){
		if(Constants.FIRST_VERSION.equals(version)){
			return userFeignService.listChildrenGrade(gradeId);
		}
		return null;
	}
	
	@RequestMapping(value = "{version}/user/feign/grade/buttjoint", method = RequestMethod.GET)
	public boolean initButtjoint(@PathVariable("version") Double version){
		if(Constants.FIRST_VERSION.equals(version)){
			return userFeignService.initButtjoint();
		}
		return false;
	}
	
	@RequestMapping(value = "{version}/user/feign/user/bo", method = RequestMethod.POST)
	public List<UserBO> listUserByUserId(@PathVariable("version") Double version, @RequestBody List<Integer> userIdList){
		if(Constants.FIRST_VERSION.equals(version)){
			return userFeignService.listUserByUserId(userIdList);
		}
		return null;
	}
	
	@RequestMapping(value = "{version}/user/feign/verify/{userId}", method = RequestMethod.GET)
	public boolean verifyUserId(@PathVariable("version") Double version, @PathVariable("userId") Integer userId){
		if(Constants.FIRST_VERSION.equals(version)){
			return userFeignService.verifyUserId(userId);
		}
		return false;
	}
}
