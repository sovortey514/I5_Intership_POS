package com.twd.Pos.service;

import java.util.List;
import java.util.Optional;

import com.twd.Pos.entity.SubCategoryFood_Drink;

public interface SubCategoryFoodDrinkService {
    
    List<SubCategoryFood_Drink> getAllSubCategories();
    Optional<SubCategoryFood_Drink> getSubCategoryById(Long id);
    SubCategoryFood_Drink saveSubCategoryFood_Drink(SubCategoryFood_Drink subCategory);
    SubCategoryFood_Drink addSubCategoryToCategory(Long categoryId, SubCategoryFood_Drink subCategory);
    SubCategoryFood_Drink updateSubCategory(Long id, SubCategoryFood_Drink updatedSubCategory);
    void deleteSubCategoryFoodDrink (Long id);
}
