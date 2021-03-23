package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.CommonResult;
import com.catchiz.enrollctrl.pojo.CommonStatus;
import com.catchiz.enrollctrl.service.UserService;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Resource
    private StringRedisTemplate redisTemplate;

    @GetMapping("/getInviteCode")
    public CommonResult getInviteCode(@RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        Integer departmentId=userService.getDepartmentIdByUsername(username);
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String inviteCode = UUID.randomUUID().toString().substring(0,7);
        operations.set(inviteCode,departmentId.toString());
        return new CommonResult(CommonStatus.CREATE,"创建成功",inviteCode);
    }

    @PatchMapping("/changeUsername")
    public CommonResult changeUsername(String name,
                                       @RequestHeader String Authorization){
        if(userService.hasSameUsername(name))return new CommonResult(CommonStatus.FORBIDDEN,"用户名已被占用");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        userService.changeUsername(name,username);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/changeDescribe")
    public CommonResult changeDescribe(String describe,
                                       @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        userService.changeDescribe(describe,username);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/changeGender")
    public CommonResult changeGender(Integer gender,
                                     @RequestHeader String Authorization){
        if(gender==null||gender>2||gender<0)return new CommonResult(CommonStatus.FORBIDDEN,"输入不合法");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        userService.changeGender(gender,username);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

}
