package com.zm.user.pojo;

/**  
 * ClassName: UserCollection <br/>  
 * Function: 用户收藏. <br/>   
 * date: Aug 16, 2017 3:04:48 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public class UserCollection {

	private Integer id;
	
	private Integer userId;
	
	private Integer type;
	
	private Integer typeId;
	
	private String typeName;
	
	private String typeUrl;
	
	private String createTime;

	private String updateTime;

	private String opt;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeUrl() {
		return typeUrl;
	}

	public void setTypeUrl(String typeUrl) {
		this.typeUrl = typeUrl;
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
		return "UserCollection [id=" + id + ", userId=" + userId + ", type=" + type + ", typeId=" + typeId
				+ ", typeName=" + typeName + ", typeUrl=" + typeUrl + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", opt=" + opt + "]";
	}
	
}
