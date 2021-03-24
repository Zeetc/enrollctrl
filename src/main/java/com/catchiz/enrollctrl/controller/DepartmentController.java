package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.CommonResult;
import com.catchiz.enrollctrl.pojo.CommonStatus;
import com.catchiz.enrollctrl.pojo.User;
import com.catchiz.enrollctrl.service.DepartmentService;
import com.catchiz.enrollctrl.service.UserService;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @PatchMapping("/changeDepartmentName")
    public CommonResult changeDepartmentName(Integer departmentId,String name,
                                             @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user= userService.getUserByUsername(username);
        if(!user.getDepartmentId().equals(departmentId)){
            return new CommonResult(CommonStatus.FORBIDDEN,"没有权限");
        }
        departmentService.changeDepartmentName(departmentId,name);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/changeDepartmentDescribe")
    public CommonResult changeDepartmentDescribe(Integer departmentId,String describe,
                                                 @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user= userService.getUserByUsername(username);
        if(!user.getDepartmentId().equals(departmentId)){
            return new CommonResult(CommonStatus.FORBIDDEN,"没有权限");
        }
        departmentService.changeDepartmentDescribe(departmentId,describe);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }
}
