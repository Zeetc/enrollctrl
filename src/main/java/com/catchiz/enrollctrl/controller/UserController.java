package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.*;
import com.catchiz.enrollctrl.service.ProblemService;
import com.catchiz.enrollctrl.service.QuestionnaireService;
import com.catchiz.enrollctrl.service.UserService;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private QuestionnaireService questionnaireService;

    @Value("${spring.mail.username}")
    private String emailSendUser;


    @GetMapping("/getInviteCode")
    public CommonResult getInviteCode(@RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        Integer departmentId=userService.getDepartmentIdByUsername(username);
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String inviteCode = UUID.randomUUID().toString().substring(0,7);
        operations.set(inviteCode,departmentId.toString());
        return new CommonResult(CommonStatus.CREATE,"创建成功",inviteCode);
    }

    @PatchMapping("/changePassword")
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

    @GetMapping("/applyForChangeEmail")
    public CommonResult applyForChangeEmail(@RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        if(!StringUtils.hasText(username))return new CommonResult(CommonStatus.FORBIDDEN,"输入不合法");
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        if(StringUtils.hasText(operations.get(username)))return new CommonResult(CommonStatus.FORBIDDEN,"频繁请求, 1分钟后再试");
        return sendEmailVerifyCode(username, operations, emailSendUser, userService, mailSender);
    }

    @GetMapping("/listProblemsByQuestionnaireId")
    public CommonResult listProblemsByQuestionnaireId(Integer questionnaireId,
                                                      @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user= userService.getUserByUsername(username);
        Questionnaire questionnaire=questionnaireService.getQuestionnaireByQuestionnaireId(questionnaireId);
        if(user.getDepartmentId().equals(questionnaire.getDepartmentId())){
            return new CommonResult(CommonStatus.FORBIDDEN,"没有权限");
        }
        List<Problem> problemList=problemService.listProblemsByQuestionnaireId(questionnaireId);
        return new CommonResult(CommonStatus.OK,"查询成功",problemList);
    }


    static CommonResult sendEmailVerifyCode(String username, ValueOperations<String, String> operations, String emailSendUser, UserService userService, JavaMailSender mailSender) {
        String uuid= UUID.randomUUID().toString().substring(0,6);
        operations.set(uuid, username,24, TimeUnit.HOURS);
        operations.set(username,username,1, TimeUnit.MINUTES);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSendUser);
        message.setTo(userService.getEmailByUsername(username));
        message.setSubject("修改密码邮箱验证");
        message.setText("验证码是："+uuid);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            return new CommonResult(CommonStatus.EXCEPTION,"邮箱发送失败");
        }
        return new CommonResult(CommonStatus.OK,"申请成功");
    }

}
