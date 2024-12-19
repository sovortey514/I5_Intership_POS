package com.twd.SpringSecurityJWT_Pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twd.SpringSecurityJWT_Pos.dto.resquest.RegisterRequest;
import com.twd.SpringSecurityJWT_Pos.dto.resquest.ReqRes;
import com.twd.SpringSecurityJWT_Pos.entity.User;
import com.twd.SpringSecurityJWT_Pos.service.AuthService;
import java.util.List;
import java.util.Optional;
// import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.enums.ParameterIn;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Signup a new user", description = "Register a new user in the system")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest) {
        try {
            // Call the service layer to handle signup
            ReqRes response = authService.signUp(signUpRequest);

            // Return appropriate response based on the service result
            if (response.getStatusCode() == 200) {
                return ResponseEntity.ok(response);
            } else if (response.getStatusCode() == 400) {
                return ResponseEntity.badRequest().body(response);
            } else {
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            // Handle unexpected exceptions
            ReqRes errorResponse = new ReqRes();
            errorResponse.setStatusCode(500);
            errorResponse.setError("Unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest) {
        signInRequest.validateRole();
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Long userId) {
        ReqRes response = authService.deleteUser(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
    @PutMapping("/users/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Long userId, @RequestBody RegisterRequest updateRequest) {
        ReqRes response = authService.updateUser(userId, updateRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = authService.getUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
