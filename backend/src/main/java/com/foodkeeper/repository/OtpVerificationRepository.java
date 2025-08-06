package com.foodkeeper.repository;

import com.foodkeeper.model.OtpType;
import com.foodkeeper.model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    
    Optional<OtpVerification> findByEmailAndOtpAndTypeAndVerifiedFalse(String email, String otp, OtpType type);
    
    Optional<OtpVerification> findTopByEmailAndTypeOrderByCreatedAtDesc(String email, OtpType type);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpVerification o WHERE o.expiresAt < :now")
    void deleteExpiredOtps(LocalDateTime now);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpVerification o WHERE o.email = :email AND o.type = :type")
    void deleteByEmailAndType(String email, OtpType type);
    
    boolean existsByEmailAndOtpAndTypeAndVerifiedFalse(String email, String otp, OtpType type);
} 