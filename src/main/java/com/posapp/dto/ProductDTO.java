package com.posapp.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductDTO {
    private Integer id;

    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 100, message = "Product name must be between 1 and 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @Size(max = 100, message = "Supplier must not exceed 100 characters")
    private String supplier;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    public ProductDTO() {}

    public ProductDTO(Integer id, String name, String description, BigDecimal price, String category, String supplier, Integer stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.supplier = supplier;
        this.stock = stock;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
