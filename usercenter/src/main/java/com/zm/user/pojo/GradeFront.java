package com.zm.user.pojo;

/**
 * @fun 前端申请微店实体对象
 * @author user
 *
 */
public class GradeFront {

	private Integer id;
	// 申请人的微信号
	private String wechat;
	// 父级ID（当前的分级ID，后天审核可修改）
	private Integer parentId;
	// 分级类型（由微服务确定，获取逻辑为父级ID的子分级类型）
	private Integer gradeType;
	// 分级名称
	private String gradeName;
	//负责人姓名
	private String personInCharge;
	//负责人手机
	private String phone;
	//负责人公司名称（选填）
	private String company;
	//负责人所在省
	private String province;
	//负责人所在市
	private String city;
	//负责人所在区
	private String district;
	//负责人所在地址
	private String address;
	//身份证照片存放地址
	private String idCardPicPath;
	//用户的userId如果之前注册过则由该字段
	private int userId;
	//状态0初始；1审核未通过；2审核通过；11手机号异常
	private int status;
	//备注
	private String remark;
	
	public void init(){
		if(this.gradeName == null){
			this.gradeName = personInCharge + "的微店";
		}
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getWechat() {
		return wechat;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getGradeType() {
		return gradeType;
	}
	public void setGradeType(Integer gradeType) {
		this.gradeType = gradeType;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getPersonInCharge() {
		return personInCharge;
	}
	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIdCardPicPath() {
		return idCardPicPath;
	}
	public void setIdCardPicPath(String idCardPicPath) {
		this.idCardPicPath = idCardPicPath;
	}
}
