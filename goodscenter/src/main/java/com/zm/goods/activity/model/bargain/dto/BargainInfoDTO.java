package com.zm.goods.activity.model.bargain.dto;

/**
 * @fun 接收前端砍价参数
 * @author user
 *
 */
public class BargainInfoDTO {

	private Integer id;//开团时接收goodsRoleId，砍价时接收该团的ID
	private String userName;//用户的微信名称
	private String userImg;//用户的微信头像（系统中的头像都是默认头像，所以不用）
	private Integer userId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
