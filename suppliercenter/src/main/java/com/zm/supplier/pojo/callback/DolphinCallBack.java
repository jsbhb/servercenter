package com.zm.supplier.pojo.callback;

public class DolphinCallBack {

	private String status;
	private String data;

	public DolphinCallBack(String status, String data) {
		this.status = status;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
