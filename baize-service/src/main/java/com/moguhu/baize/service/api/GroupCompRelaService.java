package com.moguhu.baize.service.api;


import com.moguhu.baize.metadata.entity.api.GroupCompRelaEntity;

import java.util.List;

/**
 * API Group-组件关系
 * <p>
 * Created by xuefeihu on 18/9/16.
 */
public interface GroupCompRelaService {

    /**
     * 列表查询
     *
     * @param request
     * @return
     */
    List<GroupCompRelaEntity> all(GroupCompRelaEntity request);
}
