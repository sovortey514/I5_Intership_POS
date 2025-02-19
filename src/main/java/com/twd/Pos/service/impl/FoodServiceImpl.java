package com.twd.Pos.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.twd.Pos.entity.CategoryFood_Drink;
import com.twd.Pos.entity.Food;
import com.twd.Pos.entity.Size;
import com.twd.Pos.entity.SubCategoryFood_Drink;
import com.twd.Pos.repository.CategoryFoodDrinkRepository;
import com.twd.Pos.repository.FoodRepository;
import com.twd.Pos.repository.SizeRepository;
import com.twd.Pos.repository.SubCategoryFoodDrinkRepository;
import com.twd.Pos.service.FoodService;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final CategoryFoodDrinkRepository categoryFoodDrinkRepository;
    private final SubCategoryFoodDrinkRepository subCategoryFoodDrinkRepository;
    private final SizeRepository sizeRepository;

    public FoodServiceImpl(FoodRepository foodRepository,
            CategoryFoodDrinkRepository categoryFoodDrinkRepository,
            SubCategoryFoodDrinkRepository subCategoryFoodDrinkRepository,
            SizeRepository sizeRepository) {
        this.foodRepository = foodRepository;
        this.categoryFoodDrinkRepository = categoryFoodDrinkRepository;
        this.subCategoryFoodDrinkRepository = subCategoryFoodDrinkRepository;
        this.sizeRepository = sizeRepository;
    }

    @Override
    public Food createFood(String name, String description, BigDecimal price,
            Long categoryId, Long subCategoryId, Long sizeId) {

        CategoryFood_Drink category = categoryFoodDrinkRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new RuntimeException("Size not found"));

        SubCategoryFood_Drink subCategory = null;
        if (subCategoryId != null) {
            subCategory = subCategoryFoodDrinkRepository.findById(subCategoryId)
                    .orElseThrow(() -> new RuntimeException("Subcategory not found"));
        }

        Food food = new Food();
        food.setName(name);
        food.setDescription(description);
        food.setPrice(price);
        food.setCategoryFood_Drink(category);
        food.setSubCategoryFood_Drink(subCategory);
        food.setSize(size);

        return foodRepository.save(food);
    }

    @Override
    public void deleteFood(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));
        foodRepository.delete(food);
    }


    @Override
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    @Override
    public Optional<Food> getFoodById(Long id) {
        return foodRepository.findById(id);
    }

//     @Override
//     public Food updateFood(Long foodId, String name, String description, BigDecimal price,
//             Long categoryId, Long subCategoryId, Long sizeId) {

//         Food food = foodRepository.findByIdWithFiles(foodId)
//                 .orElseThrow(() -> new RuntimeException("Food not found"));

//         CategoryFood_Drink category = categoryFoodDrinkRepository.findById(categoryId)
//                 .orElseThrow(() -> new RuntimeException("Category not found"));

//         Size size = sizeRepository.findById(sizeId)
//                 .orElseThrow(() -> new RuntimeException("Size not found"));

//         SubCategoryFood_Drink subCategory = null;
//         if (subCategoryId != null) {
//             subCategory = subCategoryFoodDrinkRepository.findById(subCategoryId)
//                     .orElseThrow(() -> new RuntimeException("Subcategory not found"));
//         }

//         food.setName(name);
//         food.setDescription(description);
//         food.setPrice(price);
//         food.setCategoryFood_Drink(category);
//         food.setSubCategoryFood_Drink(subCategory);
//         food.setSize(size);

//         return foodRepository.save(food);
//     }

@Override
public Food updateFood(Long foodId, String name, String description, BigDecimal price,
        Long categoryId, Long subCategoryId, Long sizeId) {

    // Fetch existing food entity
    Food food = foodRepository.findById(foodId)
            .orElseThrow(() -> new RuntimeException("Food not found"));

    // Fetch related entities (category, subcategory, size)
    CategoryFood_Drink category = categoryFoodDrinkRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));

    Size size = sizeRepository.findById(sizeId)
            .orElseThrow(() -> new RuntimeException("Size not found"));

    SubCategoryFood_Drink subCategory = null;
    if (subCategoryId != null) {
        subCategory = subCategoryFoodDrinkRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("Subcategory not found"));
    }

    // Update all fields in the existing food entity
    food.setName(name);
    food.setDescription(description);
    food.setPrice(price);
    food.setCategoryFood_Drink(category);
    food.setSubCategoryFood_Drink(subCategory);
    food.setSize(size);

    return foodRepository.save(food);
}


}
