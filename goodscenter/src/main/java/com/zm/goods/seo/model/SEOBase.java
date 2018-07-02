package com.zm.goods.seo.model;

import com.zm.goods.enummodel.SystemEnum;

public class SEOBase {

	private String system;
	
	public SEOBase(SystemEnum system){
		this.system = system.getName();
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
	
}
