package com.zm.goods.enummodel;

public enum PublishType {

	REGION_CREATE("http://192.168.70.122:8888/Region/handle","POST"),
	REGION_DELETE("http://192.168.70.122:8888/Region/handle","DELETE"),
	PAGE_CREATE("http://192.168.70.122:8888/Page/handle","POST"),
	PAGE_DELETE("http://192.168.70.122:8888/Page/handle","DELETE"),
	MODULE_CREATE("http://192.168.70.122:8888/Module/data","POST"),
	MODULE_DELETE("http://192.168.70.122:8888/Module/data","DELETE");
	
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
