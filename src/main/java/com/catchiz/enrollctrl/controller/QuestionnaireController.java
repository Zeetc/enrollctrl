package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.*;
import com.catchiz.enrollctrl.service.AnswerService;
import com.catchiz.enrollctrl.service.ProblemService;
import com.catchiz.enrollctrl.service.QuestionnaireService;
import com.catchiz.enrollctrl.service.UserService;
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

    public QuestionnaireController(QuestionnaireService questionnaireService, UserService userService, ProblemService problemService, AnswerService answerService) {
        this.questionnaireService = questionnaireService;
        this.userService = userService;
        this.problemService = problemService;
        this.answerService = answerService;
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
        if(questionnaireId==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user=userService.getUserByUsername(username);
        Questionnaire questionnaire=questionnaireService.getQuestionnaireByQuestionnaireId(questionnaireId);
        if(questionnaire==null||!user.getDepartmentId().equals(questionnaire.getDepartmentId())){
            return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        }
        return ManagerController.getQuestionnaireAns(questionnaireId, problemService, answerService);
    }
}
