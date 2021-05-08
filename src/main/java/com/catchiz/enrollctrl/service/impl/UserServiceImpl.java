package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.mapper.UserMapper;
import com.catchiz.enrollctrl.pojo.PermissionEntity;
import com.catchiz.enrollctrl.pojo.User;
import com.catchiz.enrollctrl.service.PermissionService;
import com.catchiz.enrollctrl.service.RolePermissionService;
import com.catchiz.enrollctrl.service.UserRoleService;
import com.catchiz.enrollctrl.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;

    public UserServiceImpl(UserMapper userMapper, UserRoleService userRoleService, RolePermissionService rolePermissionService, PermissionService permissionService) {
        this.userMapper = userMapper;
        this.userRoleService = userRoleService;
        this.rolePermissionService = rolePermissionService;
        this.permissionService = permissionService;
    }

    @Override
    public boolean hasSameUsername(String username) {
        return userMapper.hasSameUsername(username)==1;
    }

    @Override
    @Transactional
    public void register(User user) {
        String password=user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        userMapper.register(user);
        userRoleService.insertUserRole(user.getUsername(),2);
    }

    @Override
    public String getEmailByUsername(String username) {
        return userMapper.getEmailByUsername(username);
    }

    @Override
    public void resetPassword(String username, String password) {
        userMapper.resetPassword(username,password);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public void delUser(String username) {
        userMapper.delUser(username);
    }

    @Override
    public List<User> listAllUser() {
        return userMapper.listAllUser();
    }

    @Override
    public Integer getDepartmentIdByUsername(String username) {
        return userMapper.getDepartmentIdByUsername(username);
    }

    @Override
    public void changeUsername(String name, String username) {
        userMapper.changeUsername(name,username);
    }

    @Override
    public void changeDescribe(String describe, String username) {
        userMapper.changeDescribe(describe,username);
    }

    @Override
    public void changeGender(Integer gender, String username) {
        userMapper.changeGender(gender,username);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        userMapper.changePassword(username,passwordEncoder.encode(newPassword));
    }

    @Override
    public void resetEmail(String username, String email) {
        userMapper.resetEmail(username,email);
    }

    @Override
    public List<User> listAllUserByDepartmentId(Integer departmentId) {
        return userMapper.listAllUserByDepartmentId(departmentId);
    }

    private final RolePermissionService rolePermissionService;

    private final PermissionService permissionService;

    @Override
    public User loadUserByUsername(String username) {
        User user = userMapper.getUserByUsername(username);
        if(user==null)return null;
        user.setPermissionEntities(new ArrayList<>());
        List<Integer> roles = userRoleService.getRoles(user.getUsername());
        Set<String> set = new HashSet<>();
        for (Integer roleId : roles) {
            List<Integer> permissionIds = rolePermissionService.getPermissionIdsByRoleId(roleId);
            for (Integer permissionId : permissionIds) {
                PermissionEntity permissionEntity = permissionService.getPermissionById(permissionId);
                if(set.contains(permissionEntity.getPermissionName()))continue;
                user.getPermissionEntities().add(permissionEntity);
                set.add(permissionEntity.getPermissionName());
            }
        }
        return user;
    }
}
