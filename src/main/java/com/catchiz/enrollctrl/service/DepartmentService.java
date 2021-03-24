package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.Department;

import java.util.List;

public interface DepartmentService {
    void changeDepartmentName(Integer departmentId, String name);

    void changeDepartmentDescribe(Integer departmentId, String describe);

    List<Department> listAllDepartment();

    void addDepartment(Department department);
}
