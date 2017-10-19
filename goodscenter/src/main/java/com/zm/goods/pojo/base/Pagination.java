package com.zm.goods.pojo.base;

import com.github.pagehelper.Page;

/**
 * 
 * @author wqy
 * @fun 分页组件
 */
public class Pagination {

	private Integer numPerPage = 10; // 每页几条
	private Integer currentPage = 1; // 当前页
	private Integer startRow;
	private Integer endRow;
	private Integer totalPages;
	private Long totalRows;

	public void init() {
		this.startRow = this.currentPage > 0 ? (this.currentPage - 1) * this.numPerPage : 0;
		this.endRow = this.currentPage * this.numPerPage;
	}

	public Pagination webPageConverter(Page<?> page) {
		this.setTotalPages(page.getPages());
		this.setTotalRows(page.getTotal());

		return this;
	}

	public Pagination webListConverter() {
		if(this.totalRows == null){
			this.totalRows = 0L;
		}
		if(this.totalRows % this.numPerPage == 0){
			this.totalPages = (int) (this.totalRows / this.numPerPage);
		} else {
			this.totalPages = (int) (this.totalRows / this.numPerPage) + 1;
		}
		return this;
	}

	public Integer getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Long totalRows) {
		this.totalRows = totalRows;
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

}
