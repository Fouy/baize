package com.moguhu.baize.metadata.request.api;

import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.metadata.request.BasePageRequest;

/**
 * API 分页查询请求
 * <p>
 * Created by xuefeihu on 18/9/6.
 */
public class ApiGroupSearchRequest extends BasePageRequest {

    /**
     * API分组名称.
     */
    private String name;

    /**
     * 状态：启用 ON 停用 OFF.
     *
     * @see StatusEnum
     */
    private String status;

    /**
     * 组件ID.
     */
    private String compId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }
}
