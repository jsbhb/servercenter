package com.zm.order.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.UserInfo;
import com.zm.order.pojo.bo.GradeBO;

@FeignClient("usercenter")
public interface UserFeignClient {

	@RequestMapping(value = "{version}/user/vip/{centerId}/{userId}", method = RequestMethod.GET)
	public UserInfo getVipUser(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("centerId") Integer centerId);

	@RequestMapping(value = "{version}/user/identity/{userId}", method = RequestMethod.GET)
	public UserInfo getUser(@PathVariable("version") Double version, @PathVariable("userId") Integer userId);

	@RequestMapping(value = "auth/{version}/user/register/{code}", method = RequestMethod.POST)
	public ResultModel registerUser(@PathVariable("version") Double version, @RequestBody UserInfo info,
			@PathVariable("code") String code);
	
	@RequestMapping(value = "{version}/verifyEffective/{shopId}/{pushUserId}", method = RequestMethod.GET)
	public boolean verifyEffective(@PathVariable("version") Double version, @PathVariable("shopId") Integer shopId,
			@PathVariable("pushUserId") Integer pushUserId);
	
	@RequestMapping(value = "{version}/user/feign/list-grade", method = RequestMethod.GET)
	public List<GradeBO> listGradeBO(@PathVariable("version") Double version);
	
	@RequestMapping(value = "{version}/user/feign/grade/children/{gradeId}", method = RequestMethod.GET)
	public List<Integer> listChildrenGrade(@PathVariable("version") Double version,@PathVariable("gradeId") Integer gradeId);
}
