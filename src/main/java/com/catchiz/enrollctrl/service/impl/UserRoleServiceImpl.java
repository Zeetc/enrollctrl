package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.mapper.UserRoleMapper;
import com.catchiz.enrollctrl.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public void insertUserRole(String username, int roleId) {
        userRoleMapper.insertUserRole(username,roleId);
    }

    @Override
    public List<Integer> getRoles(String username) {
        return userRoleMapper.getRoles(username);
    }
}
