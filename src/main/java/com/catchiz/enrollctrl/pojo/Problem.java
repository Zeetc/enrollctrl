package com.catchiz.enrollctrl.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("问卷问题类")
public class Problem {
    private Integer id;
    @ApiModelProperty("题目标题")
    private String title;
    @ApiModelProperty("题目类型")
    private String type;
    @ApiModelProperty("题目可选值list")
    private List<String> val;
    @ApiModelProperty("题目索引，代表是问卷的第几题")
    private Integer idx;
    @ApiModelProperty("所属问卷id")
    private Integer questionnaireId;
    @ApiModelProperty("这个不需要传参")
    private String jsonVal;
}
