package com.moguhu.baize.metadata.entity.backend;

import java.io.Serializable;
import java.util.Date;

public class NodeGroupRelaEntity implements Serializable {
    /**
     * 主键ID.
     */
    private Long nodeRelaId;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 更新时间.
     */
    private Date modifyTime;

    /**
     * 节点ID.
     */
    private Long nodeId;

    /**
     * API分组ID.
     */
    private Long groupId;

    private static final long serialVersionUID = 1L;

    public Long getNodeRelaId() {
        return nodeRelaId;
    }

    public void setNodeRelaId(Long nodeRelaId) {
        this.nodeRelaId = nodeRelaId;
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

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}