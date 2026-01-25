package com.posapp.controller;

import com.posapp.dto.ProductDTO;
import com.posapp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO, Authentication authentication) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Integer id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ProductDTO> products = productService.getAllProducts(page, size);
        long totalElements = productService.getProductCount();
        long totalPages = (totalElements + size - 1) / size;

        Map<String, Object> response = new HashMap<>();
        response.put("content", products);
        response.put("pageNumber", page);
        response.put("pageSize", size);
        response.put("totalElements", totalElements);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductDTO productDTO,
            Authentication authentication) {

        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id, Authentication authentication) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
