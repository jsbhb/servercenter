package com.zm.goods.bussiness.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.component.IndexAutoConfigFactory;
import com.zm.goods.bussiness.component.base.IindexAutoConfig;
import com.zm.goods.bussiness.dao.SEOMapper;
import com.zm.goods.bussiness.service.IndexAutoConfigService;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.po.PagePO;

@Service
public class IndexAutoConfigServiceImpl implements IndexAutoConfigService {

	@Resource
	SEOMapper seoMapper;

	@Override
	public boolean autoConfig() {
		List<PagePO> pageList = seoMapper.listPublishedIndexPageDetail();
		if (pageList == null) {
			LogUtil.writeLog("没有已经发布的模板");
			return false;
		}
		pageList.stream().forEach(po -> {
			if (po.getModule() != null) {
				po.getModule().stream().forEach(module -> {
					IindexAutoConfig auto = IndexAutoConfigFactory.get(module.getKey());
					if(auto != null){
						auto.autoConfig(module, po.getClient());
					}
				});
			}
		});
		return true;
	}
}
