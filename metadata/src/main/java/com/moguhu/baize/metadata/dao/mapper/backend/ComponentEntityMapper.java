package com.moguhu.baize.metadata.dao.mapper.backend;

import com.moguhu.baize.metadata.entity.backend.ComponentEntity;
import com.moguhu.baize.metadata.request.backend.ComponentSearchRequest;

import java.util.List;

public interface ComponentEntityMapper {
    /**
     * 通过id物理删除component的数据.
     */
    int deleteById(Long compId);

    /**
     * 向表component中插入数据.
     */
    int insert(ComponentEntity record);

    /**
     * 通过id查询表component.
     */
    ComponentEntity selectById(Long compId);

    /**
     * 通过id修改表component.
     */
    int updateById(ComponentEntity record);

    /**
     * 查询列表
     *
     * @param request
     * @return
     */
    List<ComponentEntity> queryAll(ComponentSearchRequest request);

    /**
     * 加锁
     *
     * @param compId
     */
    void lock(Long compId);
}