package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.mapper.UserMapper;
import com.catchiz.enrollctrl.pojo.User;
import com.catchiz.enrollctrl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean hasSameUsername(String username) {
        return userMapper.hasSameUsername(username)==1;
    }

    @Override
    public void register(User user) {
        String password=user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        userMapper.register(user);
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
}
