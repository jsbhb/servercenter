package com.zm.supplier.supplierinf.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiangYouOrderStatusSubTemp implements Comparable<LiangYouOrderStatusSubTemp>{

	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("CreateTime")
	private String createTime;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public int compareTo(LiangYouOrderStatusSubTemp o) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = sdf.parse(createTime);
			d2 = sdf.parse(o.getCreateTime());
			if(d1.getTime() < d2.getTime()){
				return 1;
			} else if(d1.getTime() > d2.getTime()){
				return -1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public String toString() {
		return "LiangYouOrderStatusSubTemp [status=" + status + ", createTime=" + createTime + "]";
	}
	
	
}
