package com.foodkeeper.controller;

import com.foodkeeper.model.FoodItem;
import com.foodkeeper.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/food-items")
@CrossOrigin(origins = "http://localhost:3000")
public class FoodItemController {
    
    @Autowired
    private FoodItemService foodItemService;
    
    // Get all food items
    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }
    
    // Get food item by ID
    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodItemById(@PathVariable Long id) {
        Optional<FoodItem> foodItem = foodItemService.getFoodItemById(id);
        return foodItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Create a new food item
    @PostMapping
    public ResponseEntity<FoodItem> createFoodItem(@RequestBody JsonNode requestBody) {
        try {
            System.out.println("Received raw JSON: " + requestBody);
            
            // Create FoodItem from JSON
            FoodItem foodItem = new FoodItem();
            foodItem.setName(requestBody.get("name").asText());
            
            if (requestBody.has("description") && !requestBody.get("description").isNull()) {
                String description = requestBody.get("description").asText();
                if (!description.trim().isEmpty()) {
                    foodItem.setDescription(description);
                }
            }
            
            if (requestBody.has("consumedDate") && !requestBody.get("consumedDate").isNull()) {
                String dateTimeStr = requestBody.get("consumedDate").asText();
                if (!dateTimeStr.trim().isEmpty()) {
                    try {
                        // Handle datetime-local format: "YYYY-MM-DDTHH:MM"
                        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
                        foodItem.setConsumedDate(dateTime);
                    } catch (Exception e) {
                        // Fallback: try to parse as date only and set to start of day
                        try {
                            LocalDate date = LocalDate.parse(dateTimeStr);
                            foodItem.setConsumedDate(date.atStartOfDay());
                        } catch (Exception ex) {
                            System.err.println("Error parsing datetime: " + dateTimeStr + ", error: " + ex.getMessage());
                            foodItem.setConsumedDate(null);
                        }
                    }
                }
            }
            
            if (requestBody.has("calorie") && !requestBody.get("calorie").isNull()) {
                int calorie = requestBody.get("calorie").asInt();
                if (calorie > 0) {
                    foodItem.setCalorie(calorie);
                }
            }
            
            if (requestBody.has("quantity") && !requestBody.get("quantity").isNull()) {
                String quantity = requestBody.get("quantity").asText();
                if (!quantity.trim().isEmpty()) {
                    foodItem.setQuantity(quantity);
                }
            }
            
            System.out.println("Parsed food item: " + foodItem);
            FoodItem savedFoodItem = foodItemService.saveFoodItem(foodItem);
            System.out.println("Saved food item: " + savedFoodItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFoodItem);
        } catch (Exception e) {
            System.err.println("Error saving food item: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Update an existing food item
    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable Long id, 
                                                   @RequestBody JsonNode requestBody) {
        try {
            FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, requestBody);
            return ResponseEntity.ok(updatedFoodItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Delete a food item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        try {
            foodItemService.deleteFoodItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Search food items by name
    @GetMapping("/search")
    public ResponseEntity<List<FoodItem>> searchFoodItems(@RequestParam String name) {
        List<FoodItem> foodItems = foodItemService.searchFoodItemsByName(name);
        return ResponseEntity.ok(foodItems);
    }
    
    // Get recent food items (last 7 days)
    @GetMapping("/recent")
    public ResponseEntity<List<FoodItem>> getRecentFoodItems() {
        List<FoodItem> recentItems = foodItemService.getRecentFoodItems();
        return ResponseEntity.ok(recentItems);
    }
    
    // Get recently consumed food items
    @GetMapping("/recently-consumed")
    public ResponseEntity<List<FoodItem>> getRecentlyConsumedItems() {
        List<FoodItem> recentlyConsumedItems = foodItemService.getRecentlyConsumedItems();
        return ResponseEntity.ok(recentlyConsumedItems);
    }
    
    // Get total count of food items
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount() {
        long count = foodItemService.getTotalCount();
        return ResponseEntity.ok(count);
    }
    
    // Export food items to CSV
    @GetMapping("/export")
    public ResponseEntity<String> exportFoodItemsToCSV(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            LocalDateTime start = null;
            LocalDateTime end = null;
            
            // Parse start date if provided
            if (startDate != null && !startDate.trim().isEmpty()) {
                try {
                    start = LocalDate.parse(startDate).atStartOfDay();
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Invalid start date format. Use YYYY-MM-DD");
                }
            }
            
            // Parse end date if provided
            if (endDate != null && !endDate.trim().isEmpty()) {
                try {
                    end = LocalDate.parse(endDate).atTime(23, 59, 59);
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Invalid end date format. Use YYYY-MM-DD");
                }
            }
            
            String csvContent = foodItemService.exportFoodItemsToCSV(start, end);
            
            // Generate filename based on date range
            String filename = "food_items";
            if (start != null && end != null) {
                filename += "_" + start.toLocalDate() + "_to_" + end.toLocalDate();
            } else if (start != null) {
                filename += "_from_" + start.toLocalDate();
            } else {
                filename += "_all_time";
            }
            filename += ".csv";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvContent);
                    
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating CSV file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }
} 