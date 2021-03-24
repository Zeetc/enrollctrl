package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.CommonResult;
import com.catchiz.enrollctrl.pojo.CommonStatus;
import com.catchiz.enrollctrl.pojo.Department;
import com.catchiz.enrollctrl.pojo.User;
import com.catchiz.enrollctrl.service.DepartmentService;
import com.catchiz.enrollctrl.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ManagerController {
    private final UserService userService;

    private final DepartmentService departmentService;

    public ManagerController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @DeleteMapping("/delUser")
    @ApiOperation("删除用户")
    public CommonResult delUser(String username){
        userService.delUser(username);
        return new CommonResult(CommonStatus.OK,"删除成功");
    }

    @GetMapping("/listAllUser")
    @ApiOperation("获取所有用户")
    public CommonResult listAllUser(){
        List<User> userList=userService.listAllUser();
        return new CommonResult(CommonStatus.OK,"查询成功",userList);
    }

    @GetMapping("/listAllDepartment")
    @ApiOperation("获取所有部门")
    public CommonResult listAllDepartment(){
        List<Department> department=departmentService.listAllDepartment();
        return new CommonResult(CommonStatus.OK,"查询成功",department);
    }

    @PatchMapping("/changeDepartmentName")
    @ApiOperation("更改部门名字")
    public CommonResult changeDepartmentName(Integer departmentId,String name){
        departmentService.changeDepartmentName(departmentId,name);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/changeDepartmentDescribe")
    @ApiOperation("更改部门描述")
    public CommonResult changeDepartmentDescribe(Integer departmentId,String describe){
        departmentService.changeDepartmentDescribe(departmentId,describe);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

}
