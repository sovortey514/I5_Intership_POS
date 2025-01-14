package com.twd.SpringSecurityJWT_Pos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twd.SpringSecurityJWT_Pos.dto.resquest.RegisterRequest;
import com.twd.SpringSecurityJWT_Pos.dto.resquest.ReqRes;
import com.twd.SpringSecurityJWT_Pos.entity.User;
import com.twd.SpringSecurityJWT_Pos.repository.UserRepo;
import org.springframework.security.core.Authentication;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;

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
            // Validate email
            String email = registrationRequest.getEmail();
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                resp.setStatusCode(400);
                resp.setMessage("Invalid email format");
                return resp;
            }

            // Validate password
            String password = registrationRequest.getPassword();
            if (password == null || !isValidPassword(password)) {
                resp.setStatusCode(400);
                resp.setMessage("Password must be at least 8 characters long, " +
                        "contain one uppercase letter, one lowercase letter, " +
                        "one digit, and one special character");
                return resp;
            }

            // Check if the email already exists
            Optional<User> existingUser = ourUserRepo.findByEmail(registrationRequest.getEmail());
            if (existingUser.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Email already exists");
                return resp;
            }

            // Determine the role
            String role = registrationRequest.getRole();
            if (role == null || role.isEmpty()) {
                role = "ADMIN"; // Default to "ADMIN" if no role is provided
            } else if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("STAFF") && !role.equalsIgnoreCase("SUPERADMIN")) {
                resp.setStatusCode(400);
                resp.setMessage("Invalid role. Role must be 'ADMIN', 'STAFF', or 'SUPERADMIN'");
                return resp;
            }

            // Save the user to the database
            User newUser = new User();
            newUser.setEmail(registrationRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            newUser.setRole(role.toUpperCase());
            
            newUser.setEnabled(true);// Ensure role is stored in uppercase
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

    public boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return passwordPattern != null && password.matches(passwordPattern);
    }

    public ReqRes signIn(ReqRes signinRequest) {
        ReqRes response = new ReqRes();

        try {

            User user = ourUserRepo.findByEmail(signinRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.isEnabled()) {
                response.setStatusCode(403);
                response.setMessage("Account is disabled. Please contact admin.");
                return response;
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

            String jwt = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("Sign-in error: " + e.getMessage());
        }
        return response;
    }

    @Transactional
    public ReqRes updateUser(Long userId, RegisterRequest updateRequest) {
        ReqRes response = new ReqRes();
        try {
            // Retrieve user by ID, throw exception if not found
            User user = ourUserRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // if (updateRequest.getName() != null) {
            //     user.setName(updateRequest.getName());
            // }
            if (updateRequest.getUsername() != null) {
                user.setName(updateRequest.getUsername());
            }
            if (updateRequest.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
            }
            if (updateRequest.getRole() != null) {
                user.setRole(updateRequest.getRole());
            }
            User updatedUser = ourUserRepo.save(user);
            response.setOurUsers(updatedUser);
            response.setMessage("User updated successfully.");
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Update error: " + e.getMessage());
        }
        return response;
    }

    public List<User> getAllUsers() {
        return ourUserRepo.findAll();
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String username = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User user = ourUserRepo.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                String newJwt = jwtUtils.generateToken(user);
                response.setStatusCode(200);
                response.setToken(newJwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Token refreshed successfully.");
            } else {
                response.setStatusCode(401);
                response.setError("Invalid or expired token.");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Token refresh error: " + e.getMessage());
        }
        return response;
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtUtils.extractUsername(token);
            User user = ourUserRepo.findByEmail(username).orElse(null);
            return user != null && jwtUtils.isTokenValid(token, user);
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return ourUserRepo.findByUsername(username);
    }

    public User getUserById(Long userId) {
        return ourUserRepo.findById(userId).orElse(null);
    }

    @Transactional
    public ReqRes deleteUser(Long userId) {
        ReqRes response = new ReqRes();
        try {
            if (!ourUserRepo.existsById(userId)) {
                response.setMessage("User not found.");
                response.setStatusCode(404);
                return response;
            }

            ourUserRepo.deleteById(userId);
            response.setMessage("User deleted successfully.");
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Delete error: " + e.getMessage());
        }
        return response;
    }

    public ReqRes disableUser(Long userId) {
        ReqRes response = new ReqRes();
        try {
           
            Optional<User> userOptional = ourUserRepo.findById(userId);
            
            if (!userOptional.isPresent()) {
                response.setStatusCode(404);
                response.setError("User not found with ID: " + userId);
                return response;
            }
    
            User user = userOptional.get();
       
            user.setEnabled(false);
            ourUserRepo.save(user);  
            
            response.setStatusCode(200);
            response.setMessage("User disabled successfully.");
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Disable error: " + e.getMessage());
            return response;
        }
    }
    public ReqRes enableUser(Long userId) {
        ReqRes response = new ReqRes();
        try {
           
            Optional<User> userOptional = ourUserRepo.findById(userId);
            
            if (!userOptional.isPresent()) {
                response.setStatusCode(404);
                response.setError("User not found with ID: " + userId);
                return response;
            }
    
            User user = userOptional.get();
       
            user.setEnabled(true);
            ourUserRepo.save(user);  
            
            response.setStatusCode(200);
            response.setMessage("User enable successfully.");
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Enable error: " + e.getMessage());
            return response;
        }
    }
    

}
