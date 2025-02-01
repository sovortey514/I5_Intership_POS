package com.twd.Pos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.Pos.entity.CategoryFood_Drink;
import com.twd.Pos.entity.SubCategoryFood_Drink;
import com.twd.Pos.repository.CategoryFoodDrinkRepository;
import com.twd.Pos.repository.SubCategoryFoodDrinkRepository;
import com.twd.Pos.service.SubCategoryFoodDrinkService;

@Service
public class SubCategoryFoodDrinkServiceImpl implements SubCategoryFoodDrinkService{

    @Autowired
    private SubCategoryFoodDrinkRepository subCategoryFoodDrinkRepository;

    @Autowired
    private CategoryFoodDrinkRepository categoryFoodDrinkRepository;

    @Override
    public SubCategoryFood_Drink addSubCategoryToCategory(Integer categoryId, SubCategoryFood_Drink subCategory) {
       try {
        Optional<CategoryFood_Drink> categoryFood_Drinkopt = categoryFoodDrinkRepository.findById(categoryId);
        if(categoryFood_Drinkopt.isPresent()){
            CategoryFood_Drink categoryFood_Drink = categoryFood_Drinkopt.get();
            subCategory.setCategory(categoryFood_Drink);
            return subCategoryFoodDrinkRepository.save(subCategory);
        }else{
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }
       } catch (Exception e) {
        throw new RuntimeException("Error adding subcategory to category: " + e.getMessage(), e);
       }
 
    }

    @Override
    public void deleteSubCategoryFoodDrink(Long id) {
        try {
            if (subCategoryFoodDrinkRepository.existsById(id)) {
                subCategoryFoodDrinkRepository.deleteById(id);
            } else {
                throw new RuntimeException("SubCategoryfooddrink not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting SubCategoryfooddrink: " + e.getMessage(), e);
        }
        
    }

    @Override
    public List<SubCategoryFood_Drink> getAllSubCategories() {
        try {
            return subCategoryFoodDrinkRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving subcategories: " + e.getMessage(), e);
        }
    }
    @Override
    public Optional<SubCategoryFood_Drink> getSubCategoryById(Long id) {
        try {
            Optional<SubCategoryFood_Drink> subCategory = subCategoryFoodDrinkRepository.findById(id);
            if (subCategory.isPresent()) {
                return subCategory;
            } else {
                throw new RuntimeException("Subcategory not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving subcategory: " + e.getMessage(), e);
        }
    }

    @Override
    public SubCategoryFood_Drink saveSubCategoryFood_Drink(SubCategoryFood_Drink subCategory) {
        try {
            return subCategoryFoodDrinkRepository.save(subCategory);
        } catch (Exception e) {
            throw new RuntimeException("Error saving subcategory: " + e.getMessage(), e);
        }
    }

    @Override
    public SubCategoryFood_Drink updateSubCategory(Long id, SubCategoryFood_Drink updatedSubCategory) {
        try {
            Optional<SubCategoryFood_Drink> existingSubCategoryOpt = subCategoryFoodDrinkRepository.findById(id);

            if (existingSubCategoryOpt.isPresent()) {
                SubCategoryFood_Drink existingSubCategory = existingSubCategoryOpt.get();
                existingSubCategory.setName(updatedSubCategory.getName());
                existingSubCategory.setDescription(updatedSubCategory.getDescription());
                existingSubCategory.setUpdate_at(updatedSubCategory.getUpdate_at());

                return subCategoryFoodDrinkRepository.save(existingSubCategory);
            } else {
                throw new RuntimeException("Subcategory not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating subcategory: " + e.getMessage(), e);
        }
    }
    
}
