package com.zm.user.enummodel;

public enum PublishType {

	TEST_REGION_CREATE("http://192.168.70.122:8888/Region/handle","POST"),
	TEST_REGION_DELETE("http://192.168.70.122:8888/Region/handle","DELETE"),
	TEST_PAGE_CREATE("http://192.168.70.122:8888/Page/handle","POST"),
	TEST_PAGE_DELETE("http://192.168.70.122:8888/Page/handle","DELETE"),
	TEST_MODULE_CREATE("http://192.168.70.122:8888/Module/data","POST"),
	TEST_MODULE_DELETE("http://192.168.70.122:8888/Module/data","DELETE"),
	PRODUCE_REGION_CREATE("http://192.168.182.119:8888/Region/handle","POST"),
	PRODUCE_REGION_DELETE("http://192.168.182.119:8888/Region/handle","DELETE"),
	PRODUCE_PAGE_CREATE("http://192.168.182.119:8888/Page/handle","POST"),
	PRODUCE_PAGE_DELETE("http://192.168.182.119:8888/Page/handle","DELETE"),
	PRODUCE_MODULE_CREATE("http://192.168.182.119:8888/Module/data","POST"),
	PRODUCE_MODULE_DELETE("http://192.168.182.119:8888/Module/data","DELETE");
	
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
