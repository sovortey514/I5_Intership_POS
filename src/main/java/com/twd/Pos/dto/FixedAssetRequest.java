package com.twd.Pos.dto;

import java.time.LocalDate;



import lombok.Data;

@Data
public class FixedAssetRequest {
    private String name;
    private Double price;
    private LocalDate purchaseDate;
    private Integer quantity;
    private byte[] image; 
    private Integer categoryId; 
    private String remark;

    
}