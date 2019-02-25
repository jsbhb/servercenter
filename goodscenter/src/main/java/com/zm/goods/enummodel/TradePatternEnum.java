package com.zm.goods.enummodel;

public enum TradePatternEnum {

	CROSS_BORDER(0), GENERAL_TRADE(2);

	private int type;

	TradePatternEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static TradePatternEnum valueof(int type) {
		switch (type) {
		case 0:
			return CROSS_BORDER;
		case 2:
			return GENERAL_TRADE;
		default:
			return null;
		}

	}
}
