package com.zm.thirdcenter.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.thirdcenter.bussiness.dao.ExpressInfoMapper;
import com.zm.thirdcenter.bussiness.dao.LoginPluginMapper;
import com.zm.thirdcenter.cache.CacheMap;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.CarrierModel;
import com.zm.thirdcenter.pojo.ExpressInterface;
import com.zm.thirdcenter.pojo.WXLoginConfig;
import com.zm.thirdcenter.utils.ExcelUtil;

@Component
public class SysInit {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	LoginPluginMapper loginPluginMapper;
	
	@Resource
	ExpressInfoMapper expressInfoMapper;

	@PostConstruct
	public void init() {

		loadWXLoginConfig();
		
		loadExpressInfConfig();

		try {
			loadXls();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadWXLoginConfig() {
		List<WXLoginConfig> list = loginPluginMapper.listWXLoginConfig();
		for (WXLoginConfig model : list) {
			template.opsForValue().set(Constants.LOGIN + model.getCenterId() + "" + model.getLoginType(), model);
		}
	}

	/**
	 * 加载物流公司
	 * 
	 * @throws IOException
	 */
	private void loadXls() throws IOException {
		Map<String, String> carrierMap = new HashMap<String, String>();
		List<CarrierModel> list = ExcelUtil.instance().getCache("/ExpressCode.xls");
		for (CarrierModel model : list) {
			carrierMap.put(model.getCarrierName(), model.getCarrierID());
		}
		CacheMap.getCache().put(Constants.CARRIER, carrierMap);

	}
	
	private void loadExpressInfConfig() {
		List<ExpressInterface> list = expressInfoMapper.getExpressList();
		if (list == null) {
			return;
		}
		for (ExpressInterface model : list) {
			template.opsForValue().set(Constants.EXPRESS_INTERFACE + model.getExpressCode(), model);
		}
	}
}
