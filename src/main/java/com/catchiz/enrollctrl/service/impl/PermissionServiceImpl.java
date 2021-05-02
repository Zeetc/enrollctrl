package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.mapper.PermissionMapper;
import com.catchiz.enrollctrl.pojo.PermissionEntity;
import com.catchiz.enrollctrl.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Override
    public PermissionEntity getPermissionById(Integer permissionId) {
        PermissionEntity p = permissionMapper.getPermissionById(permissionId);
        return permissionMapper.getPermissionById(permissionId);
    }
}
