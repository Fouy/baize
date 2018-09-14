package com.moguhu.baize.metadata.dao.mapper.common;

import com.moguhu.baize.metadata.entity.common.RichContentEntity;

public interface RichContentEntityMapper {
    /**
     * 通过id物理删除rich_content的数据.
     */
    int deleteById(Long contentId);

    /**
     * 向表rich_content中插入数据.
     */
    int insert(RichContentEntity record);

    /**
     * 通过id查询表rich_content.
     */
    RichContentEntity selectById(Long contentId);

    /**
     * 通过id修改表rich_content.
     */
    int updateById(RichContentEntity record);

    /**
     * 加锁
     *
     * @param contentId
     */
    void lock(Long contentId);
}