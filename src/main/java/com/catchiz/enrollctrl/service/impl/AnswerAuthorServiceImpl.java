package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.mapper.AnswerAuthorMapper;
import com.catchiz.enrollctrl.mapper.AnswerMapper;
import com.catchiz.enrollctrl.pojo.AnswerAuthor;
import com.catchiz.enrollctrl.service.AnswerAuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerAuthorServiceImpl implements AnswerAuthorService {
    private final AnswerAuthorMapper answerAuthorMapper;

    private final AnswerMapper answerMapper;

    public AnswerAuthorServiceImpl(AnswerAuthorMapper answerAuthorMapper, AnswerMapper answerMapper) {
        this.answerAuthorMapper = answerAuthorMapper;
        this.answerMapper = answerMapper;
    }

    @Override
    public void saveAuthor(AnswerAuthor author) {
        author.setAuthorId(null);
        answerAuthorMapper.saveAuthor(author);
    }

    @Override
    public AnswerAuthor getAuthorById(Integer id) {
        return answerAuthorMapper.getAuthorById(id);
    }

    @Override
    public void updateAnswerAuthorName(Integer authorId,String authorName) {
        answerAuthorMapper.updateAnswerAuthorName(authorId,authorName);
    }

    @Override
    public void updateAnswerAuthorEmail(Integer authorId, String email) {
        answerAuthorMapper.updateAnswerAuthorEmail(authorId,email);
    }

    @Override
    public void setIsPass(Integer authorId, Integer isPass) {
        answerAuthorMapper.setIsPass(authorId,isPass);
    }

    @Override
    public List<AnswerAuthor> getAllUserIsPass(Integer questionnaireId) {
        return answerAuthorMapper.getAllUserIsPass(questionnaireId);
    }

    @Override
    public List<AnswerAuthor> getAllAnswerAuthor(Integer questionnaireId) {
        return answerAuthorMapper.getAllUserByQuestionnaireId(questionnaireId);
    }

    @Override
    public void deleteQuestionnaire(Integer questionnaireId) {
        answerAuthorMapper.deleteQuestionnaire(questionnaireId);
    }

    @Override
    @Transactional
    public void deleteAuthor(Integer authorId) {
        answerAuthorMapper.deleteAuthor(authorId);
        answerMapper.deleteAuthor(authorId);
    }
}
