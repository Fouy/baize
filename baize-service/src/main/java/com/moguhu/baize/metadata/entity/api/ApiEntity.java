package com.moguhu.baize.metadata.entity.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.moguhu.baize.client.constants.BooleanEnum;
import com.moguhu.baize.common.constants.EnvEnum;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.constants.api.ProtocolEnum;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Date;

public class ApiEntity implements Serializable {

    /**
     * 主键ID.
     */
    private Long apiId;

    /**
     * 创建时间.
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间.
     */
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * API名称.
     */
    private String name;

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
     *
     * @see HttpMethod
     */
    private String methods;

    /**
     * 状态：启用 ON 停用 OFF.
     *
     * @see StatusEnum
     */
    private String status;

    /**
     * 版本.
     */
    private String version;

    /**
     * 当前环境：开发 DEV  测试 TEST  预发布 UAT  生产 ONLINE.
     *
     * @see EnvEnum
     */
    private String env;

    /**
     * 是否缓存：是 YES  否 NO.
     *
     * @see BooleanEnum
     */
    private String cached;

    /**
     * 描述说明.
     */
    private String info;

    /**
     * 协议：HTTP  HTTPS.
     *
     * @see ProtocolEnum
     */
    private String protocol;

    /**
     * 扩展信息.
     */
    private String extInfo;

    private static final long serialVersionUID = 1L;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
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

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getCached() {
        return cached;
    }

    public void setCached(String cached) {
        this.cached = cached;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }
}