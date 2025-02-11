package com.twd.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.twd.Pos.entity.SubCategoryFood_Drink;

@Repository
public interface SubCategoryFoodDrinkRepository extends JpaRepository<SubCategoryFood_Drink, Long>{
     @Query("SELECT s FROM SubCategoryFood_Drink s JOIN FETCH s.category")
    List<SubCategoryFood_Drink> findAllWithCategory();
}
