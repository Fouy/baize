package com.moguhu.baize.metadata.dao.mapper.api;


import com.moguhu.baize.common.request.api.ApiGroupSearchRequest;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;

import java.util.List;

public interface ApiGroupEntityMapper {
    /**
     * 通过id物理删除api_group的数据.
     */
    int deleteById(Long groupId);

    /**
     * 向表api_group中插入数据.
     */
    int insert(ApiGroupEntity record);

    /**
     * 通过id查询表api_group.
     */
    ApiGroupEntity selectById(Long groupId);

    /**
     * 通过id修改表api_group.
     */
    int updateById(ApiGroupEntity record);

    /**
     * 查询列表
     *
     * @param request
     * @return
     */
    List<ApiGroupEntity> queryAll(ApiGroupSearchRequest request);

    /**
     * 加锁
     *
     * @param groupId
     */
    void lock(Long groupId);
}