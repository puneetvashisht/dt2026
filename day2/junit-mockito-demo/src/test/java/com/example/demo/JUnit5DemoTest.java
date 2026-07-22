package com.example.demo;

import com.example.demo.model.ProductType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("JUnit 5 Core Features Demonstration")
class JUnit5DemoTest {

    private List<String> sampleList;

    @BeforeAll
    static void setUpAll() {
        System.out.println("--- @BeforeAll: Executed once before all tests in this class ---");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("--- @AfterAll: Executed once after all tests in this class ---");
    }

    @BeforeEach
    void setUp() {
        System.out.println("@BeforeEach: Setup before each test method");
        sampleList = new ArrayList<>();
        sampleList.add("JUnit5");
        sampleList.add("Mockito");
    }

    @AfterEach
    void tearDown() {
        System.out.println("@AfterEach: Cleanup after each test method");
        sampleList.clear();
    }

    @Test
    @DisplayName("Basic Assertions (Standard JUnit 5 vs AssertJ)")
    void basicAssertionsDemo() {
        // --- ARRANGE ---
        String target = "Java Development";

        // --- ACT ---
        int length = target.length();

        // --- ASSERT ---
        // 1. Standard JUnit 5 assertions
        assertEquals(16, length);
        assertNotNull(target);
        assertTrue(target.startsWith("Java"));

        // 2. Grouped Assertions (JUnit 5 assertAll)
        assertAll("String Properties",
                () -> assertEquals(16, length),
                () -> assertNotNull(target),
                () -> assertTrue(target.startsWith("Java"))
        );

        // 3. AssertJ Assertions (Fluent & readable)
        assertThat(target)
                .isNotNull()
                .hasSize(16)
                .startsWith("Java")
                .contains("Development");
    }

    @Nested
    @DisplayName("Nested Test Class for Comparable & Exception Assertions")
    class AdvancedAssertions {

        @Test
        @DisplayName("Demonstrating isEqualByComparingTo")
        void compareToAssertionDemo() {
            // --- ARRANGE & ACT ---
            BigDecimal val1 = new BigDecimal("10.0");
            BigDecimal val2 = new BigDecimal("10.00"); // Different scale

            ProductType type1 = ProductType.ELECTRONICS;

            // --- ASSERT ---
            // Using standard equals on BigDecimal fails because of scale difference:
            // assertThat(val1).isEqualTo(val2); // This would FAIL!
            
            // isEqualByComparingTo uses compareTo() under the hood, making it scale-independent!
            assertThat(val1).isEqualByComparingTo(val2); // Passes!
            assertThat(val1).isEqualByComparingTo("10");  // Also parses string comparison
            
            // Comparing enums
            assertThat(type1).isEqualByComparingTo(ProductType.ELECTRONICS);
        }

        @Test
        @DisplayName("Demonstrating assertThatThrownBy for Exception Verification")
        void exceptionAssertionDemo() {
            // --- ACT & ASSERT ---
            assertThatThrownBy(() -> {
                throw new IllegalArgumentException("Invalid rating value: 105");
            })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid rating value: 105")
            .hasMessageContaining("rating");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8, 10})
    @DisplayName("Parameterized Test with Even Numbers")
    void parameterizedTestDemo(int number) {
        // --- ACT & ASSERT ---
        assertEquals(0, number % 2, () -> number + " should be even");
        assertThat(number % 2).isZero();
    }

    @ParameterizedTest
    @EnumSource(ProductType.class)
    @DisplayName("Parameterized Test with ProductType Enums")
    void enumParameterizedTestDemo(ProductType type) {
        // --- ACT & ASSERT ---
        assertNotNull(type);
        assertThat(type).isIn((Object[]) ProductType.values());
    }
}
