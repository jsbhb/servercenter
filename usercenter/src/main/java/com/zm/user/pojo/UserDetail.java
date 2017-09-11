package com.zm.user.pojo;


/**  
 * ClassName: UserDetail <br/>  
 * Function: 用户详情. <br/>   
 * date: Aug 16, 2017 2:44:24 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public class UserDetail {
	
	private Integer id;
	
	private Integer userId;
	
	private String name;
	
	private String nickName;
	
	private String company;
	
	private String location;
	
	private String headImg;
	
	private Integer certificates;
	
	private String idNum;
	
	private Integer sex;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getCertificates() {
		return certificates;
	}

	public void setCertificates(Integer certificates) {
		this.certificates = certificates;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	@Override
	public String toString() {
		return "UserDetail [id=" + id + ", userId=" + userId + ", name=" + name + ", nickName="
				+ nickName + ", company=" + company + ", location=" + location + ", headImg=" + headImg
				+ ", certificates=" + certificates + ", idNum=" + idNum + ", sex=" + sex + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", opt=" + opt + "]";
	}
	
}
