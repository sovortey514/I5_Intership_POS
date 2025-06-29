package com.twd.Pos.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.twd.Pos.entity.Food;
import com.twd.Pos.service.FoodService;

@RestController
@RequestMapping("/auth")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/GettAllfoods")
    public ResponseEntity<List<Food>> getAllFoods() {
        return ResponseEntity.ok(foodService.getAllFoods());
    }

    @GetMapping("/GetfoodsById/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) {
        Optional<Food> food = foodService.getFoodById(id);
        return food.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/Createfoods")
    public ResponseEntity<Food> createFood(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam Long categoryId,
            @RequestParam Long subCategoryId,
            @RequestParam Long sizeId) {
        
        Food food = foodService.createFood(name, description, price, categoryId, subCategoryId, sizeId);
        return ResponseEntity.ok(food);
    }

    @PutMapping("/Updatefoods/{id}")
    public ResponseEntity<Food> updateFood(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam Long categoryId,
            @RequestParam Long subCategoryId,
            @RequestParam Long sizeId) {
        
        Food updatedFood = foodService.updateFood(id, name, description, price, categoryId, subCategoryId, sizeId);
        return ResponseEntity.ok(updatedFood);

        
    }
    @DeleteMapping("/deletefoods/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    

}
