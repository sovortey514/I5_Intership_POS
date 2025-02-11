package com.twd.Pos.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import com.twd.Pos.entity.Food;

public interface FoodService {
    
    List<Food> getAllFoods();

    Optional<Food> getFoodById(Long id);

    Food updateFood(Long foodId, String name, String description, BigDecimal price, 
                    Long categoryId, Long sizeId, Long subCategoryId, MultipartFile image) throws IOException;

                    void deleteFood(Long foodId);

                    List<Food> getFoodsByCategory(Long categoryId);
                    List<Food> getFoodsBySubCategory(Long subCategoryId);
                    List<Food> searchFoodsByName(String name);
    
}
