package com.foodkeeper.controller;

import com.foodkeeper.dto.AuthRequest;
import com.foodkeeper.dto.AuthResponse;
import com.foodkeeper.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthRequest.SignupRequest signUpRequest) {
        try {
            AuthResponse.MessageResponse response = authService.registerUser(signUpRequest);
            
            // Check if registration was successful
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                // Return 400 Bad Request for validation errors (like existing email)
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse.MessageResponse("Registration failed: " + e.getMessage(), false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest.LoginRequest loginRequest) {
        try {
            AuthResponse.JwtResponse response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse.MessageResponse("Login failed: " + e.getMessage(), false));
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody AuthRequest.OtpVerificationRequest request) {
        try {
            AuthResponse.MessageResponse response = authService.verifyEmail(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse.MessageResponse("Email verification failed: " + e.getMessage(), false));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerificationOtp(@Valid @RequestBody AuthRequest.EmailRequest request) {
        try {
            AuthResponse.MessageResponse response = authService.resendVerificationOtp(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse.MessageResponse("Failed to resend OTP: " + e.getMessage(), false));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody AuthRequest.EmailRequest request) {
        try {
            AuthResponse.MessageResponse response = authService.sendPasswordResetOtp(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse.MessageResponse("Failed to send reset OTP: " + e.getMessage(), false));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody AuthRequest.PasswordResetRequest request) {
        try {
            AuthResponse.MessageResponse response = authService.resetPassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse.MessageResponse("Password reset failed: " + e.getMessage(), false));
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getCurrentUser() {
        try {
            AuthResponse.UserResponse response = authService.getCurrentUser();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse.MessageResponse("Failed to get user info: " + e.getMessage(), false));
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(new AuthResponse.MessageResponse("User logged out successfully!"));
    }
} 