package com.foodkeeper.service;

import com.foodkeeper.model.OtpType;
import com.foodkeeper.model.OtpVerification;
import com.foodkeeper.repository.OtpVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    @Autowired
    private OtpVerificationRepository otpRepository;

    private static final int OTP_EXPIRATION_MINUTES = 10;
    private static final SecureRandom random = new SecureRandom();

    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    @Transactional
    public String generateAndSaveOtp(String email, OtpType type) {
        // Delete any existing OTPs for this email and type
        otpRepository.deleteByEmailAndType(email, type);

        String otp = generateOtp();
        OtpVerification otpVerification = new OtpVerification(email, otp, type, OTP_EXPIRATION_MINUTES);
        otpRepository.save(otpVerification);

        return otp;
    }

    @Transactional
    public boolean verifyOtp(String email, String otp, OtpType type) {
        Optional<OtpVerification> otpVerificationOpt = 
            otpRepository.findByEmailAndOtpAndTypeAndVerifiedFalse(email, otp, type);

        if (otpVerificationOpt.isEmpty()) {
            return false;
        }

        OtpVerification otpVerification = otpVerificationOpt.get();

        if (otpVerification.isExpired()) {
            return false;
        }

        // Mark as verified
        otpVerification.setVerified(true);
        otpRepository.save(otpVerification);

        return true;
    }

    public boolean isValidOtp(String email, String otp, OtpType type) {
        return otpRepository.existsByEmailAndOtpAndTypeAndVerifiedFalse(email, otp, type);
    }

    public Optional<OtpVerification> getLatestOtp(String email, OtpType type) {
        return otpRepository.findTopByEmailAndTypeOrderByCreatedAtDesc(email, type);
    }

    // Clean up expired OTPs every hour
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    @Transactional
    public void cleanupExpiredOtps() {
        otpRepository.deleteExpiredOtps(LocalDateTime.now());
    }
} 