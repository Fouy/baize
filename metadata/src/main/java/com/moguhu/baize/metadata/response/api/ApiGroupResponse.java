package com.moguhu.baize.metadata.response.api;

import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;

/**
 * API分组 响应
 * <p>
 * Created by xuefeihu on 18/9/6.
 */
public class ApiGroupResponse extends ApiGroupEntity {

    private String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
