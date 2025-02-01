package com.twd.Pos.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.twd.Pos.entity.CategoryFood_Drink;

@Repository
public interface CategoryFoodDrinkRepository extends JpaRepository<CategoryFood_Drink, Integer> {
}