package com.moguhu.baize.metadata.mapper.backend;

import com.moguhu.baize.metadata.entity.backend.ComponentEntity;
import com.moguhu.baize.metadata.request.backend.ComponentSearchRequest;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据 groupId 查询组件
     *
     * @param groupId
     * @return
     */
    List<ComponentEntity> queryByApiGroup(Long groupId);

    /**
     * 根据 apiId 查询组件
     *
     * @param apiId
     * @return
     */
    List<ComponentEntity> queryByApi(Long apiId);

    /**
     * 根据 compList 获取组件列表
     *
     * @param compList
     * @return
     */
    List<ComponentEntity> selectByIds(@Param("compList") List<Long> compList);
}