package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.CommonResult;
import com.catchiz.enrollctrl.pojo.CommonStatus;
import com.catchiz.enrollctrl.pojo.Department;
import com.catchiz.enrollctrl.pojo.User;
import com.catchiz.enrollctrl.service.DepartmentService;
import com.catchiz.enrollctrl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ManagerController {
    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

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

    @GetMapping("/listAllDepartment")
    public CommonResult listAllDepartment(){
        List<Department> department=departmentService.listAllDepartment();
        return new CommonResult(CommonStatus.OK,"查询成功",department);
    }

    @PatchMapping("/changeDepartmentName")
    public CommonResult changeDepartmentName(Integer departmentId,String name){
        departmentService.changeDepartmentName(departmentId,name);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/changeDepartmentDescribe")
    public CommonResult changeDepartmentDescribe(Integer departmentId,String describe){
        departmentService.changeDepartmentDescribe(departmentId,describe);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

}
