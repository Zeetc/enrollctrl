package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.*;
import com.catchiz.enrollctrl.service.ArticleService;
import com.catchiz.enrollctrl.service.ProblemService;
import com.catchiz.enrollctrl.service.QuestionnaireService;
import com.catchiz.enrollctrl.service.UserService;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final StringRedisTemplate redisTemplate;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    private final ProblemService problemService;

    private final QuestionnaireService questionnaireService;

    private final ThreadPoolExecutor executor;

    @Value("${spring.mail.username}")
    private String emailSendUser;

    public UserController(UserService userService, StringRedisTemplate redisTemplate, PasswordEncoder passwordEncoder, JavaMailSender mailSender, ProblemService problemService, QuestionnaireService questionnaireService, ThreadPoolExecutor executor, ArticleService articleService) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.problemService = problemService;
        this.questionnaireService = questionnaireService;
        this.executor = executor;
        this.articleService = articleService;
    }


    @GetMapping("/getInviteCode")
    @ApiOperation("生成邀请码，有效期timeOut天")
    @PreAuthorize(value = "hasAnyAuthority('manager','inviteCode')")
    public CommonResult getInviteCode(@RequestHeader String Authorization,Integer timeOut){
        return ManagerController.generateInviteCode(Authorization, userService, redisTemplate,timeOut);
    }

    @PatchMapping("/changePassword")
    @ApiOperation("更改密码")
    public CommonResult changePassword(String originPassword,
                                       String newPassword,
                                       @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user=userService.getUserByUsername(username);
        if(!passwordEncoder.matches(originPassword,user.getPassword())){
            return new CommonResult(CommonStatus.FORBIDDEN,"原密码错误");
        }
        userService.changePassword(username,newPassword);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/changeUsername")
    @ApiOperation("更改用户名，不能与其他用户重名")
    public CommonResult changeUsername(String name,
                                       @RequestHeader String Authorization){
        if(userService.hasSameUsername(name))return new CommonResult(CommonStatus.FORBIDDEN,"用户名已被占用");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user=userService.loadUserByUsername(username);
        if(user==null)return new CommonResult(CommonStatus.OK,"无该用户");
        userService.changeUsername(name,username);
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",name);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (PermissionEntity permissionEntity : user.getPermissionEntities()) {
            authorities.add(new SimpleGrantedAuthority(permissionEntity.getPermissionName()));
        }
        claims.put("authorities",authorities);
        String token = JwtTokenUtil.generateToken(claims);
        return new CommonResult(CommonStatus.OK,"修改成功",token);
    }

    @PatchMapping("/changeDescribe")
    @ApiOperation("更改个人描述")
    public CommonResult changeDescribe(String describe,
                                       @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        userService.changeDescribe(describe,username);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/changeGender")
    @ApiOperation("更改性别")
    public CommonResult changeGender(Integer gender,
                                     @RequestHeader String Authorization){
        if(gender==null||gender>2||gender<0)return new CommonResult(CommonStatus.FORBIDDEN,"输入不合法");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        userService.changeGender(gender,username);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @GetMapping("/applyForChangeEmail")
    @ApiOperation("申请更改邮箱")
    public CommonResult applyForChangeEmail(@RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        if(!StringUtils.hasText(username))return new CommonResult(CommonStatus.FORBIDDEN,"输入不合法");
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        if(StringUtils.hasText(operations.get(username+"#")))return new CommonResult(CommonStatus.FORBIDDEN,"频繁请求, 1分钟后再试");
        return sendEmailVerifyCode(username, operations, emailSendUser, userService, mailSender);
    }

    @PatchMapping("/resetEmail")
    @ApiOperation("更改邮箱")
    public CommonResult resetEmail(String inputVerify,String email,
                                   @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        if(!StringUtils.hasText(username)||!StringUtils.hasText(inputVerify))return new CommonResult(CommonStatus.FORBIDDEN,"输入不合法");
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String verifyCode = operations.get(username);
        if(verifyCode==null||!verifyCode.equalsIgnoreCase(inputVerify)){
            return new CommonResult(CommonStatus.NOTFOUND,"验证码错误");
        }
        userService.resetEmail(username,email);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    private final ArticleService articleService;

    @GetMapping("/getMyDepartmentArticle")
    @ApiOperation("获取自己部门的article")
    public CommonResult getMyDepartmentArticle(@RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        if(!StringUtils.hasText(username))return new CommonResult(CommonStatus.FORBIDDEN,"输入不合法");
        User user = userService.getUserByUsername(username);
        Article article = articleService.getArticleByDepartmentId(user.getDepartmentId());
        return new CommonResult(CommonStatus.OK,"查询成功",article);
    }


    static CommonResult sendEmailVerifyCode(String username, ValueOperations<String, String> operations, String emailSendUser, UserService userService, JavaMailSender mailSender) {
        String uuid= UUID.randomUUID().toString().substring(0,6);
        operations.set(username,uuid,24, TimeUnit.HOURS);
        operations.set(username+"#","check",1,TimeUnit.MINUTES);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSendUser);
        message.setTo(userService.getEmailByUsername(username));
        message.setSubject("修改验证");
        message.setText("验证码是："+uuid);
        try {
            CompletableFuture.runAsync(()->mailSender.send(message));
        } catch (Exception e) {
            log.error("尝试发送邮箱失败"+username);
        }
        return new CommonResult(CommonStatus.OK,"申请成功");
    }

}
