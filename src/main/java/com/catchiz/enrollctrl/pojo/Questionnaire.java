package com.catchiz.enrollctrl.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@ApiModel("问卷类")
public class Questionnaire {
    private Integer id;
    @NotNull(groups = {Questionnaire.class},message = "组ID不能为空")
    @ApiModelProperty("问卷所属部门id")
    private Integer departmentId;
    @NotBlank(groups = {Questionnaire.class},message = "问卷标题不能为空")
    @ApiModelProperty("问卷标题")
    private String title;
    @ApiModelProperty("问卷描述")
    private String describe;
    @ApiModelProperty("问卷结束时间")
    private Timestamp endDate;
}
