package com.twd.Pos.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.Department;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

//     @Query("SELECT fa FROM department fa JOIN FETCH fa.building")
        Optional<Department> findByName(String name);
 }