package com.binterpark.service;

import com.binterpark.common.Validation;
import com.binterpark.domain.Product;
import com.binterpark.domain.ShoppingCart;
import com.binterpark.domain.User;
import com.binterpark.repository.ProductRepository;
import com.binterpark.repository.ShoppingCartRepository;
import com.binterpark.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Validation validation;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private TestData testData;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testData = new TestData();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable!=null) {
            closeable.close();
        }
    }

    @Test
    void addToCartSuccess() {
        Long userId = testData.getValidUserId();
        Long productId = testData.getValidProductId();
        int quantity = testData.getValidQuantity();

        User user = new User();
        user.setUserId(userId);

        Product product = new Product();
        product.setProductId(productId);

        when(validation.validateUserId(userId)).thenReturn(user);
        when(validation.validateProductId(productId)).thenReturn(product);
        when(validation.validatePositiveQuantity(quantity)).thenReturn(quantity);

        shoppingCartService.addToCart(userId, productId, quantity);

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    void addToCartWithInvalidUserId() {
        Long userId = testData.getInvalidUserId();
        Long productId = testData.getValidProductId();
        int quantity = testData.getValidQuantity();

        when(validation.validateUserId(userId)).thenThrow(new IllegalArgumentException("유효하지 않은 userId : " + userId));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.addToCart(userId, productId, quantity);
        });

        assertEquals("유효하지 않은 userId : " + userId, exception.getMessage());
        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    void addToCartWithInvalidProductId() {
        Long userId = testData.getValidUserId();
        Long productId = testData.getInvalidProductId();
        int quantity = testData.getValidQuantity();

        when(validation.validateUserId(userId)).thenReturn(new User());
        when(validation.validateProductId(productId)).thenThrow(new IllegalArgumentException("유효하지 않은 productId : " + productId));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.addToCart(userId, productId, quantity);
        });

        assertEquals("유효하지 않은 productId : " + productId, exception.getMessage());
        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    void addToCartWithInvalidQuantity() {
        Long userId = testData.getValidUserId();
        Long productId = testData.getValidProductId();
        int quantity = testData.getInvalidQuantity();

        when(validation.validateUserId(userId)).thenReturn(new User());
        when(validation.validateProductId(productId)).thenReturn(new Product());
        doThrow(new IllegalArgumentException("유효하지 않은 수량 : " + quantity)).when(validation).validatePositiveQuantity(quantity);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.addToCart(userId, productId, quantity);
        });

        assertEquals("유효하지 않은 수량 : " + quantity, exception.getMessage());
        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    static class TestData {
        // 각 테스트 케이스에 필요한 테스트 데이터 정의
        public Long getValidUserId() {
            return 1L;
        }

        public Long getInvalidUserId() {
            return 999L;
        }

        public Long getValidProductId() {
            return 1L;
        }

        public Long getInvalidProductId() {
            return 999L;
        }

        public int getValidQuantity() {
            return 5;
        }

        public int getInvalidQuantity() {
            return -1;
        }
    }
}



