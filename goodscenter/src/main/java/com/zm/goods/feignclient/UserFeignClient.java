package com.zm.goods.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.goods.feignclient.model.RebateFormula;
import com.zm.goods.feignclient.model.UserBO;
import com.zm.goods.pojo.ResultModel;

@FeignClient("usercenter")
public interface UserFeignClient {

	@RequestMapping(value = "{version}/user/center", method = RequestMethod.GET)
	public ResultModel getCenterId(@PathVariable("version") Double version);

	@RequestMapping(value = "{version}/grade-url/{centerId}", method = RequestMethod.GET)
	public String getClientUrl(@PathVariable("centerId") Integer centerId, @PathVariable("version") Double version);

	@RequestMapping(value = "{version}/user/feign/grade/buttjoint", method = RequestMethod.GET)
	public boolean initButtjoint(@PathVariable("version") Double version);

	@RequestMapping(value = "{version}/user/feign/user/bo", method = RequestMethod.POST)
	public List<UserBO> listUserByUserId(@PathVariable("version") Double version,
			@RequestBody List<Integer> userIdList);

	@RequestMapping(value = "{version}/user/feign/verify/{userId}", method = RequestMethod.GET)
	public boolean verifyUserId(@PathVariable("version") Double version, @PathVariable("userId") Integer userId);

	@RequestMapping(value = "{version}/grade/rebate/formula/{needPaging}", method = RequestMethod.POST)
	public ResultModel listGradeTypeRebateFormula(@PathVariable("version") Double version,
			@PathVariable("needPaging") boolean needPaging, @RequestBody RebateFormula rebateFormula);

	@RequestMapping(value = "{version}/user/feign/grade-type/{gradeId}", method = RequestMethod.GET)
	public int getGradeTypeIdByGradeId(@PathVariable("version") Double version,
			@PathVariable("gradeId") Integer gradeId);
}
