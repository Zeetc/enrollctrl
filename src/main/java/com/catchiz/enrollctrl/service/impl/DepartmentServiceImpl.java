package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.mapper.DepartmentMapper;
import com.catchiz.enrollctrl.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Override
    public void changeDepartmentName(Integer departmentId, String name) {
        departmentMapper.changeDepartmentName(departmentId,name);
    }

    @Override
    public void changeDepartmentDescribe(Integer departmentId, String describe) {
        departmentMapper.changeDepartmentDescribe(departmentId,describe);
    }
}
