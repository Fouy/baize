package com.moguhu.baize.service.api;


import com.moguhu.baize.metadata.entity.api.ApiCompRelaEntity;

import java.util.List;

/**
 * API-组件关系
 * <p>
 * Created by xuefeihu on 18/9/16.
 */
public interface ApiCompRelaService {

    /**
     * 列表查询
     *
     * @param request
     * @return
     */
    List<ApiCompRelaEntity> all(ApiCompRelaEntity request);
}
