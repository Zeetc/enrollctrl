package com.catchiz.enrollctrl.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Problem {
    private Integer id;
    private String title;
    private String type;
    private List<String> val;
    private Integer index;
    private Integer questionnaireId;
    private String jsonVal;
}
