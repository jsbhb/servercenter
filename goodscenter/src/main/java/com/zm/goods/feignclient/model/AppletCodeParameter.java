package com.zm.goods.feignclient.model;

public class AppletCodeParameter {

	private String scene;//参数
	private String page;//跳转的页面
	private String width; //二维码宽度
	private boolean auto_color; // 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false
	private boolean is_hyaline;//是否需要透明底色，为 true 时，生成透明底色的小程序码，默认 false
	private AppletCodeColor line_color;//auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示，默认全 0
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public boolean isAuto_color() {
		return auto_color;
	}
	public void setAuto_color(boolean auto_color) {
		this.auto_color = auto_color;
	}
	public boolean isIs_hyaline() {
		return is_hyaline;
	}
	public void setIs_hyaline(boolean is_hyaline) {
		this.is_hyaline = is_hyaline;
	}
	public AppletCodeColor getLine_color() {
		return line_color;
	}
	public void setLine_color(AppletCodeColor line_color) {
		this.line_color = line_color;
	}
	
}
