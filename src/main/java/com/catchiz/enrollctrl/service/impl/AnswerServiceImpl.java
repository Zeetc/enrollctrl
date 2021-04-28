package com.catchiz.enrollctrl.service.impl;

import com.alibaba.fastjson.JSON;
import com.catchiz.enrollctrl.mapper.AnswerMapper;
import com.catchiz.enrollctrl.pojo.*;
import com.catchiz.enrollctrl.service.AnswerAuthorService;
import com.catchiz.enrollctrl.service.AnswerService;
import com.catchiz.enrollctrl.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
@Transactional
public class AnswerServiceImpl implements AnswerService {
    private final AnswerMapper answerMapper;
    private final ProblemService problemService;
    private final AnswerAuthorService answerAuthorService;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailSendUser;

    public AnswerServiceImpl(AnswerMapper answerMapper, ProblemService problemService, AnswerAuthorService answerAuthorService, JavaMailSender mailSender, ThreadPoolExecutor executor) {
        this.answerMapper = answerMapper;
        this.problemService = problemService;
        this.answerAuthorService = answerAuthorService;
        this.mailSender = mailSender;
        this.executor = executor;
    }

    @Override
    public void answerQuestions(List<Answer> answers, Integer questionnaireId, AnswerAuthor author) {
        answerAuthorService.saveAuthor(author);
        for (Answer answer : answers) {
            Problem problem=problemService.getProblemByProblemId(answer.getProblemId());
            if(problem==null)continue;
            if(!problem.getQuestionnaireId().equals(questionnaireId))continue;
            answer.setProblemId(null);
            answer.setJsonVal(JSON.toJSONString(answer.getVal()));
            answer.setAuthorId(author.getAuthorId());
            answerMapper.insertAnswer(answer);
        }
    }

    @Override
    public List<Answer> getAnsByProblemId(Integer id) {
        return answerMapper.getAnsByProblemId(id);
    }

    @Override
    public List<AnswerAuthor> getAllAnswerAuthor(Integer questionnaireId) {
        List<Integer> list = answerMapper.getAllAnswerAuthor(questionnaireId);
        List<AnswerAuthor> answerAuthors = new ArrayList<>();
        for (Integer id : list) {
            AnswerAuthor answerAuthor = answerAuthorService.getAuthorById(id);
            answerAuthors.add(answerAuthor);
        }
        return answerAuthors;
    }

    @Override
    public List<Answer> getAnswerByAuthor(Integer answerAuthorId) {
        List<Answer> answers = answerMapper.getAnswerByAuthor(answerAuthorId);
        for (Answer answer : answers) {
            answer.setVal(JSON.parseArray(answer.getJsonVal(),String.class));
        }
        return answers;
    }
    private final ThreadPoolExecutor executor;

    @Override
    public void sendAllUserEmail(Integer questionnaireId,String title, String msg) {
        List<AnswerAuthor> answerAuthors = answerAuthorService.getAllAnswerAuthor(questionnaireId);
        for (AnswerAuthor answerAuthor : answerAuthors) {
            CompletableFuture.runAsync(()->{
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(emailSendUser);
                message.setTo(answerAuthor.getAuthorEmail());
                message.setSubject(title);
                message.setText(msg);
                try {
                    mailSender.send(message);
                } catch (Exception e) {
                    log.error("尝试发送消息失败");
                }
            },executor);
        }
    }

    @Override
    public void sendAllUserEmailIsPass(Integer questionnaireId, String title, String msg) {
        List<AnswerAuthor> answerAuthors = answerAuthorService.getAllUserIsPass(questionnaireId);
        for (AnswerAuthor answerAuthor : answerAuthors) {
            CompletableFuture.runAsync(()->{
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(emailSendUser);
                message.setTo(answerAuthor.getAuthorEmail());
                message.setSubject(title);
                message.setText(msg);
                try {
                    mailSender.send(message);
                } catch (Exception e) {
                    log.error("尝试发送消息失败");
                }
            },executor);
        }
    }
}
