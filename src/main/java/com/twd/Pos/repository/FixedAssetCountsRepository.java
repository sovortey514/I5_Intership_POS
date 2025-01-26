package com.twd.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.FixedAssetCounts;

@Repository
public interface FixedAssetCountsRepository extends JpaRepository<FixedAssetCounts, Long> {
    
}