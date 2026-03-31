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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock // add mock, because i dont want interact with db
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService productService; // testing class(object)

    @Nested // allow run this test separately
    @DisplayName("Create Product Tests")
    class createProductTests {

    private Category productCategory;
    private Product testProduct;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp () {

        //todo
//        this.productCategory = Category.builder()
//                .id(123)
//                .name("Work")
//                .description("Work related ")
//                .build()

//        this.testProduct = Product.builder()
//                .
//                .build();

    }

        @Test
        @DisplayName("Should create Product successfully when valid request exists")
        void shouldCreateProductSuccessfully() {
            // Given

            // When

            // Then
        }
    }
}