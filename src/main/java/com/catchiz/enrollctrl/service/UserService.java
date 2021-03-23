package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.User;

import java.util.List;

public interface UserService {
    boolean hasSameUsername(String username);

    void register(User user);

    String getEmailByUsername(String username);

    void resetPassword(String username, String password);

    User getUserByUsername(String username);

    void delUser(String username);

    List<User> listAllUser();
}
