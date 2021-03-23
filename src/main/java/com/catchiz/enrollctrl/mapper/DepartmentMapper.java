package com.catchiz.enrollctrl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DepartmentMapper {
    @Update("update from department set name = #{name} where id = #{departmentId}")
    void changeDepartmentName(@Param("departmentId") int departmentId, @Param("name") String name);

    @Update("update from department set describe = #{describe} where id = #{departmentId}")
    void changeDepartmentDescribe(@Param("departmentId") Integer departmentId, @Param("describe") String describe);
}
