package com.moguhu.baize.metadata.entity.backend;

import com.alibaba.fastjson.annotation.JSONField;
import com.moguhu.baize.common.constants.StatusEnum;

import java.io.Serializable;
import java.util.Date;

public class GateServiceEntity implements Serializable {
    /**
     * 主键ID.
     */
    private Long serviceId;

    /**
     * 创建时间.
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间.
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 服务名称.
     */
    private String name;

    /**
     * 服务编码.
     */
    private String serviceCode;

    /**
     * 后端服务HOSTS , 分隔.
     */
    private String hosts;

    /**
     * 状态：启用 ON 停用 OFF.
     *
     * @see StatusEnum
     */
    private String status;

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

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }
}