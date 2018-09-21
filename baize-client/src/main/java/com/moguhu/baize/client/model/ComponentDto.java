package com.moguhu.baize.client.model;

import java.io.Serializable;

/**
 * 组件 响应
 * <p>
 * Created by xuefeihu on 18/9/20.
 */
public class ComponentDto implements Serializable {

    /**
     * 组件 ID.
     */
    private Long compId;

    /**
     * 组件编码.
     */
    private String compCode;

    /**
     * 版本号.
     */
    private String version;

    /**
     * 执行位置：前置 PRE  路由 ROUTE  后置 POST  错误 ERROR.
     */
    private String execPosition;

    /**
     * 优先级.
     */
    private Integer priority;

    /**
     * 脚本代码
     */
    private String compContent;

    /**
     * GateWay 存储时文件名
     */
    private String fileName;

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompContent() {
        return compContent;
    }

    public void setCompContent(String compContent) {
        this.compContent = compContent;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentDto)) return false;

        ComponentDto that = (ComponentDto) o;

        return compId.equals(that.compId);

    }

    @Override
    public int hashCode() {
        return compId.hashCode();
    }
}
