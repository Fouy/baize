package com.moguhu.baize.metadata.request;

import java.io.Serializable;

/**
 * 分页请求基类
 * <p>
 * Created by xuefeihu on 18/5/14.
 */
public class BasePageRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer pageNumber;

    /**
     * 分页大小
     */
    private Integer pageSize;

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
