package com.zm.goods.pojo.vo;

import java.util.List;

public class TimeLimitActive {

	private Integer id;
	
	private String startTime;
	
	private String endTime;
	
	private Integer status;
	
	private List<TimeLimitActiveData> dataList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<TimeLimitActiveData> getDataList() {
		return dataList;
	}

	public void setDataList(List<TimeLimitActiveData> dataList) {
		this.dataList = dataList;
	}
	
	
}
