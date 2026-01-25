package com.posapp.service;

import com.posapp.dto.ProductDTO;
import com.posapp.entity.Product;
import com.posapp.exception.ResourceNotFoundException;
import com.posapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setSupplier(productDTO.getSupplier());
        product.setStock(productDTO.getStock());

        Product savedProduct = productRepository.save(product);

        return mapToDTO(savedProduct);
    }

    public ProductDTO getProductById(Integer id) {
        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        return mapToDTO(productOpt.get());
    }

    public List<ProductDTO> getAllProducts(int page, int size) {
        return productRepository.findAll(page, size)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public long getProductCount() {
        return productRepository.count();
    }

    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) {
        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        Product product = productOpt.get();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setSupplier(productDTO.getSupplier());
        product.setStock(productDTO.getStock());

        productRepository.update(product);

        return mapToDTO(product);
    }

    public void deleteProduct(Integer id) {
        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        productRepository.delete(id);
    }

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setSupplier(product.getSupplier());
        dto.setStock(product.getStock());
        return dto;
    }
}
