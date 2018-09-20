package com.moguhu.baize.metadata.request.api;

import com.moguhu.baize.common.constants.EnvEnum;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.metadata.request.BasePageRequest;

/**
 * API 分页查询请求
 * <p>
 * Created by xuefeihu on 18/9/6.
 */
public class ApiSearchRequest extends BasePageRequest {

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
     * 状态：启用 ON 停用 OFF.
     *
     * @see StatusEnum
     */
    private String status;

    /**
     * 当前环境：开发 DEV  测试 TEST  预发布 UAT  生产 ONLINE.
     *
     * @see EnvEnum
     */
    private String env;

    /**
     * 组件ID.
     */
    private Long compId;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }
}
