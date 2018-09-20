package com.moguhu.baize.metadata.request.api;

import com.moguhu.baize.common.constants.api.ParamMapTypeEnum;
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

    /**
     * 映射类型： 映射 MAP  自定义 CUSTOM
     *
     * @see ParamMapTypeEnum
     */
    private String mapType;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
}
