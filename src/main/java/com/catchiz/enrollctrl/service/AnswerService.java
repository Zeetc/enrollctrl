package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.Answer;

import java.util.List;

public interface AnswerService {
    void answerQuestions(List<Answer> answers, Integer questionnaireId);

    List<Answer> getAnsByProblemId(Integer id);
}
