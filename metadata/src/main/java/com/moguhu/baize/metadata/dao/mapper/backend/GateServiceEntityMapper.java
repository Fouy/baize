package com.moguhu.baize.metadata.dao.mapper.backend;

import com.moguhu.baize.metadata.entity.backend.GateServiceEntity;
import com.moguhu.baize.metadata.request.backend.GateServiceSearchRequest;

import java.util.List;

public interface GateServiceEntityMapper {
    /**
     * 通过id物理删除gate_service的数据.
     */
    int deleteById(Long serviceId);

    /**
     * 向表gate_service中插入数据.
     */
    int insert(GateServiceEntity record);

    /**
     * 通过id查询表gate_service.
     */
    GateServiceEntity selectById(Long serviceId);

    /**
     * 通过id修改表gate_service.
     */
    int updateById(GateServiceEntity record);

    /**
     * 查询列表
     *
     * @param request
     * @return
     */
    List<GateServiceEntity> queryAll(GateServiceSearchRequest request);

    /**
     * 加锁
     *
     * @param serviceId
     */
    void lock(Long serviceId);
}