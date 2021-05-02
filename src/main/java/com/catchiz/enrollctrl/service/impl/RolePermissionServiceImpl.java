package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.mapper.RolePermissionMapper;
import com.catchiz.enrollctrl.service.RolePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    private final RolePermissionMapper rolePermissionMapper;

    public RolePermissionServiceImpl(RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public List<Integer> getPermissionIdsByRoleId(Integer roleId) {
        return rolePermissionMapper.getPermissionIdsByRoleId(roleId);
    }
}
