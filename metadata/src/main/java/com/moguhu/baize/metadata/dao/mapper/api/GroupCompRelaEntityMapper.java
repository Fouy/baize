package com.moguhu.baize.metadata.dao.mapper.api;

import com.moguhu.baize.metadata.entity.api.GroupCompRelaEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupCompRelaEntityMapper {
    /**
     * 通过id物理删除group_comp_rela的数据.
     */
    int deleteById(Long groupCompRelaId);

    /**
     * 向表group_comp_rela中插入数据.
     */
    int insert(GroupCompRelaEntity record);

    /**
     * 通过id查询表group_comp_rela.
     */
    GroupCompRelaEntity selectById(Long groupCompRelaId);

    /**
     * 通过id修改表group_comp_rela.
     */
    int updateById(GroupCompRelaEntity record);

    /**
     * 根据groupId 删除
     *
     * @param groupId
     */
    void deleteByGroupId(Long groupId);

    /**
     * 批量插入数据
     *
     * @param batchList
     */
    void batchInsert(@Param("batchList") List<GroupCompRelaEntity> batchList);

    /**
     * 根据groupId查询组件ID列表
     *
     * @param groupId
     * @return
     */
    List<Long> queryByApiGroup(Long groupId);
}