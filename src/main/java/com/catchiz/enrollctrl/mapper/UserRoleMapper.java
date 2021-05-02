package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.RoleEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    @Insert("insert into user_role values(#{username},#{roleId})")
    void insertUserRole(@Param("username") String username, @Param("roleId") Integer roleId);

    @Select("select role_id from user_role where username = #{username} ")
    List<Integer> getRoles(String username);
}
