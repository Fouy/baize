package com.moguhu.baize.metadata.dao.mapper.backend;


import com.moguhu.baize.metadata.entity.backend.GateNodeEntity;

public interface GateNodeEntityMapper {
    /**
     * 通过id物理删除gate_node的数据.
     */
    int deleteById(Long nodeId);

    /**
     * 向表gate_node中插入数据.
     */
    int insert(GateNodeEntity record);

    /**
     * 通过id查询表gate_node.
     */
    GateNodeEntity selectById(Long nodeId);

    /**
     * 通过id修改表gate_node.
     */
    int updateById(GateNodeEntity record);
}