package com.zm.user.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.user.bussiness.service.UserFeignService;
import com.zm.user.constants.Constants;
import com.zm.user.pojo.bo.GradeBO;
import com.zm.user.utils.JSONUtil;

@Component
public class SysInit {

	@Resource
	RedisTemplate<String, String> template;
	
	@Resource
	UserFeignService userFeignService;
	
	@PostConstruct
	public void init(){
		loadGradeBO();
	}
	
	private void loadGradeBO(){
		List<GradeBO> list = userFeignService.listGradeBO();
		if(list != null && list.size() > 0){
			Map<String,String> param = new HashMap<String,String>();
			for(GradeBO grade : list){
				param.put(grade.getId()+"", JSONUtil.toJson(grade));
			}
			template.opsForHash().putAll(Constants.GRADEBO_INFO, param);
		}
	} 
}
