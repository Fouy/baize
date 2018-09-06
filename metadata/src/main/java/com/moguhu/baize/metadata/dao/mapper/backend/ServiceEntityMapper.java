package com.moguhu.baize.metadata.dao.mapper.backend;

import com.moguhu.baize.metadata.entity.backend.ServiceEntity;

public interface ServiceEntityMapper {
    /**
     * 通过id物理删除service的数据.
     */
    int deleteById(Long serviceId);

    /**
     * 向表service中插入数据.
     */
    int insert(ServiceEntity record);

    /**
     * 通过id查询表service.
     */
    ServiceEntity selectById(Long serviceId);

    /**
     * 通过id修改表service.
     */
    int updateById(ServiceEntity record);
}