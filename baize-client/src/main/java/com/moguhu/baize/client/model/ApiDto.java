package com.moguhu.baize.client.model;

import com.moguhu.baize.client.constants.BooleanEnum;
import com.moguhu.baize.client.constants.ProtocolEnum;

import java.util.List;

/**
 * API Info.
 * <p>
 * Created by xuefeihu on 18/9/19.
 */
public class ApiDto extends ApiZkStorage {

    private List<ComponentDto> componentList;

    /**
     * 主键ID.
     */
    private Long apiId;

    /**
     * 分组ID.
     */
    private Long groupId;

    /**
     * 路径.
     */
    private String path;

    /**
     * 请求方式：POST GET PUT OPTIONS 等.
     */
    private String methods;

    /**
     * 版本.
     */
    private String version;

    /**
     * 是否缓存：是 YES  否 NO.
     *
     * @see BooleanEnum
     */
    private String cached;

    /**
     * 协议：HTTP  HTTPS.
     *
     * @see ProtocolEnum
     */
    private String protocol;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getCached() {
        return cached;
    }

    public void setCached(String cached) {
        this.cached = cached;
    }

    public List<ComponentDto> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<ComponentDto> componentList) {
        this.componentList = componentList;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
