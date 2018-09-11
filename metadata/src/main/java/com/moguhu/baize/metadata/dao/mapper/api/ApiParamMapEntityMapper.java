package com.moguhu.baize.metadata.dao.mapper.api;

import com.moguhu.baize.metadata.entity.api.ApiParamMapEntity;
import com.moguhu.baize.metadata.request.api.ApiParamMapSearchRequest;

import java.util.List;

public interface ApiParamMapEntityMapper {
    /**
     * 通过id物理删除api_param_map的数据.
     */
    int deleteById(Long mapId);

    /**
     * 向表api_param_map中插入数据.
     */
    int insert(ApiParamMapEntity record);

    /**
     * 通过id查询表api_param_map.
     */
    ApiParamMapEntity selectById(Long mapId);

    /**
     * 通过id修改表api_param_map.
     */
    int updateById(ApiParamMapEntity record);

    /**
     * 查询列表
     *
     * @param request
     * @return
     */
    List<ApiParamMapEntity> queryAll(ApiParamMapSearchRequest request);

    /**
     * 加锁
     *
     * @param mapId
     */
    void lock(Long mapId);
}