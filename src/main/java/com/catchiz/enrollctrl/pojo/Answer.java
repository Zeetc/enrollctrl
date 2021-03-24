package com.catchiz.enrollctrl.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Answer {
    private Integer problemId;
    private List<String> val;
    private String author;
    private String jsonVal;
}
