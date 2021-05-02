package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.PermissionEntity;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

@Mapper
public interface PermissionMapper {
    @Select("select * from permission where permission_id = #{permissionId}")
    PermissionEntity getPermissionById(Integer permissionId);
}
