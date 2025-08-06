package com.foodkeeper.service;

import com.foodkeeper.model.FoodItem;
import com.foodkeeper.model.User;
import com.foodkeeper.repository.FoodItemRepository;
import com.foodkeeper.repository.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {
    
    @Autowired
    private FoodItemRepository foodItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private static final DateTimeFormatter CSV_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    // Get all food items for current user ordered by creation date
    public List<FoodItem> getAllFoodItems() {
        User currentUser = getCurrentUser();
        return foodItemRepository.findByUserOrderByCreatedAtDesc(currentUser);
    }
    
    // Get food item by ID for current user
    public Optional<FoodItem> getFoodItemById(Long id) {
        User currentUser = getCurrentUser();
        return foodItemRepository.findByIdAndUser(id, currentUser);
    }
    
    // Save a new food item for current user
    @Transactional
    public FoodItem saveFoodItem(FoodItem foodItem) {
        User currentUser = getCurrentUser();
        foodItem.setUser(currentUser);
        
        if (foodItem.getCreatedAt() == null) {
            foodItem.setCreatedAt(LocalDateTime.now());
        }
        System.out.println("About to save food item: " + foodItem + " for user: " + currentUser.getEmail());
        FoodItem saved = foodItemRepository.saveAndFlush(foodItem);
        System.out.println("Successfully saved food item with ID: " + saved.getId());
        return saved;
    }
    
    // Update an existing food item for current user
    public FoodItem updateFoodItem(Long id, com.fasterxml.jackson.databind.JsonNode requestBody) {
        User currentUser = getCurrentUser();
        return foodItemRepository.findByIdAndUser(id, currentUser)
                .map(existingItem -> {
                    existingItem.setName(requestBody.get("name").asText());
                    
                    if (requestBody.has("description") && !requestBody.get("description").isNull()) {
                        String description = requestBody.get("description").asText();
                        existingItem.setDescription(description.trim().isEmpty() ? null : description);
                    }
                    
                    if (requestBody.has("consumedDate") && !requestBody.get("consumedDate").isNull()) {
                        String dateTimeStr = requestBody.get("consumedDate").asText();
                        if (!dateTimeStr.trim().isEmpty()) {
                            try {
                                // Handle datetime-local format: "YYYY-MM-DDTHH:MM"
                                LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
                                existingItem.setConsumedDate(dateTime);
                            } catch (Exception e) {
                                // Fallback: try to parse as date only and set to start of day
                                try {
                                    java.time.LocalDate date = java.time.LocalDate.parse(dateTimeStr);
                                    existingItem.setConsumedDate(date.atStartOfDay());
                                } catch (Exception ex) {
                                    System.err.println("Error parsing datetime: " + dateTimeStr + ", error: " + ex.getMessage());
                                    existingItem.setConsumedDate(null);
                                }
                            }
                        } else {
                            existingItem.setConsumedDate(null);
                        }
                    }
                    
                    if (requestBody.has("calorie") && !requestBody.get("calorie").isNull()) {
                        int calorie = requestBody.get("calorie").asInt();
                        existingItem.setCalorie(calorie > 0 ? calorie : null);
                    }
                    
                    if (requestBody.has("quantity") && !requestBody.get("quantity").isNull()) {
                        String quantity = requestBody.get("quantity").asText();
                        existingItem.setQuantity(quantity.trim().isEmpty() ? null : quantity);
                    }
                    
                    return foodItemRepository.save(existingItem);
                })
                .orElseThrow(() -> new RuntimeException("Food item not found with id: " + id));
    }
    
    // Delete a food item for current user
    public void deleteFoodItem(Long id) {
        User currentUser = getCurrentUser();
        Optional<FoodItem> foodItem = foodItemRepository.findByIdAndUser(id, currentUser);
        
        if (foodItem.isPresent()) {
            foodItemRepository.delete(foodItem.get());
        } else {
            throw new RuntimeException("Food item not found or access denied with id: " + id);
        }
    }
    
    // Search food items by name for current user
    public List<FoodItem> searchFoodItemsByName(String name) {
        User currentUser = getCurrentUser();
        return foodItemRepository.findByUserAndNameContainingIgnoreCaseOrderByCreatedAtDesc(currentUser, name);
    }
    
    // Get recently added items (last 7 days) for current user
    public List<FoodItem> getRecentFoodItems() {
        User currentUser = getCurrentUser();
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        return foodItemRepository.findRecentItemsByUser(currentUser, weekAgo);
    }
    
    // Get consumed food items for current user
    public List<FoodItem> getRecentlyConsumedItems() {
        User currentUser = getCurrentUser();
        return foodItemRepository.findConsumedItemsByUser(currentUser);
    }
    
    // Get total count of food items for current user
    public long getTotalCount() {
        User currentUser = getCurrentUser();
        return foodItemRepository.countTotalItemsByUser(currentUser);
    }
    
    // Get food items within a date range for current user (based on creation date)
    public List<FoodItem> getFoodItemsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        User currentUser = getCurrentUser();
        if (startDate != null && endDate != null) {
            return foodItemRepository.findByUserAndConsumedDateBetween(currentUser, startDate, endDate);
        } else if (startDate != null) {
            return foodItemRepository.findRecentItemsByUser(currentUser, startDate);
        } else {
            return foodItemRepository.findByUserOrderByCreatedAtDesc(currentUser);
        }
    }
    
    // Get food items with calories for current user
    public List<FoodItem> getFoodItemsWithCalories() {
        User currentUser = getCurrentUser();
        return foodItemRepository.findItemsWithCaloriesByUser(currentUser);
    }
    
    // Get total calories for current user
    public Long getTotalCalories() {
        User currentUser = getCurrentUser();
        return foodItemRepository.getTotalCaloriesByUser(currentUser);
    }
    
    // Get consumed items count for current user
    public Long getConsumedItemsCount() {
        User currentUser = getCurrentUser();
        return foodItemRepository.countConsumedItemsByUser(currentUser);
    }
    
    // Export food items to CSV
    public String exportFoodItemsToCSV(LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<FoodItem> items = getFoodItemsByDateRange(startDate, endDate);
        
        StringWriter writer = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("ID", "Name", "Description", "Calories", "Quantity", "Created Date", "Consumed Date")
                .build();
        
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
            for (FoodItem item : items) {
                csvPrinter.printRecord(
                    item.getId(),
                    item.getName(),
                    item.getDescription() != null ? item.getDescription() : "",
                    item.getCalorie() != null ? item.getCalorie() : "",
                    item.getQuantity() != null ? item.getQuantity() : "",
                    item.getCreatedAt() != null ? item.getCreatedAt().format(CSV_DATE_FORMATTER) : "",
                    item.getConsumedDate() != null ? item.getConsumedDate().format(CSV_DATE_FORMATTER) : ""
                );
            }
        }
        
        return writer.toString();
    }
} 