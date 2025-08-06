package com.foodkeeper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "food_items")
public class FoodItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Food name is required")
    @Size(max = 100, message = "Food name must not exceed 100 characters")
    @Column(nullable = false)
    private String name;
    
    @Column(name = "created_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Column(name = "consumed_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime consumedDate;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @Column(name = "calorie")
    @Min(value = 0, message = "Calories must be a positive number")
    private Integer calorie;
    
    @Column(name = "quantity")
    @Size(max = 50, message = "Quantity must not exceed 50 characters")
    private String quantity;
    
    // User association
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    // Constructors
    public FoodItem() {
        this.createdAt = LocalDateTime.now();
    }
    
    public FoodItem(String name) {
        this();
        this.name = name;
    }
    
    public FoodItem(String name, String description) {
        this(name);
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getConsumedDate() {
        return consumedDate;
    }
    
    public void setConsumedDate(LocalDateTime consumedDate) {
        this.consumedDate = consumedDate;
    }
    

    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getCalorie() {
        return calorie;
    }
    
    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }
    
    public String getQuantity() {
        return quantity;
    }
    
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "FoodItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", consumedDate=" + consumedDate +
                ", description='" + description + '\'' +
                ", calorie=" + calorie +
                ", quantity='" + quantity + '\'' +
                '}';
    }
} 