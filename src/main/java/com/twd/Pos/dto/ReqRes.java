package com.twd.Pos.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.twd.Pos.entity.Category;
// import com.twd.Pos.entity.Department;
import com.twd.Pos.entity.FixedAssetCounts;
import com.twd.Pos.entity.FixedAssetDetail;
import com.twd.Pos.entity.Material;
import com.twd.Pos.entity.OurUsers;
import com.twd.Pos.entity.Product;

import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String username;
    private String role;
    private String password;
    private String phoneNumber;
    private List<OurUsers> allUsers;
    private List<Product> products;
    private Category category;
    private List<Category> categories;
    private OurUsers ourUsers;
    private Material fixedAsset;
    private List<Material> fixedAssets;
    // private Building building;
    // private List<Building> buildings; 
   
    // private Department department;
    // private List<Department> departments;

    // private AssetHolder assetHolder;
    // private List<AssetHolder> assetHolders;

    private FixedAssetDetail fixedAssetDetail;
    private List<FixedAssetDetail> fixedAssetDetails;

    private FixedAssetCounts fixedAssetCounts;
    private List<FixedAssetCounts> fixedAssetCounts2;

    private String status;
    private String statustext;

}