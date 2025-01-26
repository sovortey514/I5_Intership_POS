package com.twd.Pos.service;


import java.util.List;
import java.util.Optional;

import com.twd.Pos.entity.FixedAssetCounts;

public interface FixedAssetCountService {
    
    FixedAssetCounts createFixedAssetCounts(FixedAssetCounts fixedAssetCounts);
    // void deleteFixedAssetCount(Long id);
    List<FixedAssetCounts> getAllFixedAssetCounts();
    Optional<FixedAssetCounts> getAllFixedAssetCountsById(Long id);
    FixedAssetCounts upFixedAssetCounts(Long id, FixedAssetCounts fixedAssetCounts);

}
