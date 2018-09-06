package com.moguhu.baize.common.request.api;

import com.moguhu.baize.common.request.BasePageRequest;

/**
 * API 分页查询请求
 * <p>
 * Created by xuefeihu on 18/5/14.
 */
public class ApiGroupSearchRequest extends BasePageRequest {

    /**
     * API分组名称.
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
