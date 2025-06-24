package com.twd.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.Bakong;

@Repository
public interface BakongRepository extends JpaRepository<Bakong, Long> {
   
}