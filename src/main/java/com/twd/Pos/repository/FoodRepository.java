package com.twd.Pos.repository;

import com.twd.Pos.entity.Food;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    
    @Query("SELECT f FROM Food f LEFT JOIN FETCH f.files WHERE f.id = :foodId")
    Optional<Food> findByIdWithFiles(@Param("foodId") Long foodId);
    
}
