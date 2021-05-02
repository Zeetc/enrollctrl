package com.catchiz.enrollctrl.mapper;

import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RolePermissionMapper {
    @Select("select permission_id from role_permission where role_id = #{roleId}")
    List<Integer> getPermissionIdsByRoleId(Integer roleId);
}
