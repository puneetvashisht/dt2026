package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.model.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class ProductRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should save and retrieve a product from containerized PostgreSQL database")
    void shouldSaveAndRetrieveProductFromPostgres() {
        // --- ARRANGE ---
        Product product = new Product("Mechanical Keyboard", new BigDecimal("129.99"), ProductType.ELECTRONICS);

        // --- ACT ---
        Product savedProduct = productRepository.save(product);

        // --- ASSERT ---
        assertThat(savedProduct.getId()).isNotNull();
        
        Optional<Product> foundProductOpt = productRepository.findById(savedProduct.getId());
        assertThat(foundProductOpt).isPresent();
        
        Product foundProduct = foundProductOpt.get();
        assertThat(foundProduct.getName()).isEqualTo("Mechanical Keyboard");
        
        // AssertJ isEqualByComparingTo works well with numeric types like BigDecimals
        assertThat(foundProduct.getPrice()).isEqualByComparingTo(new BigDecimal("129.99"));
        assertThat(foundProduct.getType()).isEqualByComparingTo(ProductType.ELECTRONICS);
    }
}
