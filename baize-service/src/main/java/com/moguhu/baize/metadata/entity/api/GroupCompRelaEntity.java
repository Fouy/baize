package com.moguhu.baize.metadata.entity.api;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

public class GroupCompRelaEntity implements Serializable {
    /**
     * .
     */
    private Long groupCompRelaId;

    /**
     * 创建时间.
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 分组ID.
     */
    private Long groupId;

    /**
     * 组件ID.
     */
    private Long compId;

    private static final long serialVersionUID = 1L;

    public Long getGroupCompRelaId() {
        return groupCompRelaId;
    }

    public void setGroupCompRelaId(Long groupCompRelaId) {
        this.groupCompRelaId = groupCompRelaId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }
}