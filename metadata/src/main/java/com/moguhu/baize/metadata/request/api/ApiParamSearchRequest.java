package com.moguhu.baize.metadata.request.api;

import com.moguhu.baize.metadata.request.BasePageRequest;

/**
 * API Param 分页查询请求
 * <p>
 * Created by xuefeihu on 18/9/10.
 */
public class ApiParamSearchRequest extends BasePageRequest {

    /**
     * API ID.
     */
    private Long apiId;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }
}
