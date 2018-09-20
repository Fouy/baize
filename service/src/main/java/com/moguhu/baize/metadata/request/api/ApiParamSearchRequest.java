package com.moguhu.baize.metadata.request.api;

import com.moguhu.baize.common.constants.api.ApiParamStatuslEnum;
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

    /**
     * Param status.
     *
     * @see ApiParamStatuslEnum
     */
    private String status;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
