package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.RoleEntity;

import java.util.List;

public interface UserRoleService {
    void insertUserRole(String username, int roleId);

    List<Integer> getRoles(String username);
}
