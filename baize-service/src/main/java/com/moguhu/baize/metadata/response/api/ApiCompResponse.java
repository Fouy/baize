package com.moguhu.baize.metadata.response.api;

import com.moguhu.baize.metadata.response.backend.ComponentResponse;

import java.util.List;
import java.util.Map;

/**
 * API-组件列表 响应
 * <p>
 * Created by xuefeihu on 18/9/14.
 */
public class ApiCompResponse {

    /**
     * API ID.
     */
    private Long apiId;

    /**
     * API 名称.
     */
    private String name;

    /**
     * 组件列表
     */
    Map<String, List<ComponentResponse>> componentMap;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<ComponentResponse>> getComponentMap() {
        return componentMap;
    }

    public void setComponentMap(Map<String, List<ComponentResponse>> componentMap) {
        this.componentMap = componentMap;
    }
}
