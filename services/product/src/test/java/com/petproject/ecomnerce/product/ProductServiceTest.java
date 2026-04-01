package com.petproject.ecomnerce.product;

import com.petproject.ecomnerce.category.Category;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock // add mock, because i dont want interact with db
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks // an object that i test
    private ProductService productService;

    private Category productCategory;
    private Product testProduct;
    private ProductRequest testProductRequest;
    private ProductResponse testProductResponse;

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
                "Test ProductRequest",
                "Test description",
                5,
                new BigDecimal("59.99"),
                123
        );

        this.testProductResponse = new ProductResponse(
                456,
                "Test ProductResponse",
                "Test description",
                5,
                new BigDecimal("59.99"),
                123,
                "Work categoryName of ProductResponse",
                "Work Description of ProductResponse"
        );

    }

    @Nested // allow run this test separately
    @DisplayName("Create Product Tests")
    class createProductTests {

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

    @Nested
    @DisplayName("Find Product By Id Tests")
    class findByIdProductTests {

        @Test
        @DisplayName("Should return ProductResponse when Product exists")
        void shouldReturnProductResponseSuccessfully() {

            final Integer productId = 456;
            when(ProductServiceTest.this.productRepository.findById(productId))
                    .thenReturn(Optional.of(ProductServiceTest.this.testProduct));
            when(ProductServiceTest.this.productMapper.toProductResponse(ProductServiceTest.this.testProduct))
                    .thenReturn(ProductServiceTest.this.testProductResponse);

            final ProductResponse result = ProductServiceTest.this.productService.findById(productId);

            assertNotNull(result);
            assertEquals(ProductServiceTest.this.testProductResponse, result);
            verify(ProductServiceTest.this.productRepository).findById(productId);
            verify(ProductServiceTest.this.productMapper).toProductResponse(ProductServiceTest.this.testProduct);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when Product not found")
        void shouldThrowEntityNotFoundExceptionWhenTodoNotFound() {

            //Given
            final Integer productId = 666;
            when(ProductServiceTest.this.productRepository.findById(productId))
                    .thenReturn(Optional.empty());

            //When & Then
            final EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    ()  ->  ProductServiceTest.this.productService.findById(productId)
            );

            assertEquals("Product not found with the ID:" + productId, exception.getMessage());
            verify(ProductServiceTest.this.productRepository).findById(productId);
            verifyNoInteractions(ProductServiceTest.this.productMapper);

        }

        @Test
        @DisplayName("Should handle null Product ID")
        void shouldHandleNullId() {

            //Given
            when(ProductServiceTest.this.productRepository.findById(null))
                    .thenReturn(Optional.empty());

            //When & Then
            final EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    ()  ->  ProductServiceTest.this.productService.findById(null)
            );

            assertEquals("Product not found with the ID:null", exception.getMessage());
            verify(ProductServiceTest.this.productRepository).findById(null);
            verifyNoInteractions(ProductServiceTest.this.productMapper);

        }
    }

}