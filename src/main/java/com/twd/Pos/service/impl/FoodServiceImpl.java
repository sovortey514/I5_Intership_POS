package com.twd.Pos.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.twd.Pos.entity.Food;
import com.twd.Pos.service.FoodService;

@Service
public class FoodServiceImpl implements FoodService{

    @Override
    public void deleteFood(Long foodId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Food> getAllFoods() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Food> getFoodById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<Food> getFoodsByCategory(Long categoryId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Food> getFoodsBySubCategory(Long subCategoryId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Food> searchFoodsByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Food updateFood(Long foodId, String name, String description, BigDecimal price, Long categoryId, Long sizeId,
            Long subCategoryId, MultipartFile image) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
