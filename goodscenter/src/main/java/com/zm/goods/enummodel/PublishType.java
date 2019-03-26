package com.zm.goods.enummodel;

public enum PublishType {

	//test
	REGION_CREATE("https://testfront.cncoopay.com/Region/handle","POST"),
	REGION_DELETE("https://testfront.cncoopay.com/Region/handle","DELETE"),
	PAGE_CREATE("https://testfront.cncoopay.com/Page/handle","POST"),
	PAGE_DELETE("https://testfront.cncoopay.com/Page/handle","DELETE"),
	MODULE_CREATE("https://testfront.cncoopay.com/Data/handle/nav","POST"),
	MODULE_DELETE("https://testfront.cncoopay.com/Module/data","DELETE"),
	DISTRIBUTION_CREATE("https://testfront.cncoopay.com/Data/handle/distribution","POST"),
	DISTRIBUTION_DELETE("https://testfront.cncoopay.com/Data/handle/distribution","DELETE"),
	ADD_SITEMAP("https://testfront.cncoopay.com/Sitemap/handle","POST"),
	DEL_SITEMAP("https://testfront.cncoopay.com/Sitemap/handle","DELETE");
	
	//produce
//	REGION_CREATE("https://front.cncoopay.com/Region/handle","POST"),
//	REGION_DELETE("https://front.cncoopay.com/Region/handle","DELETE"),
//	PAGE_CREATE("https://front.cncoopay.com/Page/handle","POST"),
//	PAGE_DELETE("https://front.cncoopay.com/Page/handle","DELETE"),
//	MODULE_CREATE("https://front.cncoopay.com/Data/handle/nav","POST"),
//	MODULE_DELETE("https://front.cncoopay.com/Module/data","DELETE"),
//	DISTRIBUTION_CREATE("https://front.cncoopay.com/Data/handle/distribution","POST"),
//	DISTRIBUTION_DELETE("https://front.cncoopay.com/Data/handle/distribution","DELETE"),
//	ADD_SITEMAP("https://front.cncoopay.com/Sitemap/handle","POST"),
//	DEL_SITEMAP("https://front.cncoopay.com/Sitemap/handle","DELETE");
	
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
