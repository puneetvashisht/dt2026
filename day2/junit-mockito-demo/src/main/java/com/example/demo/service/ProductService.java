package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.ProductType;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price must be non-negative");
        }
        if (product.getType() == null) {
            throw new IllegalArgumentException("Product type cannot be null");
        }
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public BigDecimal calculateDiscountedPrice(Long id, BigDecimal discountPercentage) {
        if (discountPercentage == null || discountPercentage.compareTo(BigDecimal.ZERO) < 0 || discountPercentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        Product product = getProductById(id);
        BigDecimal discountAmount = product.getPrice().multiply(discountPercentage).divide(BigDecimal.valueOf(100));
        return product.getPrice().subtract(discountAmount);
    }

    public String getProductCategoryFormatted(Product product) {
        // Traditional Null Check
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        // Optional creation, filtering, mapping and fallback (default value)
        return Optional.ofNullable(product.getType())
                .filter(type -> type != ProductType.FOOD)
                .map(type -> "Category: " + type.name())
                .orElse("Category: Standard / Food");
    }

    public Optional<Product> findProductByIdOptional(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return productRepository.findById(id);
    }
}
