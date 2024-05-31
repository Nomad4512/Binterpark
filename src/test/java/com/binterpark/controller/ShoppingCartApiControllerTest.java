package com.binterpark.controller;

import com.binterpark.config.TestSecurityConfig;
import com.binterpark.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ShoppingCartApiController.class)
@Import(TestSecurityConfig.class)
public class ShoppingCartApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private ShoppingCartApiController shoppingCartApiController;

    @Autowired
    private ObjectMapper objectMapper;

    private TestData testData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testData = new TestData();
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingCartApiController)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // UTF-8 필터 추가
                .build();
    }

    @Test
    void addToCartSuccess() throws Exception {
        Long userId = testData.getValidUserId();
        Long productId = testData.getValidProductId();
        int quantity = testData.getValidQuantity();

        mockMvc.perform(post("/api/cart/add")
                        .param("userId", userId.toString())
                        .param("productId", productId.toString())
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(shoppingCartService, times(1)).addToCart(userId, productId, quantity);
    }

    @Test
    void addToCartWithInvalidUserId() throws Exception {
        Long userId = testData.getInvalidUserId();
        Long productId = testData.getValidProductId();
        int quantity = testData.getValidQuantity();

        doThrow(new IllegalArgumentException("유효하지 않은 사용자 ID: " + userId))
                .when(shoppingCartService).addToCart(userId, productId, quantity);

        mockMvc.perform(post("/api/cart/add")
                        .param("userId", userId.toString())
                        .param("productId", productId.toString())
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("유효하지 않은 사용자 ID: " + userId));

        verify(shoppingCartService, times(1)).addToCart(userId, productId, quantity);
    }

    @Test
    void addToCartWithInvalidProductId() throws Exception {
        Long userId = testData.getValidUserId();
        Long productId = testData.getInvalidProductId();
        int quantity = testData.getValidQuantity();

        doThrow(new IllegalArgumentException("유효하지 않은 제품 ID: " + productId))
                .when(shoppingCartService).addToCart(userId, productId, quantity);

        mockMvc.perform(post("/api/cart/add")
                        .param("userId", userId.toString())
                        .param("productId", productId.toString())
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("유효하지 않은 제품 ID: " + productId));

        verify(shoppingCartService, times(1)).addToCart(userId, productId, quantity);
    }

    @Test
    void addToCartWithInvalidQuantity() throws Exception {
        Long userId = testData.getValidUserId();
        Long productId = testData.getValidProductId();
        int quantity = testData.getInvalidQuantity();

        doThrow(new IllegalArgumentException("유효하지 않은 수량: " + quantity))
                .when(shoppingCartService).addToCart(userId, productId, quantity);

        mockMvc.perform(post("/api/cart/add")
                        .param("userId", userId.toString())
                        .param("productId", productId.toString())
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("유효하지 않은 수량: " + quantity));

        verify(shoppingCartService, times(1)).addToCart(userId, productId, quantity);
    }

    @Test
    void addToCartWithServerError() throws Exception {
        Long userId = testData.getValidUserId();
        Long productId = testData.getValidProductId();
        int quantity = testData.getValidQuantity();

        doThrow(new RuntimeException("서버 오류"))
                .when(shoppingCartService).addToCart(userId, productId, quantity);

        mockMvc.perform(post("/api/cart/add")
                        .param("userId", userId.toString())
                        .param("productId", productId.toString())
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(shoppingCartService, times(1)).addToCart(userId, productId, quantity);
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