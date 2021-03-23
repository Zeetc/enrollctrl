package com.catchiz.enrollctrl.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {

    @ApiModelProperty("状态码")
    int code;
    @ApiModelProperty("执行信息")
    private String message;
    @ApiModelProperty("返回结果，List类型")
    private Object data;

    public CommonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
