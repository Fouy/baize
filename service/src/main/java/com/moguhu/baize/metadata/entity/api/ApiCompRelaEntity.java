package com.moguhu.baize.metadata.entity.api;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

public class ApiCompRelaEntity implements Serializable {
    /**
     * .
     */
    private Long apiCompRelaId;

    /**
     * 创建时间.
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * API ID.
     */
    private Long apiId;

    /**
     * 组件ID.
     */
    private Long compId;

    private static final long serialVersionUID = 1L;

    public Long getApiCompRelaId() {
        return apiCompRelaId;
    }

    public void setApiCompRelaId(Long apiCompRelaId) {
        this.apiCompRelaId = apiCompRelaId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }
}