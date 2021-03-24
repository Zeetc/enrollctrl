package com.catchiz.enrollctrl.pojo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Questionnaire {
    private Integer id;
    @NotNull(groups = {Questionnaire.class},message = "组ID不能为空")
    private Integer departmentId;
    @NotBlank(groups = {Questionnaire.class},message = "问卷标题不能为空")
    private String title;
    private String describe;
}
