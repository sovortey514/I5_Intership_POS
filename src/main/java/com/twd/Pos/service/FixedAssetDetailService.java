package com.twd.Pos.service;

import java.util.List;
import java.util.Optional;

import com.twd.Pos.entity.FixedAssetDetail;
public interface FixedAssetDetailService {
    List<FixedAssetDetail> getAllFixedAssetDetails();
    Optional<FixedAssetDetail> getFixedAssetDetailById(Long id);
    FixedAssetDetail createFixedAssetDetail(FixedAssetDetail fixedAssetDetail);
    FixedAssetDetail updateFixedAssetDetail(Long id, FixedAssetDetail fixedAssetDetail);
    void deleteFixedAssetDetail(Long id);
}
