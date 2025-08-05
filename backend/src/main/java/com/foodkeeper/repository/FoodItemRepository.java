package com.foodkeeper.repository;

import com.foodkeeper.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    
    // Find all food items ordered by creation date (newest first)
    List<FoodItem> findAllByOrderByCreatedAtDesc();
    
    // Find food items by name containing a string (case insensitive)
    List<FoodItem> findByNameContainingIgnoreCase(String name);
    
    // Find food items created after a specific date
    List<FoodItem> findByCreatedAtAfter(LocalDateTime date);
    
    // Find food items consumed before a specific date
    List<FoodItem> findByConsumedDateBefore(LocalDateTime date);
    
    // Find food items consumed after a specific date
    List<FoodItem> findByConsumedDateAfter(LocalDateTime date);
    
    // Custom query to find recently added items (last 7 days)
    @Query("SELECT f FROM FoodItem f WHERE f.createdAt >= :weekAgo ORDER BY f.createdAt DESC")
    List<FoodItem> findRecentItems(LocalDateTime weekAgo);
    
    // Find food items created within a date range
    @Query("SELECT f FROM FoodItem f WHERE f.createdAt BETWEEN :startDate AND :endDate ORDER BY f.createdAt DESC")
    List<FoodItem> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find food items consumed within a date range
    @Query("SELECT f FROM FoodItem f WHERE f.consumedDate BETWEEN :startDate AND :endDate ORDER BY f.consumedDate DESC")
    List<FoodItem> findByConsumedDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
} 