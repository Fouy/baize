package com.moguhu.baize.service.user;

import com.moguhu.baize.common.vo.user.UserRole;
import com.moguhu.baize.metadata.entity.user.UserEntity;

import java.util.List;

/**
 * 用户服务
 *
 * @author xuefeihu
 */
public interface UserService {

    /**
     * 根据登录名获取用户信息
     *
     * @param username
     * @return
     */
    UserEntity findByName(String username);

    /**
     * 获取用户角色
     *
     * @param user
     * @return
     */
    List<UserRole> getRoleByUser(UserEntity user);
}
