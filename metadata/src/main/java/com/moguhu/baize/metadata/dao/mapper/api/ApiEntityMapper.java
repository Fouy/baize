package com.moguhu.baize.metadata.dao.mapper.api;


import com.moguhu.baize.metadata.request.api.ApiSearchRequest;
import com.moguhu.baize.metadata.entity.api.ApiEntity;

import java.util.List;

public interface ApiEntityMapper {
    /**
     * 通过id物理删除api的数据.
     */
    int deleteById(Long apiId);

    /**
     * 向表api中插入数据.
     */
    int insert(ApiEntity record);

    /**
     * 通过id查询表api.
     */
    ApiEntity selectById(Long apiId);

    /**
     * 通过id修改表api.
     */
    int updateById(ApiEntity record);

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    List<ApiEntity> queryAll(ApiSearchRequest request);

    /**
     * 加锁
     *
     * @param apiId
     */
    void lock(Long apiId);
}