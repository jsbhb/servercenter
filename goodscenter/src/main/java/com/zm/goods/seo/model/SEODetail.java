package com.zm.goods.seo.model;

import java.util.ArrayList;
import java.util.List;

import com.zm.goods.enummodel.SystemEnum;
import com.zm.goods.pojo.po.ComponentPagePO;
import com.zm.goods.pojo.po.PagePO;

public class SEODetail extends SEOBase {

	private SEOModel seo;
	private String page;
	private String file;
	private String path;
	private String region;
	private List<SEODataModel> module = new ArrayList<SEODataModel>();

	public SEODetail(Object item, SEOModel seoModel, String file, String path, SystemEnum system,
			List<PagePO> pageList) {
		super(system);
		this.seo = seoModel;
		this.file = file;
		this.path = path;
		switch (system) {
		case PCMALL:
			initData(item, pageList,SystemEnum.PCMALL.getName());
			break;
		case MPMALL:
			initData(item, pageList,SystemEnum.MPMALL.getName());
			break;
		case FMPMALL:
			initData(item, pageList,SystemEnum.FMPMALL.getName());
		}

	}

	private void initData(Object item, List<PagePO> pageList, String system) {
		PagePO page = null;
		for(PagePO tem : pageList){
			if(system.contains(tem.getClient())){
				page = tem;
				break;
			}
		}
		this.page = page.getPage();
		if(this.file == null){
			this.file = page.getFile();
		}
		SEODataModel model = null;
		for(ComponentPagePO tem : page.getModule()){
			if(tem.getKey().contains("goodsDetail")){
				model = new SEODataModel(item, tem);
			} else {
				model = new SEODataModel(null, tem);
			}
			module.add(model);
		}
	}

	public SEOModel getSeo() {
		return seo;
	}

	public void setSeo(SEOModel seo) {
		this.seo = seo;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public List<SEODataModel> getModule() {
		return module;
	}

	public void setModule(List<SEODataModel> module) {
		this.module = module;
	}

}
