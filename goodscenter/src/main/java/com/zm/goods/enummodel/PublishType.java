package com.zm.goods.enummodel;

public enum PublishType {

	//test
	REGION_CREATE("http://192.168.70.122:8888/Region/handle","POST"),
	REGION_DELETE("http://192.168.70.122:8888/Region/handle","DELETE"),
	PAGE_CREATE("http://192.168.70.122:8888/Page/handle","POST"),
	PAGE_DELETE("http://192.168.70.122:8888/Page/handle","DELETE"),
	MODULE_CREATE("http://192.168.70.122:8888/Data/handle/nav","POST"),
	MODULE_DELETE("http://192.168.70.122:8888/Module/data","DELETE"),
	DISTRIBUTION_CREATE("http://192.168.70.122:8888/Data/handle/distribution","POST"),
	DISTRIBUTION_DELETE("http://192.168.70.122:8888/Data/handle/distribution","DELETE"),
	ADD_SITEMAP("http://192.168.70.122:8888/Sitemap/handle","POST"),
	DEL_SITEMAP("http://192.168.70.122:8888/Sitemap/handle","DELETE");
	
	//produce
//	REGION_CREATE("http://192.168.182.119:8888/Region/handle","POST"),
//	REGION_DELETE("http://192.168.182.119:8888/Region/handle","DELETE"),
//	PAGE_CREATE("http://192.168.182.119:8888/Page/handle","POST"),
//	PAGE_DELETE("http://192.168.182.119:8888/Page/handle","DELETE"),
//	MODULE_CREATE("http://192.168.182.119:8888/Data/handle/nav","POST"),
//	MODULE_DELETE("http://192.168.182.119:8888/Module/data","DELETE"),
//	DISTRIBUTION_CREATE("http://192.168.182.119:8888/Data/handle/distribution","POST"),
//	DISTRIBUTION_DELETE("http://192.168.182.119:8888/Data/handle/distribution","DELETE"),
//	ADD_SITEMAP("http://192.168.182.119:8888/Sitemap/handle","POST"),
//	DEL_SITEMAP("http://192.168.182.119:8888/Sitemap/handle","DELETE");
	
	private String url;
	private String method;
	private PublishType(String url,String method){
		this.url = url;
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
}
