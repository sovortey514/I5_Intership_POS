package com.twd.Pos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.Pos.entity.CategoryFood_Drink;
import com.twd.Pos.entity.SubCategoryFood_Drink;
import com.twd.Pos.repository.CategoryFoodDrinkRepository;
import com.twd.Pos.repository.SubCategoryFoodDrinkRepository;
import com.twd.Pos.service.CategoryFoodDrinkService;

@Service
public class CategoryFoodDrinkServiceImpl implements CategoryFoodDrinkService {

    @Autowired
    private CategoryFoodDrinkRepository categoryFoodDrinkRepository;

    @Autowired
    private SubCategoryFoodDrinkRepository subCategoryFoodDrinkRepository;

    @Override
    public CategoryFood_Drink addSuCategoryFood_Drink(Integer categoryId, List<SubCategoryFood_Drink> subCategories) {
        try {
            Optional<CategoryFood_Drink> categoryOpt = categoryFoodDrinkRepository.findById(categoryId);

            if (categoryOpt.isPresent()) {
                CategoryFood_Drink category = categoryOpt.get();
                for (SubCategoryFood_Drink subCategory : subCategories) {
                    subCategory.setCategory(category);
                }
                subCategoryFoodDrinkRepository.saveAll(subCategories);
                return category;
            } else {
                throw new RuntimeException("Category not found with ID: " + categoryId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding subcategories: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCategoryFood_Drink(Integer id) {
        try {
            if (categoryFoodDrinkRepository.existsById(id)) {
                categoryFoodDrinkRepository.deleteById(id);
            } else {
                throw new RuntimeException("Category not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting category: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CategoryFood_Drink> getAllCategoryFoodDrink() { 
        try {
            return categoryFoodDrinkRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving categories: " + e.getMessage(), e);
        }
    }
    @Override
    public Optional<CategoryFood_Drink> getCategoryById(Integer id) {
        try {
            Optional<CategoryFood_Drink> category = categoryFoodDrinkRepository.findById(id);
            if (category.isPresent()) {
                return category;
            } else {
                throw new RuntimeException("Category not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving category: " + e.getMessage(), e);
        }
    }

    @Override
    public CategoryFood_Drink saveCategoryFood_Drink(CategoryFood_Drink category) {
        try {
            return categoryFoodDrinkRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Error saving category: " + e.getMessage(), e);
        }
    }
    @Override
    public CategoryFood_Drink updatCategoryFood_Drink(Integer id, CategoryFood_Drink upCategoryFood_Drink) {
        try {
            Optional<CategoryFood_Drink> existingCategoryOpt = categoryFoodDrinkRepository.findById(id);

            if (existingCategoryOpt.isPresent()) {
                CategoryFood_Drink existingCategoryFood_Drink = existingCategoryOpt.get();
                existingCategoryFood_Drink.setName(upCategoryFood_Drink.getName());
                existingCategoryFood_Drink.setDescription(upCategoryFood_Drink.getDescription());
                existingCategoryFood_Drink.setUpdate_at(upCategoryFood_Drink.getUpdate_at());

                return categoryFoodDrinkRepository.save(existingCategoryFood_Drink);
            } else {
                throw new RuntimeException("Category not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating category: " + e.getMessage(), e);
        }
    }
}
