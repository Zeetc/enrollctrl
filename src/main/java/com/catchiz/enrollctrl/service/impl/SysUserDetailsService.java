package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.pojo.PermissionEntity;
import com.catchiz.enrollctrl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SysUserDetailsService implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals(""))throw new UsernameNotFoundException("用户ID不合法！");
        com.catchiz.enrollctrl.pojo.User myUser = userService.loadUserByUsername(username);
        if (myUser == null){
            throw new UsernameNotFoundException("用户不存在！");
        }
        log.info("用户存在，用户:"+username);
        //返回数据对象(手动已加密)
        User.UserBuilder userBuilder = User.withUsername(username);
        userBuilder.password(myUser.getPassword());
        List<SimpleGrantedAuthority> userGrantedAuthorities = new ArrayList<>();
        for (PermissionEntity permissionEntity : myUser.getPermissionEntities()) {
            userGrantedAuthorities.add(new SimpleGrantedAuthority(permissionEntity.getPermissionName()));
        }
        userBuilder.authorities(userGrantedAuthorities);
        return userBuilder.build();
    }
}