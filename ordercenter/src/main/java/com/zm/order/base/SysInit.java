package com.zm.order.base;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.order.component.CacheComponent;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.pojo.bo.GradeBO;

@Component
public class SysInit {

	@Resource
	UserFeignClient userFeignClient;
	
	@PostConstruct
	public void init(){
		loadGradeBO();
	}
	
	private void loadGradeBO(){
		List<GradeBO> list = userFeignClient.listGradeBO(Constants.FIRST_VERSION);
		if(list != null && list.size() > 0){
			for(GradeBO grade : list){
				CacheComponent.getInstance().addGrade(grade);
			}
		}
	}
}
