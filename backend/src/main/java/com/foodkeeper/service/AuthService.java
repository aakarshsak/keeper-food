package com.foodkeeper.service;

import com.foodkeeper.dto.AuthRequest;
import com.foodkeeper.dto.AuthResponse;
import com.foodkeeper.model.AuthProvider;
import com.foodkeeper.model.OtpType;
import com.foodkeeper.model.User;
import com.foodkeeper.repository.UserRepository;
import com.foodkeeper.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public AuthResponse.MessageResponse registerUser(AuthRequest.SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new AuthResponse.MessageResponse("Error: Email is already in use!", false);
        }

        // Create new user account
        User user = new User(
            signUpRequest.getFirstName(),
            signUpRequest.getLastName(),
            signUpRequest.getEmail(),
            passwordEncoder.encode(signUpRequest.getPassword())
        );

        user.setProvider(AuthProvider.LOCAL);
        userRepository.save(user);

        // Send email verification OTP
        try {
            String otp = otpService.generateAndSaveOtp(user.getEmail(), OtpType.EMAIL_VERIFICATION);
            emailService.sendOtpEmail(user.getEmail(), otp, "Email Verification");
        } catch (Exception e) {
            // Log error but don't fail registration
            System.err.println("Failed to send verification email: " + e.getMessage());
        }

        return new AuthResponse.MessageResponse("User registered successfully. Please check your email for verification OTP.");
    }

    public AuthResponse.JwtResponse authenticateUser(AuthRequest.LoginRequest loginRequest) {
        // Check if user exists and is verified
        User user = userRepository.findByLocalEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email first");
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new AuthResponse.JwtResponse(jwt, user);
    }

    @Transactional
    public AuthResponse.MessageResponse verifyEmail(AuthRequest.OtpVerificationRequest request) {
        if (!otpService.verifyOtp(request.getEmail(), request.getOtp(), OtpType.EMAIL_VERIFICATION)) {
            return new AuthResponse.MessageResponse("Invalid or expired OTP", false);
        }

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);
        userRepository.save(user);

        // Send welcome email
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        return new AuthResponse.MessageResponse("Email verified successfully!");
    }

    public AuthResponse.MessageResponse sendPasswordResetOtp(AuthRequest.EmailRequest request) {
        User user = userRepository.findByLocalEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = otpService.generateAndSaveOtp(user.getEmail(), OtpType.PASSWORD_RESET);
        emailService.sendOtpEmail(user.getEmail(), otp, "Password Reset");

        return new AuthResponse.MessageResponse("Password reset OTP sent to your email");
    }

    @Transactional
    public AuthResponse.MessageResponse resetPassword(AuthRequest.PasswordResetRequest request) {
        if (!otpService.verifyOtp(request.getEmail(), request.getOtp(), OtpType.PASSWORD_RESET)) {
            return new AuthResponse.MessageResponse("Invalid or expired OTP", false);
        }

        User user = userRepository.findByLocalEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new AuthResponse.MessageResponse("Password reset successfully!");
    }

    public AuthResponse.MessageResponse resendVerificationOtp(AuthRequest.EmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            return new AuthResponse.MessageResponse("Email is already verified", false);
        }

        String otp = otpService.generateAndSaveOtp(user.getEmail(), OtpType.EMAIL_VERIFICATION);
        emailService.sendOtpEmail(user.getEmail(), otp, "Email Verification");

        return new AuthResponse.MessageResponse("Verification OTP sent to your email");
    }

    public AuthResponse.UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        return new AuthResponse.UserResponse(user);
    }
} 