package com.moguhu.baize.client.model;

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
    private List<ApiParamDto> params;

    /**
     * parameter mappings.
     */
    private List<ApiParamMapDto> mappings;

    public List<Long> getCompIds() {
        return compIds;
    }

    public void setCompIds(List<Long> compIds) {
        this.compIds = compIds;
    }

    public List<ApiParamMapDto> getMappings() {
        return mappings;
    }

    public void setMappings(List<ApiParamMapDto> mappings) {
        this.mappings = mappings;
    }

    public List<ApiParamDto> getParams() {
        return params;
    }

    public void setParams(List<ApiParamDto> params) {
        this.params = params;
    }
}
