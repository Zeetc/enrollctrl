package com.catchiz.enrollctrl.service;

public interface DepartmentService {
    void changeDepartmentName(Integer departmentId, String name);

    void changeDepartmentDescribe(Integer departmentId, String describe);
}
