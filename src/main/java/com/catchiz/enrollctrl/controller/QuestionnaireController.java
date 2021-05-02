package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.*;
import com.catchiz.enrollctrl.service.*;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("/question")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    private final UserService userService;
    private final ProblemService problemService;
    private final AnswerService answerService;

    private final AnswerAuthorService answerAuthorService;

    public QuestionnaireController(QuestionnaireService questionnaireService, UserService userService, ProblemService problemService, AnswerService answerService, AnswerAuthorService answerAuthorService, ThreadPoolExecutor executor) {
        this.questionnaireService = questionnaireService;
        this.userService = userService;
        this.problemService = problemService;
        this.answerService = answerService;
        this.answerAuthorService = answerAuthorService;
        this.executor = executor;
    }

    @PostMapping("/insertQuestionnaire")
    @ApiOperation("创建问卷并提交")
    @PreAuthorize(value = "hasAnyAuthority('manager','createQuestionnaire')")
    public CommonResult insertQuestionnaire(@RequestBody Questionnaire questionnaire,
                                            @RequestBody List<Problem> problems,
                                            @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        Integer departmentId= userService.getDepartmentIdByUsername(username);
        if(departmentId==null)return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        questionnaireService.insertQuestionnaire(questionnaire,problems);
        return new CommonResult(CommonStatus.CREATE,"创建成功",questionnaire.getId());
    }

    @GetMapping("/getAnswerByQuestionnaireId")
    @ApiOperation("获取该问卷的所有答案，按照题目区分list ==>（list[0]就是第一题的所有答案）")
    @PreAuthorize(value = "hasAnyAuthority('manager','getAnswer')")
    public CommonResult getAnswerByQuestionnaireId(Integer questionnaireId,
                                                   @RequestHeader String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        return ManagerController.getQuestionnaireAns(questionnaireId, problemService, answerService);
    }

    @GetMapping("/getAllAnswerAuthor")
    @ApiOperation(("获取回答某个问卷的所有用户以及他们的用户信息"))
    @PreAuthorize(value = "hasAnyAuthority('manager','getAnswer')")
    public CommonResult getAllAnswerAuthor(Integer questionnaireId,
                                           @RequestHeader String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        List<AnswerAuthor> answerAuthors = answerService.getAllAnswerAuthor(questionnaireId);
        return new CommonResult(CommonStatus.OK,"获取成功",answerAuthors);
    }

    @GetMapping("/compareTo")
    @ApiOperation("指定两份问卷，对比结果，第一个list存两个问卷都有的用户，第二个list存被淘汰的用户")
    @PreAuthorize(value = "hasAnyAuthority('manager','getAnswer')")
    public CommonResult compareTo(Integer first, Integer second, @RequestHeader String Authorization){
        CommonResult FORBIDDEN1 = checkPermission(first, Authorization);
        if (FORBIDDEN1 != null) return FORBIDDEN1;
        CommonResult FORBIDDEN2 = checkPermission(second, Authorization);
        if (FORBIDDEN2 != null) return FORBIDDEN2;
        Set<AnswerAuthor> firstAnswerAuthor = new HashSet<>(answerService.getAllAnswerAuthor(first));
        List<AnswerAuthor> secondAnswerAuthor = answerService.getAllAnswerAuthor(second);
        List<AnswerAuthor> both = new ArrayList<>();
        List<AnswerAuthor> reject = new ArrayList<>();
        for (AnswerAuthor answerAuthor : secondAnswerAuthor) {
            if(firstAnswerAuthor.contains(answerAuthor)){
                both.add(answerAuthor);
            }else reject.add(answerAuthor);
        }
        return new CommonResult(CommonStatus.OK,"获取成功", Arrays.asList(both,reject));
    }

    @GetMapping("/getAnswerByAuthor")
    @ApiOperation(("获取某个回答者的回答内容"))
    @PreAuthorize(value = "hasAnyAuthority('manager','getAnswer')")
    public CommonResult getAnswerByAuthor(Integer questionnaireId,Integer answerAuthorId,
                                          @RequestHeader String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        List<Answer> answers = answerService.getAnswerByAuthor(answerAuthorId);
        return new CommonResult(CommonStatus.OK,"获取成功",answers);
    }

    @GetMapping("/getSingleQuestion")
    @ApiOperation("获取单个问题，根据Id")
    @PreAuthorize(value = "hasAnyAuthority('manager','getQuestionnaire')")
    public CommonResult getSingleQuestion(Integer problemId,
                                          @RequestHeader String Authorization){
        if(problemId ==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user=userService.getUserByUsername(username);
        if(user==null)return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        Problem problem=problemService.getProblemByProblemId(problemId);
        if(problem==null){
            return new CommonResult(CommonStatus.FORBIDDEN,"无该问题");
        }
        return new CommonResult(CommonStatus.OK,"查询成功",problem);
    }

    private final ThreadPoolExecutor executor;

    @GetMapping("/sendAllUserEmail")
    @ApiOperation("群发消息，根据问卷Id")
    @PreAuthorize(value = "hasAnyAuthority('manager','sendEmail')")
    public CommonResult sendAllUserEmail(Integer questionnaireId,
                                         String title,
                                         String msg,
                                         @RequestHeader String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        CompletableFuture.runAsync(()-> answerService.sendAllUserEmail(questionnaireId,title,msg),executor);
        return new CommonResult(CommonStatus.OK,"发送成功");
    }

    @GetMapping("/sendAllUserEmailIsPass")
    @PreAuthorize(value = "hasAnyAuthority('manager','sendEmail')")
    public CommonResult sendAllUserEmailIsPass(Integer questionnaireId,
                                         String title,
                                         String msg,
                                         @RequestHeader String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        CompletableFuture.runAsync(()-> answerService.sendAllUserEmailIsPass(questionnaireId,title,msg),executor);
        return new CommonResult(CommonStatus.OK,"发送成功");
    }

    @PatchMapping("/updateAnswerAuthorName")
    @ApiOperation("修改报名信息--名字")
    @PreAuthorize(value = "hasAnyAuthority('manager','updateAnswer')")
    public CommonResult updateAnswerAuthorName(Integer authorId,String authorName,
                                               @RequestHeader String Authorization){
        if(authorId ==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user=userService.getUserByUsername(username);
        if(user==null)return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        AnswerAuthor answerAuthor=answerAuthorService.getAuthorById(authorId);
        if(answerAuthor==null){
            return new CommonResult(CommonStatus.FORBIDDEN,"无该回答者");
        }
        answerAuthorService.updateAnswerAuthorName(authorId,authorName);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/updateAnswerAuthorEmail")
    @ApiOperation("修改报名信息--邮箱")
    @PreAuthorize(value = "hasAnyAuthority('manager','updateAnswer')")
    public CommonResult updateAnswerAuthorEmail(Integer authorId,String email,
                                               @RequestHeader String Authorization){
        if(authorId ==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user=userService.getUserByUsername(username);
        AnswerAuthor answerAuthor=answerAuthorService.getAuthorById(authorId);
        if(user==null||answerAuthor==null)return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        answerAuthorService.updateAnswerAuthorEmail(authorId,email);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @PatchMapping("/setIsPass")
    @ApiOperation("将某个考核成员设置成通过")
    @PreAuthorize(value = "hasAnyAuthority('manager','updateAnswer')")
    public CommonResult setIsPass(Integer authorId, Integer isPass,@RequestHeader String Authorization){
        AnswerAuthor author = answerAuthorService.getAuthorById(authorId);
        if(author==null)return new CommonResult(CommonStatus.NOTFOUND,"未找到该回答作者");
        CommonResult FORBIDDEN = checkPermission(author.getQuestionnaireId(), Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        answerAuthorService.setIsPass(authorId,isPass);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @DeleteMapping("/deleteQuestionnaire")
    @ApiOperation("删除问卷，同时会删除问卷的所有答卷，以及回答者的信息")
    @PreAuthorize(value = "hasAnyAuthority('manager','deleteQuestionnaire')")
    public CommonResult deleteQuestionnaire(Integer questionnaireId, String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        questionnaireService.deleteQuestionnaire(questionnaireId);
        return new CommonResult(CommonStatus.OK,"删除成功");
    }

    @PatchMapping("/changeEndDate")
    @ApiOperation("修改问卷截止时间")
    @PreAuthorize(value = "hasAnyAuthority('manager','updateQuestionnaire')")
    public CommonResult changeEndDate(Integer questionnaireId, Timestamp endDate, String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        questionnaireService.changeEndDate(questionnaireId, endDate);
        return new CommonResult(CommonStatus.OK,"删除成功");
    }

    @GetMapping("/listProblemsByQuestionnaireId")
    @ApiOperation("获取某问卷的所有题目")
    @PreAuthorize(value = "hasAnyAuthority('manager','getQuestionnaire')")
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

    @PatchMapping("/changeProblem")
    @ApiOperation(("修改问卷题目"))
    @PreAuthorize(value = "hasAnyAuthority('manager','updateQuestionnaire')")
    public CommonResult changeProblem(@RequestBody Problem p,
                                      @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user= userService.getUserByUsername(username);
        if(user==null)return new CommonResult(CommonStatus.FORBIDDEN,"没有权限");
        Problem problem= problemService.getProblemByProblemId(p.getId());
        Questionnaire questionnaire = questionnaireService.getQuestionnaireByQuestionnaireId(problem.getQuestionnaireId());
        if(questionnaire==null)return new CommonResult(CommonStatus.NOTFOUND,"未找到问卷");
        if(!user.getDepartmentId().equals(questionnaire.getDepartmentId()))return new CommonResult(CommonStatus.FORBIDDEN,"没有权限");
        problemService.changeProblem(p);
        return new CommonResult(CommonStatus.OK,"修改成功");
    }

    @DeleteMapping("/deleteAuthor")
    @ApiOperation("删除作答用户，同时删除作答记录")
    @PreAuthorize(value = "hasAnyAuthority('manager','deleteAnswer')")
    public CommonResult deleteAuthor(Integer authorId, @RequestHeader String Authorization){
        AnswerAuthor answerAuthor = answerAuthorService.getAuthorById(authorId);
        if(answerAuthor == null)return new CommonResult(CommonStatus.NOTFOUND,"未找到作答用户");
        CommonResult FORBIDDEN = checkPermission(answerAuthor.getQuestionnaireId(), Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        answerAuthorService.deleteAuthor(authorId);
        return new CommonResult(CommonStatus.OK,"删除成功");
    }

    private CommonResult checkPermission(Integer questionnaireId, String Authorization) {
        if(questionnaireId ==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user=userService.getUserByUsername(username);
        if(user==null)return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        Questionnaire questionnaire=questionnaireService.getQuestionnaireByQuestionnaireId(questionnaireId);
        if(questionnaire==null||!user.getDepartmentId().equals(questionnaire.getDepartmentId())){
            return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        }
        return null;
    }
}
