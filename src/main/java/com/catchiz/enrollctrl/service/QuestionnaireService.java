package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.Problem;
import com.catchiz.enrollctrl.pojo.Questionnaire;

import java.sql.Timestamp;
import java.util.List;

public interface QuestionnaireService {
    void insertQuestionnaire(Questionnaire questionnaire, List<Problem> problems);

    Questionnaire getQuestionnaireByQuestionnaireId(Integer questionnaireId);

    void deleteQuestionnaire(Integer questionnaireId);

    void changeEndDate(Integer questionnaireId, Timestamp endDate);
}
