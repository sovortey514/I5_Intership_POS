package com.twd.Pos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.Tables;

@Repository
public interface TableRepository extends JpaRepository<Tables, Long>{
    List<Tables> findByStatus(String status);
    List<Tables> findByType(String type);
}
