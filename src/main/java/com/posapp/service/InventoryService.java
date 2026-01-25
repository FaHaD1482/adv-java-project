package com.posapp.service;

import com.posapp.dto.InventoryTransactionDTO;
import com.posapp.entity.InventoryTransaction;
import com.posapp.entity.Product;
import com.posapp.exception.BadRequestException;
import com.posapp.exception.ResourceNotFoundException;
import com.posapp.repository.InventoryTransactionRepository;
import com.posapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private InventoryTransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    public InventoryTransactionDTO restockProduct(Integer productId, Integer quantity, Integer userId, String notes) {
        if (quantity <= 0) {
            throw new BadRequestException("Restock quantity must be greater than 0");
        }

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        Product product = productOpt.get();
        product.setStock(product.getStock() + quantity);
        productRepository.updateStock(productId, product.getStock());

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(productId);
        transaction.setUserId(userId);
        transaction.setType("RESTOCK");
        transaction.setQuantity(quantity);
        transaction.setNotes(notes);

        InventoryTransaction savedTransaction = transactionRepository.save(transaction);

        return mapToDTO(savedTransaction, product.getName());
    }

    public InventoryTransactionDTO sellProduct(Integer productId, Integer quantity, Integer userId, String notes) {
        if (quantity <= 0) {
            throw new BadRequestException("Sale quantity must be greater than 0");
        }

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        Product product = productOpt.get();

        if (product.getStock() < quantity) {
            throw new BadRequestException("Insufficient stock. Available: " + product.getStock() + ", Requested: " + quantity);
        }

        product.setStock(product.getStock() - quantity);
        productRepository.updateStock(productId, product.getStock());

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(productId);
        transaction.setUserId(userId);
        transaction.setType("SALE");
        transaction.setQuantity(-quantity);
        transaction.setNotes(notes);

        InventoryTransaction savedTransaction = transactionRepository.save(transaction);

        return mapToDTO(savedTransaction, product.getName());
    }

    public InventoryTransactionDTO adjustStock(Integer productId, Integer quantity, Integer userId, String notes) {
        if (quantity == 0) {
            throw new BadRequestException("Adjustment quantity cannot be 0");
        }

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        Product product = productOpt.get();
        int newStock = product.getStock() + quantity;

        if (newStock < 0) {
            throw new BadRequestException("Adjustment would result in negative stock");
        }

        product.setStock(newStock);
        productRepository.updateStock(productId, newStock);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(productId);
        transaction.setUserId(userId);
        transaction.setType("ADJUST");
        transaction.setQuantity(quantity);
        transaction.setNotes(notes);

        InventoryTransaction savedTransaction = transactionRepository.save(transaction);

        return mapToDTO(savedTransaction, product.getName());
    }

    public List<InventoryTransactionDTO> getTransactions(int page, int size) {
        List<InventoryTransaction> transactions = transactionRepository.findAll(page, size);

        return transactions.stream()
                .map(trans -> {
                    Optional<Product> productOpt = productRepository.findById(trans.getProductId());
                    String productName = productOpt.isPresent() ? productOpt.get().getName() : "Unknown";
                    return mapToDTO(trans, productName);
                })
                .collect(Collectors.toList());
    }

    public long getTransactionCount() {
        return transactionRepository.count();
    }

    private InventoryTransactionDTO mapToDTO(InventoryTransaction transaction, String productName) {
        InventoryTransactionDTO dto = new InventoryTransactionDTO();
        dto.setId(transaction.getId());
        dto.setProductId(transaction.getProductId());
        dto.setProductName(productName);
        dto.setUserId(transaction.getUserId());
        dto.setType(transaction.getType());
        dto.setQuantity(transaction.getQuantity());
        dto.setNotes(transaction.getNotes());
        dto.setTransAt(transaction.getTransAt());
        return dto;
    }
}
