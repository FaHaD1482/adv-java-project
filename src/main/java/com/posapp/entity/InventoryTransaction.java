package com.posapp.entity;

import java.time.LocalDateTime;

public class InventoryTransaction {
    private Integer id;
    private Integer productId;
    private Integer userId;
    private String type;
    private Integer quantity;
    private String notes;
    private LocalDateTime transAt;

    public InventoryTransaction() {}

    public InventoryTransaction(Integer id, Integer productId, Integer userId, String type, Integer quantity, String notes, LocalDateTime transAt) {
        this.id = id;
        this.productId = productId;
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
