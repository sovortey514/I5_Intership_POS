package com.twd.Pos.dto;

import lombok.Data;

@Data
public class MembershipDTO {
    
    private Long id;
    private String membershipId;
    private String name;
    private String membershipType;
    private Double balance;
    private String gender;


    public MembershipDTO(Long id, String membershipId, String name, String membershipType, Double balance, String gender) {
        this.id = id;
        this.membershipId = membershipId;
        this.name = name;
        this.membershipType = membershipType;
        this.balance = balance;
        this.gender = gender;
    }
}
