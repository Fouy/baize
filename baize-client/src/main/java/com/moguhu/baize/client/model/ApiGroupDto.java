package com.moguhu.baize.client.model;

import java.util.List;

/**
 * API Group
 * <p>
 * Created by xuefeihu on 18/9/19.
 */
public class ApiGroupDto {

    private List<ComponentDto> componentList;

    private List<ApiDto> apiList;

    private List<String> compIds;

    /**
     * 主键ID.
     */
    private Long groupId;

    /**
     * 分组名称.
     */
    private String name;

    /**
     * 分组类型：COMMON 普通  .
     */
    private String type;

    /**
     * 服务编码.
     */
    private String gateServiceCode;

    public List<ApiDto> getApiList() {
        return apiList;
    }

    public void setApiList(List<ApiDto> apiList) {
        this.apiList = apiList;
    }

    public List<String> getCompIds() {
        return compIds;
    }

    public void setCompIds(List<String> compIds) {
        this.compIds = compIds;
    }

    public List<ComponentDto> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<ComponentDto> componentList) {
        this.componentList = componentList;
    }

    public String getGateServiceCode() {
        return gateServiceCode;
    }

    public void setGateServiceCode(String gateServiceCode) {
        this.gateServiceCode = gateServiceCode;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
