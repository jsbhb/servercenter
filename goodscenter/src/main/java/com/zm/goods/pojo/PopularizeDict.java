package com.zm.goods.pojo;

public class PopularizeDict {

	private Integer id;
	
	private Integer layoutId;
	
	private String code;
	
	private String name;
	
	private String enname;
	
	private Integer firstCategory;
	
	private Integer type;
	
	private String picPath1;
	
	private String picPath2;
	
	private String picPath3;
	
	private String description;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;
	
	private String href;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Integer layoutId) {
		this.layoutId = layoutId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public Integer getFirstCategory() {
		return firstCategory;
	}

	public void setFirstCategory(Integer firstCategory) {
		this.firstCategory = firstCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getPicPath1() {
		return picPath1;
	}

	public void setPicPath1(String picPath1) {
		this.picPath1 = picPath1;
	}

	public String getPicPath2() {
		return picPath2;
	}

	public void setPicPath2(String picPath2) {
		this.picPath2 = picPath2;
	}

	public String getPicPath3() {
		return picPath3;
	}

	public void setPicPath3(String picPath3) {
		this.picPath3 = picPath3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((enname == null) ? 0 : enname.hashCode());
		result = prime * result + ((firstCategory == null) ? 0 : firstCategory.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((layoutId == null) ? 0 : layoutId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((opt == null) ? 0 : opt.hashCode());
		result = prime * result + ((picPath1 == null) ? 0 : picPath1.hashCode());
		result = prime * result + ((picPath2 == null) ? 0 : picPath2.hashCode());
		result = prime * result + ((picPath3 == null) ? 0 : picPath3.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PopularizeDict other = (PopularizeDict) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (enname == null) {
			if (other.enname != null)
				return false;
		} else if (!enname.equals(other.enname))
			return false;
		if (firstCategory == null) {
			if (other.firstCategory != null)
				return false;
		} else if (!firstCategory.equals(other.firstCategory))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (layoutId == null) {
			if (other.layoutId != null)
				return false;
		} else if (!layoutId.equals(other.layoutId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (opt == null) {
			if (other.opt != null)
				return false;
		} else if (!opt.equals(other.opt))
			return false;
		if (picPath1 == null) {
			if (other.picPath1 != null)
				return false;
		} else if (!picPath1.equals(other.picPath1))
			return false;
		if (picPath2 == null) {
			if (other.picPath2 != null)
				return false;
		} else if (!picPath2.equals(other.picPath2))
			return false;
		if (picPath3 == null) {
			if (other.picPath3 != null)
				return false;
		} else if (!picPath3.equals(other.picPath3))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PopularizeDict [id=" + id + ", layoutId=" + layoutId + ", code=" + code + ", name=" + name + ", enname="
				+ enname + ", firstCategory=" + firstCategory + ", picPath1=" + picPath1 + ", picPath2=" + picPath2
				+ ", picPath3=" + picPath3 + ", description=" + description + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", opt=" + opt + "]";
	}

	
}
