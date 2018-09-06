package com.moguhu.baize.service.user.impl;

import com.moguhu.baize.common.constants.user.RoleEnum;
import com.moguhu.baize.common.vo.user.UserRole;
import com.moguhu.baize.metadata.dao.mapper.user.UserEntityMapper;
import com.moguhu.baize.metadata.entity.user.UserEntity;
import com.moguhu.baize.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务
 *
 * @author xuefeihu
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Override
    public UserEntity findByName(String username) {
        return userEntityMapper.findByName(username);
    }

    @Override
    public List<UserRole> getRoleByUser(UserEntity user) {
        List<UserRole> list = new ArrayList<>();
        list.add(new UserRole(RoleEnum.USER.desc()));
        return list;
    }
}
