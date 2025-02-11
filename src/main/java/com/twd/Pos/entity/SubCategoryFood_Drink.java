package com.twd.Pos.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "subcategory_food_drink")
public class SubCategoryFood_Drink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime create_at;

    @Column(name = "updated_at")
    private LocalDateTime update_at;

     @PrePersist
    protected void onCreate() {
        this.create_at = LocalDateTime.now();
        this.create_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.update_at = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) 
   @JsonIgnoreProperties("subCategoryFoodDrinks")
    private CategoryFood_Drink category;
}
