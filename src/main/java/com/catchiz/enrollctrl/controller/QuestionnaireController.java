package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.*;
import com.catchiz.enrollctrl.service.AnswerService;
import com.catchiz.enrollctrl.service.ProblemService;
import com.catchiz.enrollctrl.service.QuestionnaireService;
import com.catchiz.enrollctrl.service.UserService;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class QuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private UserService userService;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private AnswerService answerService;

    @PostMapping("/insertQuestionnaire")
    public CommonResult insertQuestionnaire(Questionnaire questionnaire,
                                            List<Problem> problems,
                                            @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        Integer departmentId= userService.getDepartmentIdByUsername(username);
        if(departmentId==null)return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        questionnaireService.insertQuestionnaire(questionnaire,problems);
        return new CommonResult(CommonStatus.CREATE,"创建成功",questionnaire.getId());
    }

    @GetMapping("/getAnswerByQuestionnaireId")
    public CommonResult getAnswerByQuestionnaireId(Integer questionnaireId,
                                                   @RequestHeader String Authorization){
        if(questionnaireId==null)return new CommonResult(CommonStatus.FORBIDDEN,"非法参数");
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        User user=userService.getUserByUsername(username);
        Questionnaire questionnaire=questionnaireService.getQuestionnaireByQuestionnaireId(questionnaireId);
        if(questionnaire==null||!user.getDepartmentId().equals(questionnaire.getDepartmentId())){
            return new CommonResult(CommonStatus.FORBIDDEN,"无权限");
        }
        List<Problem> problems=problemService.getProblemIdsByQuestionnaireId(questionnaireId);
        problems.sort(Comparator.comparingInt(Problem::getIndex));
        List<List<Answer>> lists=new ArrayList<>();
        for (Problem problem : problems) {
            lists.add(answerService.getAnsByProblemId(problem.getId()));
        }
        return new CommonResult(CommonStatus.OK,"查询成功",lists);
    }
}
