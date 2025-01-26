package com.twd.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.FixedAssetDetail;

@Repository
public interface FixedAssetDetailRepository extends JpaRepository<FixedAssetDetail, Long> {
    
}
