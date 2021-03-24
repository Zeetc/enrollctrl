package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.Problem;

import java.util.List;

public interface ProblemService {
    List<Problem> listProblemsByQuestionnaireId(Integer questionnaireId);

    Problem getProblemByProblemId(Integer problemId);

    List<Problem> getProblemIdsByQuestionnaireId(Integer questionnaireId);

    void changeProblem(Problem p);
}
