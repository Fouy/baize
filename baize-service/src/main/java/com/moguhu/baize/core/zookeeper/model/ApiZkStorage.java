package com.moguhu.baize.core.zookeeper.model;

import com.moguhu.baize.metadata.response.api.ApiParamMapResponse;
import com.moguhu.baize.metadata.response.api.ApiParamResponse;

import java.util.List;

/**
 * API ZooKeeper Storage Model
 * <p>
 * Created by xuefeihu on 18/9/15.
 */
public class ApiZkStorage {

    /**
     * component ids.
     */
    private List<Long> compIds;

    /**
     * parameters.
     */
    private List<ApiParamResponse> params;

    /**
     * parameter mappings.
     */
    private List<ApiParamMapResponse> mappings;

    public List<Long> getCompIds() {
        return compIds;
    }

    public void setCompIds(List<Long> compIds) {
        this.compIds = compIds;
    }

    public List<ApiParamMapResponse> getMappings() {
        return mappings;
    }

    public void setMappings(List<ApiParamMapResponse> mappings) {
        this.mappings = mappings;
    }

    public List<ApiParamResponse> getParams() {
        return params;
    }

    public void setParams(List<ApiParamResponse> params) {
        this.params = params;
    }
}
