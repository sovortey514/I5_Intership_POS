package com.twd.Pos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.Material;

@Repository
public interface FixedAssetRepository extends JpaRepository<Material, Long> {
    
 
    @Query("SELECT DISTINCT fa FROM Material fa " +
           "JOIN FETCH fa.category")
    List<Material> findAllWithCategory();

    // @Query("SELECT fa FROM FixedAsset fa " +
    //     //    "JOIN fa.assetHolder ah " +
    //     //    "JOIN ah.department d " +
    //        "WHERE d.id = :departmentId")
    // List<FixedAsset> findFixedAssetsByDepartmentId(@Param("departmentId") Long departmentId);
    
    // boolean existsBySerialNumber(String serialNumber);
    
}
