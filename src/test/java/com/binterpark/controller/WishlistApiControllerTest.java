package com.binterpark.controller;

import com.binterpark.config.TestSecurityConfig;
import com.binterpark.dto.WishResponseDto;
import com.binterpark.service.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WishlistApiController.class)
@Import(TestSecurityConfig.class)
class WishlistApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistApiController wishlistApiController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("찜 추가 성공 테스트")
    void addToWishTest() throws Exception {
        // Given
        Long userId = 1L;
        Long productId = 2L;

        WishResponseDto wishlist = new WishResponseDto();
        wishlist.setWishId(1L);
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);
        wishlist.setDateAdded(LocalDateTime.now());

        given(wishlistService.addToWish(userId, productId)).willReturn(wishlist);

        mockMvc.perform(post("/api/wishlist/add")
                        .param("userId", userId.toString())
                        .param("productId", productId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wishId").value(1L))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.productId").value(productId));
    }

    @Test
    @DisplayName("찜 추가 실패 테스트_잘못된 유저")
    void addToWishWithInvalidUser() throws Exception {
        // Given
        Long userId = 1L;
        Long productId = 2L;

        WishResponseDto wishlist = new WishResponseDto();
        wishlist.setWishId(1L);
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);
        wishlist.setDateAdded(LocalDateTime.now());

        given(wishlistService.addToWish(userId, productId)).willThrow(new IllegalArgumentException("유효하지 않은 유저 : " + userId));

        mockMvc.perform(post("/api/wishlist/add")
                        .param("userId", userId.toString())
                        .param("productId", productId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("유효하지 않은 유저 : " + userId));
    }

    @Test
    @DisplayName("찜 추가 실패 테스트_잘못된 상품")
    void addToWishWithInvalidProduct() throws Exception {
        // Given
        Long userId = 1L;
        Long productId = 2L;

        WishResponseDto wishlist = new WishResponseDto();
        wishlist.setWishId(1L);
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);
        wishlist.setDateAdded(LocalDateTime.now());

        given(wishlistService.addToWish(userId, productId)).willThrow(new IllegalArgumentException("유효하지 않은 상품 : " + productId));

        mockMvc.perform(post("/api/wishlist/add")
                        .param("userId", userId.toString())
                        .param("productId", productId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("유효하지 않은 상품 : " + productId));
    }

    @Test
    @DisplayName("찜 추가 실패 테스트_중복 상품")
    void addToWishWithDuplicateProduct() throws Exception {
        // Given
        Long userId = 1L;
        Long productId = 2L;

        WishResponseDto wishlist = new WishResponseDto();
        wishlist.setWishId(1L);
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);
        wishlist.setDateAdded(LocalDateTime.now());

        given(wishlistService.addToWish(userId, productId)).willThrow(new IllegalArgumentException("상품이 이미 찜에 등록되어 있습니다. : " + productId));

        mockMvc.perform(post("/api/wishlist/add")
                        .param("userId", userId.toString())
                        .param("productId", productId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("상품이 이미 찜에 등록되어 있습니다. : " + productId));
    }

    @Test
    @DisplayName("찜 목록 가져오기 성공 테스트")
    void getWishListTest() throws Exception {
        Long userId = 1L;
        List<WishResponseDto> wishlists = Arrays.asList(new WishResponseDto(1L, userId, 2L, LocalDateTime.now()));

        given(wishlistService.getWishList(userId)).willReturn(wishlists);

        mockMvc.perform(get("/api/wishlist")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].wishId").value(1L));
    }

    @Test
    @DisplayName("찜 목록 가져오기 실패 테스트_잘못된 유저")
    void getWishListWithInvalidUser() throws Exception {
        Long userId = 1L;

        given(wishlistService.getWishList(userId)).willThrow(new IllegalArgumentException("유효하지 않은 유저 : " + userId));

        mockMvc.perform(get("/api/wishlist")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("유효하지 않은 유저 : " + userId));
    }

    @Test
    @DisplayName("찜 상품 삭제 성공 테스트")
    void removeItemFromWishTest() throws Exception {
        Long wishId = 1L;
        Long productId = 2L;

        given(wishlistService.removeItemFromWish(wishId, productId)).willReturn(true);

        mockMvc.perform(delete("/api/wishlist/remove")
                        .param("wishId", wishId.toString())
                        .param("productId", productId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("상품이 찜에서 삭제되었습니다."));
    }




}