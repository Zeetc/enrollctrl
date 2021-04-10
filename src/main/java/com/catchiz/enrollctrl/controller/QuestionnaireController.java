package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.*;
import com.catchiz.enrollctrl.service.*;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    private final UserService userService;
    private final ProblemService problemService;
    private final AnswerService answerService;

    private final AnswerAuthorService answerAuthorService;

    public QuestionnaireController(QuestionnaireService questionnaireService, UserService userService, ProblemService problemService, AnswerService answerService, AnswerAuthorService answerAuthorService) {
        this.questionnaireService = questionnaireService;
        this.userService = userService;
        this.problemService = problemService;
        this.answerService = answerService;
        this.answerAuthorService = answerAuthorService;
    }

    @PostMapping("/insertQuestionnaire")
    @ApiOperation("创建问卷并提交")
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
    public CommonResult getAnswerByQuestionnaireId(Integer questionnaireId,
                                                   @RequestHeader String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        return ManagerController.getQuestionnaireAns(questionnaireId, problemService, answerService);
    }

    @GetMapping("/getAllAnswerAuthor")
    @ApiOperation(("获取回答某个问卷的所有用户以及他们的用户信息"))
    public CommonResult getAllAnswerAuthor(Integer questionnaireId,
                                           @RequestHeader String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        List<AnswerAuthor> answerAuthors = answerService.getAllAnswerAuthor(questionnaireId);
        return new CommonResult(CommonStatus.OK,"获取成功",answerAuthors);
    }

    @GetMapping("/getAnswerByAuthor")
    @ApiOperation(("获取某个回答者的回答内容"))
    public CommonResult getAnswerByAuthor(Integer questionnaireId,Integer answerAuthorId,
                                          @RequestHeader String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        List<Answer> answers = answerService.getAnswerByAuthor(answerAuthorId);
        return new CommonResult(CommonStatus.OK,"获取成功",answers);
    }

    @GetMapping("/getSingleQuestion")
    @ApiOperation("获取单个问题，根据Id")
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

    @GetMapping("/sendAllUserEmail")
    @ApiOperation("群发消息，根据问卷Id")
    public CommonResult sendAllUserEmail(Integer questionnaireId,
                                         String title,
                                         String msg,
                                         @RequestHeader String Authorization){
        CommonResult FORBIDDEN = checkPermission(questionnaireId, Authorization);
        if (FORBIDDEN != null) return FORBIDDEN;
        answerService.sendAllUserEmail(questionnaireId,title,msg);
        return new CommonResult(CommonStatus.OK,"发送成功");
    }

    @GetMapping("/updateAnswerAuthorName")
    @ApiOperation("修改报名信息--名字")
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

    @GetMapping("/updateAnswerAuthorEmail")
    @ApiOperation("修改报名信息--邮箱")
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
