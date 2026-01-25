package com.posapp.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class InventoryTransactionDTO {
    private Integer id;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    private String productName;
    private Integer userId;

    @NotNull(message = "Transaction type is required")
    private String type;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private String notes;
    private LocalDateTime transAt;

    public InventoryTransactionDTO() {}

    public InventoryTransactionDTO(Integer id, Integer productId, String productName, Integer userId, String type, Integer quantity, String notes, LocalDateTime transAt) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.userId = userId;
        this.type = type;
        this.quantity = quantity;
        this.notes = notes;
        this.transAt = transAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getTransAt() { return transAt; }
    public void setTransAt(LocalDateTime transAt) { this.transAt = transAt; }
}
