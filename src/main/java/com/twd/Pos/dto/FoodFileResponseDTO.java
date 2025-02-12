package com.twd.Pos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.twd.Pos.entity.Food;

import lombok.Data;

@Data
public class FoodFileResponseDTO {
     private Long foodId;
    private String foodName;
    private String description;
    private BigDecimal price;
    private String categoryName;
    private String subCategoryName;
    private String sizeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<FileDataDTO> files;

    public FoodFileResponseDTO(Food food, List<FileDataDTO> files) {
        this.foodId = food.getId();
        this.foodName = food.getName();
        this.description = food.getDescription();
        this.price = food.getPrice();
        this.categoryName = food.getCategoryFood_Drink() != null ? food.getCategoryFood_Drink().getName() : null;
        this.subCategoryName = food.getSubCategoryFood_Drink() != null ? food.getSubCategoryFood_Drink().getName() : null;
        this.sizeName = food.getSize() != null ? food.getSize().getName() : null;
        this.createdAt = food.getCreatedAt();
        this.updatedAt = food.getUpdatedAt();
        this.files = files;
    }

    
}
