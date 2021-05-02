package com.catchiz.enrollctrl.service;

import java.util.List;

public interface RolePermissionService {
    List<Integer> getPermissionIdsByRoleId(Integer roleId);
}
