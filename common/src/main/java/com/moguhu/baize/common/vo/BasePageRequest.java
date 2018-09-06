package com.moguhu.baize.common.vo;

/**
 * 分页列表BASE请求
 * 
 * @author xuefeihu
 *
 */
public class BasePageRequest {

	/** 页码 */
	private Integer page;

	/** 每页记录数 */
	private Integer rows;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

}
