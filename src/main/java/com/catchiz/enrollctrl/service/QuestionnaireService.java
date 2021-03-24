package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.Problem;
import com.catchiz.enrollctrl.pojo.Questionnaire;

import java.util.List;

public interface QuestionnaireService {
    void insertQuestionnaire(Questionnaire questionnaire, List<Problem> problems);

    Questionnaire getQuestionnaireByQuestionnaireId(Integer questionnaireId);
}
