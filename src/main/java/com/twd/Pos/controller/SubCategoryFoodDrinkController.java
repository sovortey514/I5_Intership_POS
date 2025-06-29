package com.twd.Pos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twd.Pos.entity.SubCategoryFood_Drink;
import com.twd.Pos.service.SubCategoryFoodDrinkService;

@RestController
@RequestMapping("/auth")
public class SubCategoryFoodDrinkController {

    @Autowired
    private SubCategoryFoodDrinkService subCategoryFoodDrinkService;

    @GetMapping("/getAllSubCategoryFoodDrinks")
    public List<SubCategoryFood_Drink> getAllSubCategories() {
        return subCategoryFoodDrinkService.getAllSubCategories();
    }

    @GetMapping("/getSubCategoryFoodDrinks/{id}")
    public Optional<SubCategoryFood_Drink> getSubCategoryById(@PathVariable Long id) {
        return subCategoryFoodDrinkService.getSubCategoryById(id);
    }

    @PostMapping("/createSubCategoryFoodDrink")
    public SubCategoryFood_Drink addSubCategoryToCategory(@RequestBody SubCategoryFood_Drink subCategory) {
        return subCategoryFoodDrinkService.saveSubCategoryFood_Drink(subCategory);
    }

    @PostMapping("/subcategory/{categoryId}/add")
    public List<SubCategoryFood_Drink> addSubCategoriesToCategory(
            @PathVariable Long categoryId,
            @RequestBody List<SubCategoryFood_Drink> subCategories) {
        return subCategories.stream()
                .map(subCategory -> subCategoryFoodDrinkService.addSubCategoryToCategory(categoryId, subCategory))
                .toList();
    }

    @PutMapping("/updateSubcategoryFoodDrink/{id}")
    public SubCategoryFood_Drink updateSubCategory(
            @PathVariable Long id,
            @RequestBody SubCategoryFood_Drink updatedSubCategory) {
        return subCategoryFoodDrinkService.updateSubCategory(id, updatedSubCategory);
    }

    @DeleteMapping("/deleteSubcategoryFoodDrink/{id}")
    public String deleteSubCategory(@PathVariable Long id) {
        subCategoryFoodDrinkService.deleteSubCategoryFoodDrink(id);
        return "Subcategory with ID " + id + " deleted successfully.";
    }

    @GetMapping("/subcategories")
    public List<SubCategoryFood_Drink> getAllSubCategoriesWithCategory() {
        return subCategoryFoodDrinkService.getAllSubCategoriesWithCategory();
    }

}
