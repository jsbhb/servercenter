package com.zm.user.bussiness.service.impl;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.zm.user.bussiness.component.UserComponent;
import com.zm.user.bussiness.dao.GradeFrontMapper;
import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.service.GradeFrontService;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.feignclient.ThirdPartFeignClient;
import com.zm.user.feignclient.model.AppletCodeParameter;
import com.zm.user.pojo.GradeConfig;
import com.zm.user.pojo.UserInfo;

@Service
public class GradeFrontServiceImpl implements GradeFrontService {

	@Resource
	GradeFrontMapper gradeFrontMapper;
	
	@Resource
	UserMapper userMapper;
	
	@Resource
	UserComponent userComponent;
	
	@Resource
	ThirdPartFeignClient thirdPartFeignClient;
	
	@Override
	public GradeConfig getGradeConfig(Integer mallId, Integer shopId, Integer userId) {
		if(shopId != null){
			Integer tmallId = userComponent.getMallId(shopId);
			if(!mallId.equals(tmallId)){
				return null;
			}
			return gradeFrontMapper.getGradeConfig(shopId);
		}
		if(userId != null){
			UserInfo user = userMapper.getUserInfo(userId);
			if(!mallId.equals(user.getCenterId())){
				return null;
			}
			if(user.getShopId() == null){
				return null;
			}
			return gradeFrontMapper.getGradeConfig(user.getShopId());
		}
		return null;
	}

	@Override
	public String getClientUrl(Integer mallId) {
		return gradeFrontMapper.getClientUrlById(mallId);
	}

	@Override
	public String getMobileUrl(Integer shopId) {
		Integer parentId = userMapper.getParentIdByGradeId(shopId);
		
		return gradeFrontMapper.getMobileUrl(parentId);
	}
	
	@Override
	public byte[] getShopBillboard(Integer shopId) {
		// 获取小程序二维码流
//		InputStream in = null;
		// 生成二维码
		AppletCodeParameter param = new AppletCodeParameter();
		param.setScene("shopId=" + shopId);
		param.setPage("separate/joinUs/joinUs");
		param.setWidth("400");
		ResultModel model = thirdPartFeignClient.getAppletCode(Constants.FIRST_VERSION, param);
		if (!model.isSuccess()) {
			throw new RuntimeException(model.getErrorCode() + "==" + model.getErrorMsg());
		}
		// 图片字符串需base64解码
		Base64 base = new Base64();
//		in = new ByteArrayInputStream(base.decode(model.getObj().toString()));
		byte[] result = base.decode(model.getObj().toString());
		return result;
	}

}
