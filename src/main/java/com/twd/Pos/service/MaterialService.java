package com.twd.Pos.service;

import java.util.List;
import java.util.Optional;

import com.twd.Pos.entity.Material;

public interface MaterialService {

    Material createFixedAsset(Material fixedAsset, Integer categoryId);
    
    void deleteFixedAsset(Long id);

    List<Material> getAllFixedAssets();

    // List<FixedAsset> getAllFixedAssetsWithDepartment(Long departmentId);

    Optional<Material> getFixedAssetById(Long id);

    Material updateFixedAsset(Long id, Material fixedAsset);

    
}
