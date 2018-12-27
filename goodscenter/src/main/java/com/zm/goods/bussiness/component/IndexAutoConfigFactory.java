package com.zm.goods.bussiness.component;

import com.zm.goods.bussiness.component.base.IindexAutoConfig;

public class IndexAutoConfigFactory {

	private final static String ACTIVITY_2 = "activity-2";

	public static final IindexAutoConfig get(String key) {
		switch (key) {
		case ACTIVITY_2:
			return new BigSalesPerWeekModule();
		default:
			return null;
		}

	}
}
