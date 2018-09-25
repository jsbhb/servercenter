package com.zm.user.enummodel;

public enum PublishType {

	//test
		REGION_CREATE("https://testfront.cncoopbuy.com/Region/handle","POST"),
		REGION_DELETE("https://testfront.cncoopbuy.com/Region/handle","DELETE"),
		PAGE_CREATE("https://testfront.cncoopbuy.com/Page/handle","POST"),
		PAGE_DELETE("https://testfront.cncoopbuy.com/Page/handle","DELETE"),
		MODULE_CREATE("https://testfront.cncoopbuy.com/Data/handle/nav","POST"),
		MODULE_DELETE("https://testfront.cncoopbuy.com/Module/data","DELETE"),
		DISTRIBUTION_CREATE("https://testfront.cncoopbuy.com/Data/handle/distribution","POST"),
		DISTRIBUTION_DELETE("https://testfront.cncoopbuy.com/Data/handle/distribution","DELETE"),
		ADD_SITEMAP("https://testfront.cncoopbuy.com/Sitemap/handle","POST"),
		DEL_SITEMAP("https://testfront.cncoopbuy.com/Sitemap/handle","DELETE");
		
		//produce
//		REGION_CREATE("https://front.cncoopbuy.com/Region/handle","POST"),
//		REGION_DELETE("https://front.cncoopbuy.com/Region/handle","DELETE"),
//		PAGE_CREATE("https://front.cncoopbuy.com/Page/handle","POST"),
//		PAGE_DELETE("https://front.cncoopbuy.com/Page/handle","DELETE"),
//		MODULE_CREATE("https://front.cncoopbuy.com/Data/handle/nav","POST"),
//		MODULE_DELETE("https://front.cncoopbuy.com/Module/data","DELETE"),
//		DISTRIBUTION_CREATE("https://front.cncoopbuy.com/Data/handle/distribution","POST"),
//		DISTRIBUTION_DELETE("https://front.cncoopbuy.com/Data/handle/distribution","DELETE"),
//		ADD_SITEMAP("https://front.cncoopbuy.com/Sitemap/handle","POST"),
//		DEL_SITEMAP("https://front.cncoopbuy.com/Sitemap/handle","DELETE");
	
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
