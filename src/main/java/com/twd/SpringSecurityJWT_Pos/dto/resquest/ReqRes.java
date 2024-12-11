package com.twd.SpringSecurityJWT_Pos.dto.resquest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.twd.SpringSecurityJWT_Pos.entity.User;
// import com.twd.SpringSecurityJWT_Pos.entity.Product;

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
    private String email;
    private String role;
    private String password;
    // private List<Product> products;
    private User ourUsers;

    public void validateRole(){
        if (!List.of("ADMIN", "STAFF", "MANAGER").contains(role)) {
            throw new IllegalArgumentException("Invalid role provided");
        }
    }
}