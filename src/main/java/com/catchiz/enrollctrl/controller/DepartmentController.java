package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.CommonResult;
import com.catchiz.enrollctrl.pojo.CommonStatus;
import com.catchiz.enrollctrl.pojo.User;
import com.catchiz.enrollctrl.service.DepartmentService;
import com.catchiz.enrollctrl.service.UserService;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    private final UserService userService;

    public DepartmentController(DepartmentService departmentService, UserService userService) {
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @PatchMapping("/changeDepartmentDescribe")
    @ApiOperation("更改部门描述")
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
