package com.zm.thirdcenter.bussiness.customs.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @fun 海关实时支付数据商品信息
 * @author user
 *
 */
public class GoodsInfo {

	private String gname;
	private String itemLink;
	public void encode(){
		try {
			gname = URLEncoder.encode(gname, "UTF-8");
			itemLink = URLEncoder.encode(itemLink, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getItemLink() {
		return itemLink;
	}
	public void setItemLink(String itemLink) {
		this.itemLink = itemLink;
	}
}
