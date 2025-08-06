package com.foodkeeper.dto;

import com.foodkeeper.model.User;

public class AuthResponse {
    
    // JWT Authentication Response
    public static class JwtResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private String profilePicture;
        private boolean emailVerified;
        
        public JwtResponse(String accessToken, User user) {
            this.token = accessToken;
            this.id = user.getId();
            this.email = user.getEmail();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.profilePicture = user.getProfilePicture();
            this.emailVerified = user.isEmailVerified();
        }
        
        // Getters and Setters
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getProfilePicture() { return profilePicture; }
        public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
        
        public boolean isEmailVerified() { return emailVerified; }
        public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    }
    
    // Generic Message Response
    public static class MessageResponse {
        private String message;
        private boolean success;
        
        public MessageResponse(String message, boolean success) {
            this.message = message;
            this.success = success;
        }
        
        public MessageResponse(String message) {
            this.message = message;
            this.success = true;
        }
        
        // Getters and Setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }
    
    // User Profile Response
    public static class UserResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String profilePicture;
        private boolean emailVerified;
        private String provider;
        
        public UserResponse(User user) {
            this.id = user.getId();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.email = user.getEmail();
            this.profilePicture = user.getProfilePicture();
            this.emailVerified = user.isEmailVerified();
            this.provider = user.getProvider() != null ? user.getProvider().name() : null;
        }
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getProfilePicture() { return profilePicture; }
        public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
        
        public boolean isEmailVerified() { return emailVerified; }
        public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
        
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
    }
} 