package com.zm.order.common;

import com.github.pagehelper.Page;

/**
 * 
 * ClassName: Pagination <br/>
 * Function: 分页组件. <br/>
 * date: Oct 29, 2017 8:00:08 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class Pagination {
	private int numPerPage;
	private long totalRows;
	private int totalPages;
	private int currentPage;
	private int startIndex;
	private int lastIndex;
	
	public Pagination(){}

	public Pagination(Page<?> page) {
		this.currentPage = page.getPageNum();
		this.lastIndex = page.getEndRow();
		this.numPerPage = page.getPageSize();
		this.totalPages = page.getPages();
		this.totalRows = page.getTotal();
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

}
