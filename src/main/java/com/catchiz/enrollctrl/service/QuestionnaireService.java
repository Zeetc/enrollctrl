package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.Questionnaire;

import java.sql.Timestamp;

public interface QuestionnaireService {
    void insertQuestionnaire(Questionnaire questionnaire);

    Questionnaire getQuestionnaireByQuestionnaireId(Integer questionnaireId);

    void deleteQuestionnaire(Integer questionnaireId);

    void changeEndDate(Integer questionnaireId, Timestamp endDate);
}
