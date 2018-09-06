package com.moguhu.baize.metadata.dao.mapper.backend;

import com.moguhu.baize.metadata.entity.backend.NodeGroupRelaEntity;

public interface NodeGroupRelaEntityMapper {
    /**
     * 通过id物理删除node_group_rela的数据.
     */
    int deleteById(Long nodeRelaId);

    /**
     * 向表node_group_rela中插入数据.
     */
    int insert(NodeGroupRelaEntity record);

    /**
     * 通过id查询表node_group_rela.
     */
    NodeGroupRelaEntity selectById(Long nodeRelaId);

    /**
     * 通过id修改表node_group_rela.
     */
    int updateById(NodeGroupRelaEntity record);
}