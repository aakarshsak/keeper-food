package com.foodkeeper.repository;

import com.foodkeeper.model.AuthProvider;
import com.foodkeeper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Boolean existsByEmail(String email);
    
    Optional<User> findByEmailAndProvider(String email, AuthProvider provider);
    
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);
    
    // For OAuth2 users
    default Optional<User> findByGoogleEmail(String email) {
        return findByEmailAndProvider(email, AuthProvider.GOOGLE);
    }
    
    // For local users
    default Optional<User> findByLocalEmail(String email) {
        return findByEmailAndProvider(email, AuthProvider.LOCAL);
    }
} 