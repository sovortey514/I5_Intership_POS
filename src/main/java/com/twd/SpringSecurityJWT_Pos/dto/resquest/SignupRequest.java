package com.twd.SpringSecurityJWT_Pos.dto.resquest;

import lombok.Data;

@Data
public class SignupRequest {

    private String email;
    private String password;
    private String role;  // Role can be 'STAFF' or 'MANAGER'

    // Getters and Setters
}