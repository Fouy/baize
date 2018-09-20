package com.moguhu.baize.metadata.response.api;

import com.moguhu.baize.metadata.entity.api.ApiParamMapEntity;

/**
 * API Param 映射 响应
 * <p>
 * Created by xuefeihu on 18/9/11.
 */
public class ApiParamMapResponse extends ApiParamMapEntity {

    /**
     * BASE参数名.
     */
    private String paramName;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
