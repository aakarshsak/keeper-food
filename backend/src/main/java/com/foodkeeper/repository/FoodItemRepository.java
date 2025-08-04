package com.foodkeeper.repository;

import com.foodkeeper.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
} 