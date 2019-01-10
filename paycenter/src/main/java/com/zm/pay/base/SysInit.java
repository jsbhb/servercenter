package com.zm.pay.base;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.pay.bussiness.dao.PayMapper;
import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.CustomConfig;
import com.zm.pay.pojo.UnionPayConfig;
import com.zm.pay.pojo.WeixinPayConfig;
import com.zm.pay.pojo.YopConfigModel;

@Component
public class SysInit {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	PayMapper payMapper;

	@PostConstruct
	public void init() {

		loadAliPayConfig();//加载阿里支付配置

		loadWeixinPayConfig();//加载微信支付配置

		loadUnionPayConfig();//加载银联支付配置

		loadYopPayConfig();//加载易宝支付配置
		
		loadCustomConfig();//加载报关信息配置
	}

	private void loadYopPayConfig() {
		List<YopConfigModel> list = payMapper.listYopPayConfig();
		for (YopConfigModel model : list) {
			template.opsForValue().set(Constants.PAY + model.getCenterId() + Constants.YOP_PAY, model);
		}
	}

	private void loadAliPayConfig() {
		List<AliPayConfigModel> list = payMapper.listAliPayConfig();
		for (AliPayConfigModel model : list) {
			template.opsForValue().set(Constants.PAY + model.getCenterId() + Constants.ALI_PAY, model);
		}
	}

	private void loadWeixinPayConfig() {
		List<WeixinPayConfig> list = payMapper.listWeixinPayConfig();
		for (WeixinPayConfig model : list) {
			template.opsForValue().set(Constants.PAY + model.getCenterId() + Constants.WX_PAY, model);
		}
	}

	private void loadUnionPayConfig() {
		List<UnionPayConfig> list = payMapper.listUnionPayConfig();
		for (UnionPayConfig model : list) {
			template.opsForValue().set(Constants.PAY + model.getCenterId() + Constants.UNION_PAY, model);
			template.opsForValue().set(Constants.PAY + model.getCenterId() + Constants.UNION_PAY_MER_ID,
					model.getMerId());
		}
	}

	private void loadCustomConfig() {
		List<CustomConfig> list = payMapper.listCustomConfig();
		for (CustomConfig model : list) {
			template.opsForValue().set(Constants.CUSTOM_CONFIG + model.getSupplierId(), model);
		}
	}
}
