package com.moguhu.baize.metadata.mapper.api;


import com.moguhu.baize.metadata.entity.api.ApiParamEntity;
import com.moguhu.baize.metadata.request.api.ApiParamSearchRequest;

import java.util.List;

public interface ApiParamEntityMapper {

    /**
     * 通过id物理删除api_param的数据.
     */
    int deleteById(Long paramId);

    /**
     * 向表api_param中插入数据.
     */
    int insert(ApiParamEntity record);

    /**
     * 通过id查询表api_param.
     */
    ApiParamEntity selectById(Long paramId);

    /**
     * 通过id修改表api_param.
     */
    int updateById(ApiParamEntity record);

    /**
     * 查询列表
     *
     * @param request
     * @return
     */
    List<ApiParamEntity> queryAll(ApiParamSearchRequest request);

    /**
     * 加锁
     *
     * @param paramId
     */
    void lock(Long paramId);
}