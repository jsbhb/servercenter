package com.zm.goods.pojo.base;

import java.util.List;

/**
 * @author wqy
 * @fun springMVC不能直接接收list<Object>,将sortModel封装成SortModelList
 * @date 2017年6月6日
 */
public class SortModelList {

	private List<SortModel> sortList;

	public List<SortModel> getSortList() {
		return sortList;
	}

	public void setSortList(List<SortModel> sortList) {
		this.sortList = sortList;
	}
	
}
