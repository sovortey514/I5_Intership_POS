package com.twd.Pos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.FileData;
import com.twd.Pos.entity.Food;


@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long>{

	Optional<FileData> findByName(String fileName);
    // List<FileData> findByFixedAsset(Material fixedAsset);

    @Query("SELECT f FROM FileData f WHERE f.food.id = :foodId")
    List<FileData> findByFoodId(@Param("foodId") Long foodId);
}