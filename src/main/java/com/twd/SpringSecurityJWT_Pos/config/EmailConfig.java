package com.twd.SpringSecurityJWT_Pos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        // Configure the SMTP server properties
        mailSender.setHost("smtp.gmail.com"); // Example: Gmail SMTP server
        mailSender.setPort(587); // Port for TLS/STARTTLS
        mailSender.setUsername("your-email@gmail.com"); // Your email address
        mailSender.setPassword("your-email-password"); // Your email password
        
        // Set additional properties for the mail sender
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        return mailSender;
    }
    
    // Example of a helper bean for sending email
    @Bean
    public MimeMessageHelper mimeMessageHelper(JavaMailSender javaMailSender) throws MessagingException {
        return new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
    }
}