package com.foodkeeper.repository;

import com.foodkeeper.model.FoodItem;
import com.foodkeeper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    
    // Find items by user
    List<FoodItem> findByUserOrderByCreatedAtDesc(User user);
    
    // Find item by id and user (for security)
    Optional<FoodItem> findByIdAndUser(Long id, User user);
    
    // Find items by name and user (case-insensitive)
    List<FoodItem> findByUserAndNameContainingIgnoreCaseOrderByCreatedAtDesc(User user, String name);
    
    // Find items consumed within a date range for a specific user
    @Query("SELECT f FROM FoodItem f WHERE f.user = :user AND f.consumedDate BETWEEN :startDate AND :endDate ORDER BY f.consumedDate DESC")
    List<FoodItem> findByUserAndConsumedDateBetween(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find items created in the last N days for a specific user
    @Query("SELECT f FROM FoodItem f WHERE f.user = :user AND f.createdAt >= :fromDate ORDER BY f.createdAt DESC")
    List<FoodItem> findRecentItemsByUser(@Param("user") User user, @Param("fromDate") LocalDateTime fromDate);
    
    // Find consumed items for a specific user
    @Query("SELECT f FROM FoodItem f WHERE f.user = :user AND f.consumedDate IS NOT NULL ORDER BY f.consumedDate DESC")
    List<FoodItem> findConsumedItemsByUser(@Param("user") User user);
    
    // Count total items for a specific user
    @Query("SELECT COUNT(f) FROM FoodItem f WHERE f.user = :user")
    Long countTotalItemsByUser(@Param("user") User user);
    
    // Count consumed items for a specific user
    @Query("SELECT COUNT(f) FROM FoodItem f WHERE f.user = :user AND f.consumedDate IS NOT NULL")
    Long countConsumedItemsByUser(@Param("user") User user);
    
    // Find items with calories for a specific user
    @Query("SELECT f FROM FoodItem f WHERE f.user = :user AND f.calorie IS NOT NULL AND f.calorie > 0 ORDER BY f.createdAt DESC")
    List<FoodItem> findItemsWithCaloriesByUser(@Param("user") User user);
    
    // Get total calories consumed by a user
    @Query("SELECT COALESCE(SUM(f.calorie), 0) FROM FoodItem f WHERE f.user = :user AND f.calorie IS NOT NULL")
    Long getTotalCaloriesByUser(@Param("user") User user);
} 