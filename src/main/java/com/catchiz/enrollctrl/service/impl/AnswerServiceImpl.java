package com.catchiz.enrollctrl.service.impl;

import com.alibaba.fastjson.JSON;
import com.catchiz.enrollctrl.mapper.AnswerMapper;
import com.catchiz.enrollctrl.pojo.Answer;
import com.catchiz.enrollctrl.pojo.Problem;
import com.catchiz.enrollctrl.service.AnswerService;
import com.catchiz.enrollctrl.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private ProblemService problemService;
    @Override
    public void answerQuestions(List<Answer> answers) {
        String author= UUID.randomUUID().toString();
        for (Answer answer : answers) {
            Problem problem=problemService.getProblemByProblemId(answer.getProblemId());
            if(problem==null)continue;
            answer.setJsonVal(JSON.toJSONString(answer.getVal()));
            answer.setAuthor(author);
            answerMapper.insertAnswer(answer);
        }
    }

    @Override
    public List<Answer> getAnsByProblemId(Integer id) {
        return answerMapper.getAnsByProblemId(id);
    }
}
