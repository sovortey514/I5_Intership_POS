package com.twd.Pos.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.twd.Pos.entity.Food;

public interface FoodService {
    
    List<Food> getAllFoods();

    Optional<Food> getFoodById(Long id);

    Food createFood(String name, String description, BigDecimal price, Long categoryId, Long subCategoryId, Long sizeId);
    Food updateFood(Long foodId, String name, String description, BigDecimal price, Long categoryId, Long subCategoryId, Long sizeId);

    void deleteFood(Long foodId);
    
}
