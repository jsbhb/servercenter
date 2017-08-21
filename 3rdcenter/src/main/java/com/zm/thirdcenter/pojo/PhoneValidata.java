package com.zm.thirdcenter.pojo;

public class PhoneValidata {

	private Integer sendTime;
	
	private String code;
	
	private Long time;

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getSendTime() {
		return sendTime;
	}

	public void setSendTime(Integer sendTime) {
		this.sendTime = sendTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "PhoneValidata [sendTime=" + sendTime + ", code=" + code + ", time=" + time + "]";
	}
	
}
