package com.catchiz.enrollctrl.pojo;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireAnswer {
    List<Answer> answerList;
    AnswerAuthor author;
}
