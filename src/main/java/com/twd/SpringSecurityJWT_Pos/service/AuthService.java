package com.twd.SpringSecurityJWT_Pos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.twd.SpringSecurityJWT_Pos.dto.resquest.ReqRes;
import com.twd.SpringSecurityJWT_Pos.entity.User;
import com.twd.SpringSecurityJWT_Pos.repository.UserRepo;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes signUp(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            // Check if the email already exists
            Optional<User> existingUser = ourUserRepo.findByEmail(registrationRequest.getEmail());
            if (existingUser.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Email already exists");
                return resp;
            }
    
            // Save the user to the database
            User newUser = new User();
            newUser.setEmail(registrationRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            newUser.setRole(registrationRequest.getRole().toUpperCase());
            User savedUser = ourUserRepo.save(newUser);
    
            // Construct the response
            if (savedUser.getId() > 0) {
                resp.setOurUsers(savedUser);
                resp.setMessage("User registered successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Failed to register user");
                resp.setStatusCode(500);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Error during sign-up: " + e.getMessage());
        }
        return resp;
    }
    
    
    
    
    


    public ReqRes signIn(ReqRes signinRequest){
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
            var user = ourUserRepo.findByEmail(signinRequest.getEmail()).orElseThrow();
            System.out.println("USER IS: "+ user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
        User users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }

    public boolean validateToken(String token){
        try {
            String username = jwtUtils.extractUsername(token);
            User user = ourUserRepo.findByEmail(username).orElse(null);
            return user != null && jwtUtils.isTokenValid(token, user);
        } catch (Exception e) {
            return false;
        }
    }
    
    
}
