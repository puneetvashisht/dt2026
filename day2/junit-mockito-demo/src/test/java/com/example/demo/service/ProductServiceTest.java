package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.ProductType;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    @DisplayName("Should save valid product using Mockito verify and ArgumentCaptor")
    void saveProduct_WithValidProduct_ShouldSaveProduct() {
        // --- ARRANGE ---
        Product inputProduct = new Product("Smartphone", new BigDecimal("799.99"), ProductType.ELECTRONICS);
        Product savedProduct = new Product(1L, "Smartphone", new BigDecimal("799.99"), ProductType.ELECTRONICS);

        // Stubbing behaviour with when(...).thenReturn(...)
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // --- ACT ---
        Product result = productService.saveProduct(inputProduct);

        // --- ASSERT ---
        // Verify mock service interactions & capture argument
        verify(productRepository).save(productCaptor.capture());

        Product captured = productCaptor.getValue();
        
        // AssertJ assertions
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        
        // Demonstration of isEqualByComparingTo with Enum
        assertThat(captured.getType()).isEqualByComparingTo(ProductType.ELECTRONICS);

        // Demonstration of isEqualByComparingTo with BigDecimal (which ignores scale: 799.99 vs 799.990)
        assertThat(captured.getPrice()).isEqualByComparingTo(new BigDecimal("799.990"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when product name is empty")
    void saveProduct_WithBlankName_ShouldThrowException() {
        // --- ARRANGE ---
        Product invalidProduct = new Product("", new BigDecimal("19.99"), ProductType.BOOKS);

        // --- ACT & ASSERT ---
        // Demonstration of assertThatThrownBy
        assertThatThrownBy(() -> productService.saveProduct(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product name cannot be empty");
    }

    @Test
    @DisplayName("Should calculate correct discounted price comparing BigDecimals with scale difference")
    void calculateDiscountedPrice_WithValidDiscount_ShouldReturnDiscountedPrice() {
        // --- ARRANGE ---
        Long productId = 10L;
        Product product = new Product(productId, "Java Book", new BigDecimal("100.00"), ProductType.BOOKS);

        // Stubbing behavior
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // --- ACT ---
        BigDecimal discountedPrice = productService.calculateDiscountedPrice(productId, new BigDecimal("15"));

        // --- ASSERT ---
        // Verify discount calculations
        // BigDecimal scale of 100.00 * 15 / 100 might result in 85.0000 etc.
        // isEqualByComparingTo compares the value ignoring the scale.
        assertThat(discountedPrice).isEqualByComparingTo(new BigDecimal("85"));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product does not exist")
    void calculateDiscountedPrice_WithNonExistentProduct_ShouldThrowException() {
        // --- ARRANGE ---
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        assertThatThrownBy(() -> productService.calculateDiscountedPrice(productId, new BigDecimal("10")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found with id: 999");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when discount percentage is out of range")
    void calculateDiscountedPrice_WithInvalidDiscountPercentage_ShouldThrowException() {
        // --- ARRANGE ---
        Long productId = 1L;

        // --- ACT & ASSERT ---
        assertThatThrownBy(() -> productService.calculateDiscountedPrice(productId, new BigDecimal("105")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount percentage must be between 0 and 100");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when product is null (Null Check Demo)")
    void getProductCategoryFormatted_WithNullProduct_ShouldThrowException() {
        // Act & Assert
        assertThatThrownBy(() -> productService.getProductCategoryFormatted(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product cannot be null");
    }

    @Test
    @DisplayName("Should return default category when product type is null (Optional Demo)")
    void getProductCategoryFormatted_WithNullType_ShouldReturnDefault() {
        // Arrange
        Product product = new Product("Uncategorized Product", new BigDecimal("9.99"), null);

        // Act
        String category = productService.getProductCategoryFormatted(product);

        // Assert
        assertThat(category).isEqualTo("Category: Standard / Food");
    }

    @Test
    @DisplayName("Should return default category when product type is FOOD (Optional Filter Demo)")
    void getProductCategoryFormatted_WithFoodType_ShouldReturnDefault() {
        // Arrange
        Product product = new Product("Apple", new BigDecimal("1.50"), ProductType.FOOD);

        // Act
        String category = productService.getProductCategoryFormatted(product);

        // Assert
        assertThat(category).isEqualTo("Category: Standard / Food");
    }

    @Test
    @DisplayName("Should return formatted category when product type is ELECTRONICS (Optional Map Demo)")
    void getProductCategoryFormatted_WithElectronicsType_ShouldReturnCategoryName() {
        // Arrange
        Product product = new Product("Laptop", new BigDecimal("999.99"), ProductType.ELECTRONICS);

        // Act
        String category = productService.getProductCategoryFormatted(product);

        // Assert
        assertThat(category).isEqualTo("Category: ELECTRONICS");
    }

    @Test
    @DisplayName("Should return empty Optional when ID is null")
    void findProductByIdOptional_WithNullId_ShouldReturnEmptyOptional() {
        // Act
        Optional<Product> result = productService.findProductByIdOptional(null);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return product wrapped in Optional when product exists")
    void findProductByIdOptional_WithExistingId_ShouldReturnProductOptional() {
        // Arrange
        Long productId = 5L;
        Product product = new Product(productId, "T-Shirt", new BigDecimal("25.00"), ProductType.CLOTHING);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Optional<Product> result = productService.findProductByIdOptional(productId);

        // Assert
        assertThat(result).isPresent().contains(product);
    }
}
