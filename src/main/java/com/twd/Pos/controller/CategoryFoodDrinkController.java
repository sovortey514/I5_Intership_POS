package com.twd.Pos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twd.Pos.entity.CategoryFood_Drink;
import com.twd.Pos.entity.SubCategoryFood_Drink;
import com.twd.Pos.service.CategoryFoodDrinkService;

@RestController
@RequestMapping("/admin")
public class CategoryFoodDrinkController {

    @Autowired
    private CategoryFoodDrinkService categoryFoodDrinkService;

    @GetMapping("/getAllCategoryFood_Drink")
    public List<CategoryFood_Drink> getAllCategories() {
        return categoryFoodDrinkService.getAllCategoryFoodDrink();
    }

    @GetMapping("/getAllCategoryFood_DrinkById/{id}")
    public Optional<CategoryFood_Drink> getCategoryById(@PathVariable Long id) {
        return categoryFoodDrinkService.getCategoryById(id);
    }

    @PostMapping("/CreateCategoryFoodDrink")
    public CategoryFood_Drink createCategory(@RequestBody CategoryFood_Drink category) {
        return categoryFoodDrinkService.saveCategoryFood_Drink(category);
    }

    @PutMapping("/UpdateCategoryFoodDrink/{id}")
    public CategoryFood_Drink updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryFood_Drink updatedCategory) {
        return categoryFoodDrinkService.updatCategoryFood_Drink(id, updatedCategory);
    }

    @DeleteMapping("/deleteCagoryFoodDrink/{id}")
    public String deleteCategory(@PathVariable Long id) {
    categoryFoodDrinkService.deleteCategoryFood_Drink(id);
    return "Category with ID " + id + " deleted successfully.";
    }
    @PostMapping("/CreateCategoryFoodDrink/{id}/subcategories")
    public CategoryFood_Drink addSubCategoriesToCategory(
            @PathVariable Long id,
            @RequestBody List<SubCategoryFood_Drink> subCategories) {
        return categoryFoodDrinkService.addSuCategoryFood_Drink(id, subCategories);
    }

}
