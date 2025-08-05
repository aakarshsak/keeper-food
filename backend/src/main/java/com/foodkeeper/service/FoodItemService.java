package com.foodkeeper.service;

import com.foodkeeper.model.FoodItem;
import com.foodkeeper.repository.FoodItemRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    private static final DateTimeFormatter CSV_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Get all food items ordered by creation date
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAllByOrderByCreatedAtDesc();
    }
    
    // Get food item by ID
    public Optional<FoodItem> getFoodItemById(Long id) {
        return foodItemRepository.findById(id);
    }
    
    // Save a new food item
    @Transactional
    public FoodItem saveFoodItem(FoodItem foodItem) {
        if (foodItem.getCreatedAt() == null) {
            foodItem.setCreatedAt(LocalDateTime.now());
        }
        System.out.println("About to save food item: " + foodItem);
        FoodItem saved = foodItemRepository.saveAndFlush(foodItem);
        System.out.println("Successfully saved food item with ID: " + saved.getId());
        return saved;
    }
    
    // Update an existing food item
    public FoodItem updateFoodItem(Long id, com.fasterxml.jackson.databind.JsonNode requestBody) {
        return foodItemRepository.findById(id)
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
    
    // Delete a food item
    public void deleteFoodItem(Long id) {
        if (foodItemRepository.existsById(id)) {
            foodItemRepository.deleteById(id);
        } else {
            throw new RuntimeException("Food item not found with id: " + id);
        }
    }
    
    // Search food items by name
    public List<FoodItem> searchFoodItemsByName(String name) {
        return foodItemRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Get recently added items (last 7 days)
    public List<FoodItem> getRecentFoodItems() {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        return foodItemRepository.findRecentItems(weekAgo);
    }
    
    // Get food items consumed recently (last 7 days)
    public List<FoodItem> getRecentlyConsumedItems() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return foodItemRepository.findByConsumedDateAfter(sevenDaysAgo);
    }
    
    // Get total count of food items
    public long getTotalCount() {
        return foodItemRepository.count();
    }
    
    // Get food items within a date range (based on creation date)
    public List<FoodItem> getFoodItemsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return foodItemRepository.findByCreatedAtBetween(startDate, endDate);
        } else if (startDate != null) {
            return foodItemRepository.findByCreatedAtAfter(startDate);
        } else {
            return foodItemRepository.findAllByOrderByCreatedAtDesc();
        }
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