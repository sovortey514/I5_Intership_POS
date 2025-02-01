package com.twd.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.SubCategoryFood_Drink;

@Repository
public interface SubCategoryFoodDrinkRepository extends JpaRepository<SubCategoryFood_Drink, Long>{
    
}
