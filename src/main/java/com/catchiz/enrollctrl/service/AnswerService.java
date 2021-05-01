package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.Answer;
import com.catchiz.enrollctrl.pojo.AnswerAuthor;

import java.util.List;

public interface AnswerService {
    void answerQuestions(List<Answer> answers, Integer questionnaireId, AnswerAuthor author);

    List<Answer> getAnsByProblemId(Integer id);

    List<AnswerAuthor> getAllAnswerAuthor(Integer questionnaireId);

    List<Answer> getAnswerByAuthor(Integer answerAuthorId);

    void sendAllUserEmail(Integer questionnaireId, String title,String msg);

    void sendAllUserEmailIsPass(Integer questionnaireId, String title, String msg);

    void deleteQuestionnaire(Integer questionnaireId);

    void deleteAuthor(Integer authorId);
}
