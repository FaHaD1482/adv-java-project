package com.posapp.controller;

import com.posapp.dto.InventoryTransactionDTO;
import com.posapp.service.InventoryService;
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
@RequestMapping("/inventory")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/restock")
    public ResponseEntity<InventoryTransactionDTO> restockProduct(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String notes,
            Authentication authentication) {

        Integer userId = (Integer) authentication.getDetails();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        InventoryTransactionDTO transaction = inventoryService.restockProduct(productId, quantity, userId, notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @PostMapping("/sell")
    public ResponseEntity<InventoryTransactionDTO> sellProduct(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String notes,
            Authentication authentication) {

        Integer userId = (Integer) authentication.getDetails();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        InventoryTransactionDTO transaction = inventoryService.sellProduct(productId, quantity, userId, notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @PostMapping("/adjust")
    public ResponseEntity<InventoryTransactionDTO> adjustStock(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String notes,
            Authentication authentication) {

        Integer userId = (Integer) authentication.getDetails();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        InventoryTransactionDTO transaction = inventoryService.adjustStock(productId, quantity, userId, notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping("/transactions")
    public ResponseEntity<Map<String, Object>> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        List<InventoryTransactionDTO> transactions = inventoryService.getTransactions(page, size);
        long totalElements = inventoryService.getTransactionCount();
        long totalPages = (totalElements + size - 1);

        Map<String, Object> response = new HashMap<>();
        response.put("content", transactions);
        response.put("pageNumber", page);
        response.put("pageSize", size);
        response.put("totalElements", totalElements);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }
}
