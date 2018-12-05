package com.zm.goods.enummodel;

public enum SystemEnum {

	PCMALL("pcMall"), MPMALL("mpMall"), FMPMALL("fmpMall,mpMall"), APPLET("applet");

	private String name;

	private SystemEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static SystemEnum getSystem(String system) {
		if (system.equals(MPMALL.name)) {
			return MPMALL;
		}
		if (system.equals(PCMALL.name)) {
			return PCMALL;
		}
		if(system.equals(APPLET.name)){
			return APPLET;
		}
		return null;
	}

}
