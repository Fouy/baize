package com.moguhu.baize.metadata.request.api;

import com.moguhu.baize.metadata.request.BasePageRequest;

/**
 * API Param 映射分页查询请求
 * <p>
 * Created by xuefeihu on 18/9/11.
 */
public class ApiParamMapSearchRequest extends BasePageRequest {

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
