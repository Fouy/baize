package com.moguhu.baize.metadata.entity.backend;

import com.moguhu.baize.common.constants.backend.NodeStatusEnum;

import java.io.Serializable;
import java.util.Date;

public class GateNodeEntity implements Serializable {
    /**
     * 主键ID.
     */
    private Long nodeId;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 创建时间.
     */
    private Date modifyTime;

    /**
     * 机房名称.
     */
    private String engineRoom;

    /**
     * 节点编码.
     */
    private String nodeNo;

    /**
     * SSH地址.
     */
    private String sshAddr;

    /**
     * SSH账号.
     */
    private String sshAccount;

    /**
     * SSH密码.
     */
    private String sshPwd;

    /**
     * 探测IP.
     */
    private String detectIp;

    /**
     * 探测端口.
     */
    private String derectPort;

    /**
     * 状态：已创建 CREATED 收集中 COLLECTING 运行中 RUNNING  已关闭 CLOSED.
     *
     * @see NodeStatusEnum
     */
    private String status;

    /**
     * 重启时间.
     */
    private Date rebootTime;

    private static final long serialVersionUID = 1L;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
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

    public String getEngineRoom() {
        return engineRoom;
    }

    public void setEngineRoom(String engineRoom) {
        this.engineRoom = engineRoom;
    }

    public String getNodeNo() {
        return nodeNo;
    }

    public void setNodeNo(String nodeNo) {
        this.nodeNo = nodeNo;
    }

    public String getSshAddr() {
        return sshAddr;
    }

    public void setSshAddr(String sshAddr) {
        this.sshAddr = sshAddr;
    }

    public String getSshAccount() {
        return sshAccount;
    }

    public void setSshAccount(String sshAccount) {
        this.sshAccount = sshAccount;
    }

    public String getSshPwd() {
        return sshPwd;
    }

    public void setSshPwd(String sshPwd) {
        this.sshPwd = sshPwd;
    }

    public String getDetectIp() {
        return detectIp;
    }

    public void setDetectIp(String detectIp) {
        this.detectIp = detectIp;
    }

    public String getDerectPort() {
        return derectPort;
    }

    public void setDerectPort(String derectPort) {
        this.derectPort = derectPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRebootTime() {
        return rebootTime;
    }

    public void setRebootTime(Date rebootTime) {
        this.rebootTime = rebootTime;
    }
}