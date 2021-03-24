package com.catchiz.enrollctrl.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("问卷结果类")
public class Answer {

    @ApiModelProperty("无需传参")
    private Integer problemId;
    @ApiModelProperty("list类型的值，如果是多选的话需要传所有可选值")
    private List<String> val;
    @ApiModelProperty("无需传参")
    private String author;
    @ApiModelProperty("无需传参")
    private String jsonVal;
}
