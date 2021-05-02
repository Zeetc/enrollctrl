package com.catchiz.enrollctrl.pojo;

import com.catchiz.enrollctrl.valid.RegisterGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(groups = {RegisterGroup.class},message = "用户名不能为空")
    private String username;

    @NotBlank(groups = {RegisterGroup.class},message = "密码不能为空")
    private String password;

    @Email(groups = {RegisterGroup.class},message = "邮箱不合法")
    private String email;

    @Pattern(regexp = "^[0|1]$",groups = {RegisterGroup.class},message = "性别输入不合法")
    private Integer gender;

    private Integer departmentId;
    private Timestamp registerDate;
    private String describe;

    private List<PermissionEntity> permissionEntities;
}
