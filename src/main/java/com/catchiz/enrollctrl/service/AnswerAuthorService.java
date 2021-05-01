package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.AnswerAuthor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnswerAuthorService {
    void saveAuthor(AnswerAuthor author);

    AnswerAuthor getAuthorById(Integer id);

    void updateAnswerAuthorName(Integer authorId,String authorName);

    void updateAnswerAuthorEmail(Integer authorId, String email);

    void setIsPass( Integer authorId,Integer isPass);

    List<AnswerAuthor> getAllUserIsPass(Integer questionnaireId);

    List<AnswerAuthor> getAllAnswerAuthor(Integer questionnaireId);

    void deleteQuestionnaire(Integer questionnaireId);
}
