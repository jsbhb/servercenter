package com.zm.order.pojo.bo;

public class TempSpecs {

	private String svV;

	private String skV;

	public void setSvV(String svV) {
		this.svV = svV;
	}

	public void setSkV(String skV) {
		this.skV = skV;
	}

	public String getSvV() {
		return svV;
	}

	public String getSkV() {
		return skV;
	}

	@Override
	public String toString() {
		return "TempSpecs [svV=" + svV + ", skV=" + skV + "]";
	}
}
