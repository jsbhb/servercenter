package com.zm.finance.util;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.finance.pojo.Pagination;

public class CommonUtil {

	public static <T> Page<? extends Pagination> pagination(List<? extends Pagination> list, Pagination pagination) {
		int currentPage = pagination.getCurrentPage();
		int pageSize = pagination.getNumPerPage();
		int startRow = currentPage > 0 ? (currentPage - 1) * pageSize : 0;
		int endRow = currentPage * pageSize;
		Page<Pagination> result = new Page<Pagination>(currentPage, pageSize, list.size());
		result.setPages(list.size()/pageSize + 1);
		if (startRow > list.size()) {
			return result;
		}
		for (int i = startRow; i < endRow; i++) {
			if(i >= list.size()){
				break;
			}
			result.add(list.get(i));
		}
		return result;
	}
}
