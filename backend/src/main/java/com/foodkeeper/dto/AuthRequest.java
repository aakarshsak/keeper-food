package com.foodkeeper.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {
    
    // Login Request
    public static class LoginRequest {
        @NotBlank
        @Email
        private String email;
        
        @NotBlank
        private String password;
        
        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    // Signup Request
    public static class SignupRequest {
        @NotBlank
        @Size(max = 50)
        private String firstName;
        
        @NotBlank
        @Size(max = 50)
        private String lastName;
        
        @NotBlank
        @Email
        @Size(max = 100)
        private String email;
        
        @NotBlank
        @Size(min = 6, max = 40)
        private String password;
        
        // Getters and Setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    // OTP Verification Request
    public static class OtpVerificationRequest {
        @NotBlank
        @Email
        private String email;
        
        @NotBlank
        @Size(min = 6, max = 6)
        private String otp;
        
        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
    }
    
    // Password Reset Request
    public static class PasswordResetRequest {
        @NotBlank
        @Email
        private String email;
        
        @NotBlank
        @Size(min = 6, max = 6)
        private String otp;
        
        @NotBlank
        @Size(min = 6, max = 40)
        private String newPassword;
        
        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
        
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
    
    // Email Request (for sending OTP)
    public static class EmailRequest {
        @NotBlank
        @Email
        private String email;
        
        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
} 