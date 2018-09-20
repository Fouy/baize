package com.moguhu.baize.metadata.response.api;

import com.moguhu.baize.metadata.entity.api.ApiEntity;

/**
 * API 响应
 * <p>
 * Created by xuefeihu on 18/9/6.
 */
public class ApiResponse extends ApiEntity {

    /**
     * API 分组名称
     */
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
