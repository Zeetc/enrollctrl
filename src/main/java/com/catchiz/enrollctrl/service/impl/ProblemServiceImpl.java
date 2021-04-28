package com.catchiz.enrollctrl.service.impl;

import com.alibaba.fastjson.JSON;
import com.catchiz.enrollctrl.mapper.ProblemMapper;
import com.catchiz.enrollctrl.pojo.Problem;
import com.catchiz.enrollctrl.service.ProblemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProblemServiceImpl implements ProblemService {
    private final ProblemMapper problemMapper;

    public ProblemServiceImpl(ProblemMapper problemMapper) {
        this.problemMapper = problemMapper;
    }

    @Override
    public List<Problem> listProblemsByQuestionnaireId(Integer questionnaireId) {
        List<Problem> problems=problemMapper.listProblemsByQuestionnaireId(questionnaireId);
        for (Problem problem : problems) {
            problem.setVal(JSON.parseArray(problem.getJsonVal(),String.class));
        }
        return problems;
    }

    @Override
    public Problem getProblemByProblemId(Integer problemId) {
        return problemMapper.getProblemByProblemId(problemId);
    }

    @Override
    public List<Problem> getProblemIdsByQuestionnaireId(Integer questionnaireId) {
        return problemMapper.getProblemsByQuestionnaireId(questionnaireId);
    }

    @Override
    public void changeProblem(Problem p) {
        p.setJsonVal(JSON.toJSONString(p.getVal()));
        problemMapper.changeProblem(p);
    }
}
