package com.moguhu.baize.metadata.entity.backend;

import com.alibaba.fastjson.annotation.JSONField;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.constants.backend.ComponentTypeEnum;
import com.moguhu.baize.common.constants.backend.ExecPositionEnum;

import java.io.Serializable;
import java.util.Date;

public class ComponentEntity implements Serializable {
    /**
     * 主键ID.
     */
    private Long compId;

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
     * 组件名称.
     */
    private String name;

    /**
     * 组件编码.
     */
    private String compCode;

    /**
     * 类型：认证 AUTH; 鉴权(角色权限) ALLOW; 流量控制 TRAFFIC; 缓存 CACHE; 路由 ROUTE; 日志 LOG; 协议转换 PROTO_CONVERT; 其他 OTHER.
     *
     * @see ComponentTypeEnum
     */
    private String type;

    /**
     * groovy 脚本ID.
     */
    private Long contentId;

    /**
     * 状态： ON 启用  OFF 停用.
     *
     * @see StatusEnum
     */
    private String status;

    /**
     * 版本号.
     */
    private String version;

    /**
     * 执行位置：前置 PRE  路由 ROUTE  后置 POST  错误 ERROR.
     *
     * @see ExecPositionEnum
     */
    private String execPosition;

    /**
     * 优先级.
     */
    private Integer priority;

    /**
     * 描述说明.
     */
    private String info;

    /**
     * 扩展字段.
     */
    private String extInfo;

    private static final long serialVersionUID = 1L;

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExecPosition() {
        return execPosition;
    }

    public void setExecPosition(String execPosition) {
        this.execPosition = execPosition;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentEntity)) return false;

        ComponentEntity that = (ComponentEntity) o;

        if (!compId.equals(that.compId)) return false;
        if (!compCode.equals(that.compCode)) return false;
        return type.equals(that.type);

    }

    @Override
    public int hashCode() {
        int result = compId.hashCode();
        result = 31 * result + compCode.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}