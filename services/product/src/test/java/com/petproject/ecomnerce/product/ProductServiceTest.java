package com.petproject.ecomnerce.product;

import com.petproject.ecomnerce.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock // add mock, because i dont want interact with db
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService; // testing class(object)

    @Nested // allow run this test separately
    @DisplayName("Create Product Tests")
    class createProductTests {

    private Category productCategory;
    private Product testProduct;
    private ProductRequest testProductRequest;

    @BeforeEach
    void setUp () {

        this.productCategory = Category.builder()
                .id(123)
                .name("Work")
                .description("Work related Products")
                .products(null)
                .build();

        this.testProduct = Product.builder()
                .id(321)
                .name("Test product")
                .description("Test description")
                .availableQuantity(2)
                .price(new BigDecimal("19.99"))
                .category(this.productCategory)
                .build();

        this.testProductRequest = new ProductRequest(
                321,
                "Test product",
                "Test description",
                5,
                new BigDecimal("59.99"),
                123
        );

    }

        @Test
        @DisplayName("Should create Product successfully when valid request exists")
        void shouldCreateProductSuccessfully() {

            // Given
            when(productMapper.toProduct(testProductRequest)).thenReturn(testProduct);
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);

            // When
            final Integer result  = ProductServiceTest.this.productService.createProduct(testProductRequest);

            // Then
            assertNotNull(result);
            assertEquals(321, result);
            verify(productMapper, times(1)).toProduct(testProductRequest);
            verify(productRepository, times(1)).save(testProduct);
        }
    }
}