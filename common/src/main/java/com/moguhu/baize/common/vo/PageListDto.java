package com.moguhu.baize.common.vo;

import java.util.List;

/**
 * 分页列表 DTO
 *
 * @author xuefeihu
 */
public class PageListDto<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 行记录
     */
    private List<T> rows;

    /**
     * 当前页码
     */
    private Integer pageNumber;

    /**
     * 分页大小
     */
    private Integer pageSize;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
