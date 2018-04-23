package com.zm.timetask.pojo;

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
	private Integer startRow;
	private Integer endRow;
	
	public Pagination(){}
	
	public void init() {
		this.startRow = this.currentPage > 0 ? (this.currentPage - 1) * this.numPerPage : 0;
		this.endRow = this.currentPage * this.numPerPage;
	}

	public Pagination(Page<?> page) {
		this.currentPage = page.getPageNum();
		this.lastIndex = page.getEndRow();
		this.numPerPage = page.getPageSize();
		this.totalPages = page.getPages();
		this.totalRows = page.getTotal();
	}
	
	public Pagination webListConverter() {
		if(this.totalRows % this.numPerPage == 0){
			this.totalPages = (int) (this.totalRows / this.numPerPage);
		} else {
			this.totalPages = (int) (this.totalRows / this.numPerPage) + 1;
		}
		return this;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public void setEndRow(Integer endRow) {
		this.endRow = endRow;
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
