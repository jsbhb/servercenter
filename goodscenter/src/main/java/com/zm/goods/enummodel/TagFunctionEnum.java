package com.zm.goods.enummodel;

public enum TagFunctionEnum {
	CUT_ORDER(1,"卡单");
	
	private int id;
	private String name;
	
	private TagFunctionEnum(int id, String name){
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
