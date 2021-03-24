package com.catchiz.enrollctrl.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("部门类")
public class Department {
    @ApiModelProperty("无需传参")
    private Integer id;
    @ApiModelProperty("部门名字")
    private String name;
    @ApiModelProperty("部门描述")
    private String describe;

}
