package com.catchiz.enrollctrl.service.impl;

import com.alibaba.fastjson.JSON;
import com.catchiz.enrollctrl.mapper.ProblemMapper;
import com.catchiz.enrollctrl.mapper.QuestionnaireMapper;
import com.catchiz.enrollctrl.pojo.Problem;
import com.catchiz.enrollctrl.pojo.ProblemType;
import com.catchiz.enrollctrl.pojo.Questionnaire;
import com.catchiz.enrollctrl.service.AnswerAuthorService;
import com.catchiz.enrollctrl.service.AnswerService;
import com.catchiz.enrollctrl.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final QuestionnaireMapper questionnaireMapper;
    private final ProblemMapper problemMapper;
    @Autowired
    private AnswerAuthorService answerAuthorService;
    @Autowired
    private AnswerService answerService;


    public QuestionnaireServiceImpl(QuestionnaireMapper questionnaireMapper, ProblemMapper problemMapper) {
        this.questionnaireMapper = questionnaireMapper;
        this.problemMapper = problemMapper;
    }

    @Override
    public void insertQuestionnaire(Questionnaire questionnaire, List<Problem> problems) {
        questionnaireMapper.insertQuestionnaire(questionnaire);
        int index=1;
        for (Problem problem : problems) {
            if(!ProblemType.typeSet.contains(problem.getType()))continue;
            problem.setId(null);
            problem.setIndex(index++);
            problem.setQuestionnaireId(questionnaire.getId());
            String jsonVal = JSON.toJSONString(problem.getVal());
            problem.setJsonVal(jsonVal);
            problemMapper.insertProblem(problem);
        }
    }

    @Override
    public Questionnaire getQuestionnaireByQuestionnaireId(Integer questionnaireId) {
        return questionnaireMapper.getQuestionnaireByQuestionnaireId(questionnaireId);
    }

    @Transactional
    @Override
    public void deleteQuestionnaire(Integer questionnaireId) {
        questionnaireMapper.deleteQuestionnaire(questionnaireId);
        answerAuthorService.deleteQuestionnaire(questionnaireId);
        answerService.deleteQuestionnaire(questionnaireId);
    }
}
