package com.moguhu.baize.metadata.entity.backend;

import java.io.Serializable;
import java.util.Date;

public class ServiceEntity implements Serializable {
    /**
     * 主键ID.
     */
    private Long serviceId;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 更新时间.
     */
    private Date modifyTime;

    /**
     * 服务名称.
     */
    private String name;

    /**
     * HOST.
     */
    private String host;

    /**
     * 端口.
     */
    private String port;

    /**
     * 扩展信息.
     */
    private String extInfo;

    private static final long serialVersionUID = 1L;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }
}