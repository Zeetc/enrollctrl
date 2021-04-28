package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.*;
import com.catchiz.enrollctrl.service.*;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/manager")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ManagerController {
    private final UserService userService;

    private final ProblemService problemService;
    private final AnswerService answerService;

    private final DepartmentService departmentService;

    private final StringRedisTemplate redisTemplate;

    public ManagerController(UserService userService, DepartmentService departmentService, ProblemService problemService, AnswerService answerService, StringRedisTemplate redisTemplate, QuestionnaireService questionnaireService, AnswerAuthorService answerAuthorService, ThreadPoolExecutor executor) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.problemService = problemService;
        this.answerService = answerService;
        this.redisTemplate = redisTemplate;
        this.questionnaireService = questionnaireService;
        this.answerAuthorService = answerAuthorService;
        this.executor = executor;
    }

    @GetMapping("/getInviteCode")
    @ApiOperation("生成邀请码，需要指定部门Id,有效期timeOut天,有效期最大为7天")
    public CommonResult getInviteCode(Integer departmentId,Integer timeOut){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String inviteCode = UUID.randomUUID().toString().substring(0,7);
        operations.set(inviteCode,departmentId.toString(),timeOut, TimeUnit.DAYS);
        return new CommonResult(CommonStatus.CREATE,"创建成功",inviteCode);
    }

    static CommonResult generateInviteCode(@RequestHeader String Authorization, UserService userService, StringRedisTemplate redisTemplate,Integer timeOut) {
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        Integer departmentId= userService.getDepartmentIdByUsername(username);
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String inviteCode = UUID.randomUUID().toString().substring(0,7);
        operations.set(inviteCode,departmentId.toString(),timeOut, TimeUnit.DAYS);
        return new CommonResult(CommonStatus.CREATE,"创建成功",inviteCode);
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

    @PostMapping("/addDepartment")
    @ApiOperation("新建部门")
    public CommonResult addDepartment(@RequestBody Department department){
        departmentService.addDepartment(department);
        return new CommonResult(CommonStatus.CREATE,"创建成功");
    }

    @GetMapping("/getAnswerByQuestionnaireId")
    @ApiOperation("获取该问卷的所有答案，按照题目区分list ==>（list[0]就是第一题的所有答案）")
    public CommonResult getAnswerByQuestionnaireId(Integer questionnaireId){
        if(questionnaireId==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        return getQuestionnaireAns(questionnaireId, problemService, answerService);
    }

    private final QuestionnaireService questionnaireService;

    @PatchMapping("/changeProblem")
    @ApiOperation(("修改问卷题目"))
    public CommonResult changeProblem(@RequestBody Problem p){
        Problem problem= problemService.getProblemByProblemId(p.getId());
        Questionnaire questionnaire = questionnaireService.getQuestionnaireByQuestionnaireId(problem.getQuestionnaireId());
        if(questionnaire==null)return new CommonResult(CommonStatus.NOTFOUND,"未找到问卷");
        problemService.changeProblem(p);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @GetMapping("/listProblemsByQuestionnaireId")
    @ApiOperation("获取某问卷的所有题目")
    public CommonResult listProblemsByQuestionnaireId(Integer questionnaireId){
        List<Problem> problemList=problemService.listProblemsByQuestionnaireId(questionnaireId);
        return new CommonResult(CommonStatus.OK,"查询成功",problemList);
    }

    @GetMapping("/getAllAnswerAuthor")
    @ApiOperation(("获取回答某个问卷的所有用户以及他们的用户信息"))
    public CommonResult getAllAnswerAuthor(Integer questionnaireId){
        List<AnswerAuthor> answerAuthors = answerService.getAllAnswerAuthor(questionnaireId);
        return new CommonResult(CommonStatus.OK,"获取成功",answerAuthors);
    }

    @GetMapping("/getAnswerByAuthor")
    @ApiOperation(("获取某个回答者的回答内容"))
    public CommonResult getAnswerByAuthor(Integer answerAuthorId){
        List<Answer> answers = answerService.getAnswerByAuthor(answerAuthorId);
        return new CommonResult(CommonStatus.OK,"获取成功",answers);
    }

    @GetMapping("/getSingleQuestion")
    @ApiOperation("获取单个问题，根据Id")
    public CommonResult getSingleQuestion(Integer problemId){
        if(problemId ==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        Problem problem=problemService.getProblemByProblemId(problemId);
        if(problem==null){
            return new CommonResult(CommonStatus.FORBIDDEN,"无该问题");
        }
        return new CommonResult(CommonStatus.OK,"查询成功",problem);
    }

    private final ThreadPoolExecutor executor;

    @GetMapping("/sendAllUserEmail")
    @ApiOperation("群发消息，根据问卷Id")
    public CommonResult sendAllUserEmail(Integer questionnaireId,
                                         String title,
                                         String msg){
        CompletableFuture.runAsync(()-> answerService.sendAllUserEmail(questionnaireId,title,msg),executor);
        return new CommonResult(CommonStatus.OK,"发送成功");
    }

    private final AnswerAuthorService answerAuthorService;

    @PatchMapping("/updateAnswerAuthorName")
    @ApiOperation("修改报名信息--名字")
    public CommonResult updateAnswerAuthorName(Integer authorId,String authorName){
        if(authorId ==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        AnswerAuthor answerAuthor=answerAuthorService.getAuthorById(authorId);
        if(answerAuthor==null){
            return new CommonResult(CommonStatus.FORBIDDEN,"无该回答者");
        }
        answerAuthorService.updateAnswerAuthorName(authorId,authorName);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/updateAnswerAuthorEmail")
    @ApiOperation("修改报名信息--邮箱")
    public CommonResult updateAnswerAuthorEmail(Integer authorId,String email){
        if(authorId ==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        AnswerAuthor answerAuthor=answerAuthorService.getAuthorById(authorId);
        if(answerAuthor==null)return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        answerAuthorService.updateAnswerAuthorEmail(authorId,email);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }


    static CommonResult getQuestionnaireAns(Integer questionnaireId, ProblemService problemService, AnswerService answerService) {
        List<Problem> problems= problemService.getProblemIdsByQuestionnaireId(questionnaireId);
        problems.sort(Comparator.comparingInt(Problem::getIndex));
        List<List<Answer>> lists=new ArrayList<>();
        for (Problem problem : problems) {
            lists.add(answerService.getAnsByProblemId(problem.getId()));
        }
        return new CommonResult(CommonStatus.OK,"查询成功",lists);
    }

}
