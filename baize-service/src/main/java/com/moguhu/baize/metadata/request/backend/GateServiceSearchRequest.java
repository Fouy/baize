package com.moguhu.baize.metadata.request.backend;

import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.metadata.request.BasePageRequest;

/**
 * 分页查询请求
 * <p>
 * Created by xuefeihu on 18/9/8.
 */
public class GateServiceSearchRequest extends BasePageRequest {

    /**
     * 名称.
     */
    private String name;

    /**
     * 状态：启用 ON 停用 OFF.
     *
     * @see StatusEnum
     */
    private String status;

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
}
