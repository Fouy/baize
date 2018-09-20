package com.moguhu.baize.metadata.response.api;

import com.moguhu.baize.metadata.response.backend.ComponentResponse;

import java.util.List;
import java.util.Map;

/**
 * API分组-组件列表 响应
 * <p>
 * Created by xuefeihu on 18/9/14.
 */
public class ApiGroupCompResponse {

    /**
     * 组件ID.
     */
    private Long groupId;

    /**
     * 分组名称.
     */
    private String name;

    /**
     * 组件列表
     */
    Map<String, List<ComponentResponse>> componentMap;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
