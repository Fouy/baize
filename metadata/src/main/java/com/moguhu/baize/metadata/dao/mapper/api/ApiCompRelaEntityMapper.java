package com.moguhu.baize.metadata.dao.mapper.api;

import com.moguhu.baize.metadata.entity.api.ApiCompRelaEntity;

public interface ApiCompRelaEntityMapper {
    /**
     * 通过id物理删除api_comp_rela的数据.
     */
    int deleteById(Long apiCompRelaId);

    /**
     * 向表api_comp_rela中插入数据.
     */
    int insert(ApiCompRelaEntity record);

    /**
     * 通过id查询表api_comp_rela.
     */
    ApiCompRelaEntity selectById(Long apiCompRelaId);

    /**
     * 通过id修改表api_comp_rela.
     */
    int updateById(ApiCompRelaEntity record);
}