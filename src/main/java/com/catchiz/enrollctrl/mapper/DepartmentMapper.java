package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    @Update("update department set department_name = #{name} where id = #{departmentId}")
    void changeDepartmentName(@Param("departmentId") int departmentId, @Param("name") String name);

    @Update("update department set describes = #{describes} where id = #{departmentId}")
    void changeDepartmentDescribe(@Param("departmentId") Integer departmentId, @Param("describes") String describes);

    @Select("select * from department")
    List<Department> listAllDepartment();

    @Insert("insert into department values(#{id},#{departmentName},#{describes})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    void addDepartment(Department department);
}
