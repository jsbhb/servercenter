package com.zm.thirdcenter.bussiness.customs.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @fun 海关实时数据信息实体
 * @author user
 *
 */
@JsonInclude(Include.NON_NULL) 
public class PayExchangeInfo {

	private String orderNo;
	private List<GoodsInfo> goodsInfo;
	private String recpAccount;
	private String recpCode;
	private String recpName;
	
	public void encode(){
		try {
			recpName = URLEncoder.encode(recpName, "UTF-8");
			for(GoodsInfo info : goodsInfo){
				info.encode();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public List<GoodsInfo> getGoodsInfo() {
		return goodsInfo;
	}
	public void setGoodsInfo(List<GoodsInfo> goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
	public String getRecpAccount() {
		return recpAccount;
	}
	public void setRecpAccount(String recpAccount) {
		this.recpAccount = recpAccount;
	}
	public String getRecpCode() {
		return recpCode;
	}
	public void setRecpCode(String recpCode) {
		this.recpCode = recpCode;
	}
	public String getRecpName() {
		return recpName;
	}
	public void setRecpName(String recpName) {
		this.recpName = recpName;
	}
}
