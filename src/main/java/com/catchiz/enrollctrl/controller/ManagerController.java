package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.CommonResult;
import com.catchiz.enrollctrl.pojo.CommonStatus;
import com.catchiz.enrollctrl.pojo.User;
import com.catchiz.enrollctrl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ManagerController {
    @Autowired
    private UserService userService;

    @DeleteMapping("/delUser")
    public CommonResult delUser(String username){
        userService.delUser(username);
        return new CommonResult(CommonStatus.OK,"删除成功");
    }

    @GetMapping("/listAllUser")
    public CommonResult listAllUser(){
        List<User> userList=userService.listAllUser();
        return new CommonResult(CommonStatus.OK,"查询成功",userList);
    }
}
