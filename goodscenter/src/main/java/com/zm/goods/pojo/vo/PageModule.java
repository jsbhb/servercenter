package com.zm.goods.pojo.vo;

import java.util.ArrayList;
import java.util.List;

import com.zm.goods.pojo.Activity;
import com.zm.goods.pojo.ActivityData;
import com.zm.goods.pojo.DictData;
import com.zm.goods.pojo.PopularizeDict;

public class PageModule {

	private Integer layoutId;

	private String code;

	private String name;

	private String enname;

	private Integer firstCategory;
	
	private Integer type;//0限时抢购，1：全场。。
	
	private Integer activiey;//0:普通模块；1：活动模块
	
	private String href;

	private String picPath1;

	private String picPath2;

	private String picPath3;

	private String description;
	
	private String startTime;
	
	private String endTime;
	
	private Integer status;

	private List<ModuleData> moduleDataList;

	public PageModule(PopularizeDict dict, List<DictData> data) {
		init(dict, data);
	}

	public PageModule(Activity activity, List<ActivityData> data) {
		init(activity, data);
	}

	private void init(Activity activity, List<ActivityData> dataList) {
		this.code = activity.getCode();
		this.name = activity.getName();
		this.description = activity.getDescription();
		this.layoutId = activity.getLayoutId();
		this.activiey = 1;
		this.type = activity.getType();
		this.startTime = activity.getStartTime();
		this.endTime = activity.getEndTime();
		this.status = activity.getStatus();
		this.picPath1 = activity.getPicPath();
		List<ModuleData> moduleDataList = new ArrayList<ModuleData>();
		if (dataList != null) {
			for (ActivityData data : dataList) {
				ModuleData moduleData = new ModuleData(data);
				moduleDataList.add(moduleData);
			}
		}
		this.moduleDataList = moduleDataList;
	}

	private void init(PopularizeDict dict, List<DictData> dataList) {
		this.code = dict.getCode();
		this.name = dict.getName();
		this.description = dict.getDescription();
		this.layoutId = dict.getLayoutId();
		this.enname = dict.getEnname();
		this.firstCategory = dict.getFirstCategory();
		this.picPath1 = dict.getPicPath1();
		this.picPath2 = dict.getPicPath2();
		this.picPath3 = dict.getPicPath3();
		this.activiey = 0;
		this.type = dict.getType();
		List<ModuleData> moduleDataList = new ArrayList<ModuleData>();
		if (dataList != null) {
			for (DictData data : dataList) {
				ModuleData moduleData = new ModuleData(data);
				moduleDataList.add(moduleData);
			}
		}
		this.moduleDataList = moduleDataList;

	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getActiviey() {
		return activiey;
	}

	public void setActiviey(Integer activiey) {
		this.activiey = activiey;
	}

	public Integer getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Integer layoutId) {
		this.layoutId = layoutId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ModuleData> getModuleDataList() {
		return moduleDataList;
	}

	public void setModuleDataList(List<ModuleData> moduleDataList) {
		this.moduleDataList = moduleDataList;
	}

}
