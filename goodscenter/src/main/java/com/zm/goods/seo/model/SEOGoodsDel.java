package com.zm.goods.seo.model;

import com.zm.goods.enummodel.SystemEnum;

public class SEOGoodsDel extends SEOBase{

	private String page;
	private String file;
	private String path;
	private String region;
	
	public SEOGoodsDel(String path, SystemEnum system,String file){
		super(system);
		this.page = "goodsDetail";
		this.file = file;
		this.path = path;
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
	
}
