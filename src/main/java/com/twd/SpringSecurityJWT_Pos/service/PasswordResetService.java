package com.twd.SpringSecurityJWT_Pos.service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.time.Duration;


import com.twd.SpringSecurityJWT_Pos.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.twd.SpringSecurityJWT_Pos.entity.PasswordResetRequest;
import com.twd.SpringSecurityJWT_Pos.repository.PasswordResetRequestRepository;
import com.twd.SpringSecurityJWT_Pos.repository.UserRepo;

// import io.swagger.v3.oas.annotations.servers.Server;

@Service
public class PasswordResetService {
    
    @Autowired
    private PasswordResetRequestRepository passwordResetRequestRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepo userRepository;

    

    private static final String RESET_PASSWORD_URL = "http://localhost:9090/reset-password?token=";

    public void sendResetPasswordEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate token
        String token = UUID.randomUUID().toString();

        // Save the token and email in the database
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail(user.getUsername());
        request.setToken(token);
        request.setCreatedAt(LocalDateTime.now());
        passwordResetRequestRepository.save(request);

        // Send email with the reset password link
        String resetLink = RESET_PASSWORD_URL + token;

        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the following link: " + resetLink);

        mailSender.send(message);
        System.out.println("sucessfully");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean validateToken(String token) {
        PasswordResetRequest request = passwordResetRequestRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
    
        // Check if the token is expired (e.g., after 1 hour)
        if (Duration.between(request.getCreatedAt(), LocalDateTime.now()).toHours() > 1) {
            throw new TokenExpiredException("Token has expired");
        }
        return true;
    }
    
    public void resetPassword(String token, String newPassword) {
        PasswordResetRequest request = passwordResetRequestRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
    
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    
        // Encode and set the new password
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
    
        // Delete the token after usage
        passwordResetRequestRepository.delete(request);
    }

    public class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }
    }
    
    public class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }
    
    
}
