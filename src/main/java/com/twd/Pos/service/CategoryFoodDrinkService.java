package com.twd.Pos.service;

import java.util.List;
import java.util.Optional;

import com.twd.Pos.entity.CategoryFood_Drink;
import com.twd.Pos.entity.SubCategoryFood_Drink;

public interface CategoryFoodDrinkService {
    List<CategoryFood_Drink> getAllCategoryFoodDrink();
    Optional<CategoryFood_Drink> getCategoryById(Long id);
    CategoryFood_Drink saveCategoryFood_Drink(CategoryFood_Drink category);
    void deleteCategoryFood_Drink(Long id);
    CategoryFood_Drink addSuCategoryFood_Drink(Long categoryId, List<SubCategoryFood_Drink> subCategoryFood_Drinks);
    CategoryFood_Drink updatCategoryFood_Drink(Long id, CategoryFood_Drink upCategoryFood_Drink);
    CategoryFood_Drink updateCategoryStatus(Long id, Integer status);
}
