package com.twd.Pos.service;


import java.util.List;
import java.util.Optional;

import com.twd.Pos.entity.Department;


public interface DepartmentService {
    
    Department creaDepartment(Department department);
    void deleteDepartment(Long id);
    List<Department> getAllDepartments();
    Optional<Department> getDepartmentById(Long id);
    Department updateDepartment(Long id, Department department);
}
