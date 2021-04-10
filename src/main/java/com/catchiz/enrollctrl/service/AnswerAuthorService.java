package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.AnswerAuthor;

public interface AnswerAuthorService {
    void saveAuthor(AnswerAuthor author);

    AnswerAuthor getAuthorById(Integer id);

    void updateAnswerAuthorName(Integer authorId,String authorName);

    void updateAnswerAuthorEmail(Integer authorId, String email);
}
