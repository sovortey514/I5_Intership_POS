package com.twd.Pos.dto;

import lombok.Data;

@Data
public class FixedAssetDetailRequest {
    private Long countId;
    private Long assetHolderId;
    private Long assetId;
    private Integer quantityCounted;
    private String conditions;
    private String existenceAsset;
    private String remarks;
}
