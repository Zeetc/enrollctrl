package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    @Update("update department set name = #{name} where id = #{departmentId}")
    void changeDepartmentName(@Param("departmentId") int departmentId, @Param("name") String name);

    @Update("update department set describe = #{describe} where id = #{departmentId}")
    void changeDepartmentDescribe(@Param("departmentId") Integer departmentId, @Param("describe") String describe);

    @Select("select * from department")
    List<Department> listAllDepartment();
}
