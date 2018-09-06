package com.moguhu.baize.metadata.dao.mapper.user;

import com.moguhu.baize.metadata.entity.user.UserEntity;

public interface UserEntityMapper {
    /**
     * 通过id物理删除user的数据.
     */
    int deleteById(Long userId);

    /**
     * 向表user中插入数据.
     */
    int insert(UserEntity record);

    /**
     * 通过id查询表user.
     */
    UserEntity selectById(Long userId);

    /**
     * 通过id修改表user.
     */
    int updateById(UserEntity record);

    /**
     * 根据登录名获取用户信息
     *
     * @param username
     * @return
     */
    UserEntity findByName(String username);
}