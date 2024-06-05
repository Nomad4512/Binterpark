package com.binterpark.service;

import com.binterpark.common.Validation;
import com.binterpark.domain.Product;
import com.binterpark.domain.User;
import com.binterpark.domain.Wishlist;
import com.binterpark.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private Validation validation;

    @InjectMocks
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("찜 담기 성공 테스트")
    void addToWishTest(){
        // Given
        Long userId = 1L;
        Long productId = 2L;

        User user = new User();
        user.setUserId(userId);

        Product product = Product.builder()
                .productId(productId).build();

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlist.setDateAdded(LocalDateTime.now());

        // When
        when(validation.validateUserId(userId)).thenReturn(user);
        when(validation.validateProductId(productId)).thenReturn(product);
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        Wishlist result = wishlistService.addToWish(userId, productId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUser().getUserId());
        assertEquals(productId, result.getProduct().getProductId());
        verify(wishlistRepository, times(1)).save(any(Wishlist.class));
    }


    @Test
    @DisplayName("찜 담기 실패 테스트_잘못된 유저")
    void addToWishWithInvalidUser(){
        // Given
        Long userId = 0L;
        Long productId = 2L;

        // When
        when(validation.validateUserId(userId)).thenThrow(new IllegalArgumentException("유효하지 않은 유저 ID : " + userId));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.addToWish(userId, productId);
        });

        // Then
        assertEquals("유효하지 않은 유저 ID : " + userId, exception.getMessage());
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("찜 담기 실패 테스트_잘못된 상품")
    void addToWishWithInvalidProduct(){
        // Given
        Long userId = 5L;
        Long productId = null;

        // When
        when(validation.validateProductId(productId)).thenThrow(new IllegalArgumentException("유효하지 않은 상품 ID : " + productId));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.addToWish(userId, productId);
        });

        // Then
        assertEquals("유효하지 않은 상품 ID : " + productId, exception.getMessage());
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("찜 담기 실패 테스트_중복된 상품")
    void addToWishAlreadyAddedProduct(){
        // Given
        Long userId = 5L;
        Long productId = 2L;

        User user = new User();
        Product product = Product.builder()
                .productId(productId).build();

        // When
        when(validation.validateUserId(userId)).thenReturn(user);
        when(validation.validateProductId(productId)).thenReturn(product);

        when(wishlistRepository.existsByUser_UserIdAndProduct_ProductId(userId,productId)).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.addToWish(userId, productId);
        });

        // Then
        assertEquals("이미 등록된 상품 ID : " + productId, exception.getMessage());
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("찜 목록 가져오기 성공 테스트")
    void getWishList() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);

        Product product1 = Product.builder()
                .productId(101L).build();
        Product product2 = Product.builder()
                .productId(102L).build();

        Wishlist wishlist1 = new Wishlist();
        wishlist1.setUser(user);
        wishlist1.setProduct(product1);
        wishlist1.setDateAdded(LocalDateTime.now());

        Wishlist wishlist2 = new Wishlist();
        wishlist2.setUser(user);
        wishlist2.setProduct(product2);
        wishlist2.setDateAdded(LocalDateTime.now());

        List<Wishlist> wishlists = Arrays.asList(wishlist1, wishlist2);

        // When
        when(wishlistRepository.findByUser_UserId(userId)).thenReturn(wishlists);
        List<Wishlist> result = wishlistService.getWishList(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(101L, result.get(0).getProduct().getProductId());
        assertEquals(102L, result.get(1).getProduct().getProductId());
        verify(wishlistRepository, times(1)).findByUser_UserId(userId);
    }

    @Test
    @DisplayName("찜 목록 가져오기 실패 테스트_잘못된 유저")
    void getWishListWithInvalidUser() {
        // Given
        Long invalidUserId = 103L;

        // When
        when(validation.validateUserId(invalidUserId))
                .thenThrow(new IllegalArgumentException("잘못된 유저입니다 ID : " + invalidUserId));

        // Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.getWishList(invalidUserId);
        });

        assertEquals("잘못된 유저입니다 ID : " + invalidUserId, exception.getMessage());
        verify(wishlistRepository, never()).findByUser_UserId(any(Long.class));
    }

    @Test
    @DisplayName("찜 목록에서 상품 삭제 성공 테스트")
    void removeItemFromWish_Success() {
        // Given
        Long wishId = 1L;
        Long productId = 2L;
        User user = new User();
        user.setUserId(1L);
        Wishlist wishlist = new Wishlist();
        wishlist.setWishId(wishId);
        wishlist.setUser(user);
        Product product = Product.builder()
                .productId(productId).build();
        wishlist.setProduct(product);

        // When
        when(wishlistRepository.findById(wishId)).thenReturn(Optional.of(wishlist));
        when(validation.validateProductId(productId)).thenReturn(product);
        when(wishlistRepository.existsByUser_UserIdAndProduct_ProductId(user.getUserId(), productId)).thenReturn(true);
        doNothing().when(wishlistRepository).deleteItemFromWish(wishId, productId);

        boolean result = wishlistService.removeItemFromWish(wishId, productId);

        // Then
        assertTrue(result);
        verify(wishlistRepository, times(1)).findById(wishId);
        verify(validation, times(1)).validateProductId(productId);
        verify(wishlistRepository, times(1)).existsByUser_UserIdAndProduct_ProductId(user.getUserId(), productId);
        verify(wishlistRepository, times(1)).deleteItemFromWish(wishId, productId);
    }

    @Test
    @DisplayName("찜 목록에서 상품 삭제 실패 테스트_잘못된 찜 ID")
    void removeItemFromWish_InvalidWishId() {
        // Given
        Long wishId = 1L;
        Long productId = 2L;

        // When
        when(wishlistRepository.findById(wishId)).thenReturn(Optional.empty());

        // Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.removeItemFromWish(wishId, productId);
        });

        assertEquals("잘못된 찜 ID : " + wishId, exception.getMessage());
        verify(wishlistRepository, times(1)).findById(wishId);
        verify(wishlistRepository, never()).deleteItemFromWish(anyLong(), anyLong());
    }

    @Test
    @DisplayName("찜 목록에서 상품 삭제 실패 테스트_잘못된 상품 ID")
    void removeItemFromWish_InvalidProductId() {
        // Given
        Long wishId = 1L;
        Long productId = 2L;
        User user = new User();
        user.setUserId(1L);
        Wishlist wishlist = new Wishlist();
        wishlist.setWishId(wishId);
        wishlist.setUser(user);
        Product product = Product.builder()
                .productId(productId).build();
        wishlist.setProduct(product);

        // When
        when(wishlistRepository.findById(wishId)).thenReturn(Optional.of(wishlist));
        doThrow(new IllegalArgumentException("유효하지 않은 상품 ID : " + productId))
                .when(validation).validateProductId(productId);

        // Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.removeItemFromWish(wishId, productId);
        });

        assertEquals("유효하지 않은 상품 ID : " + productId, exception.getMessage());
        verify(wishlistRepository, times(1)).findById(wishId);
        verify(validation, times(1)).validateProductId(productId);
        verify(wishlistRepository, never()).deleteItemFromWish(anyLong(), anyLong());
    }

    @Test
    @DisplayName("찜 목록에서 상품 삭제 실패 테스트_상품이 찜에 존재하지 않음")
    void removeItemFromWish_ProductNotInWish() {
        // Given
        Long wishId = 1L;
        Long productId = 2L;
        User user = new User();
        user.setUserId(1L);
        Wishlist wishlist = new Wishlist();
        wishlist.setWishId(wishId);
        wishlist.setUser(user);
        Product product = Product.builder()
                .productId(productId).build();
        wishlist.setProduct(product);

        // When
        when(wishlistRepository.findById(wishId)).thenReturn(Optional.of(wishlist));
        when(validation.validateProductId(productId)).thenReturn(product);
        when(wishlistRepository.existsByUser_UserIdAndProduct_ProductId(user.getUserId(), productId)).thenReturn(false);

        // Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.removeItemFromWish(wishId, productId);
        });

        assertEquals("상품이 찜에 존재하지 않습니다. ID : " + productId, exception.getMessage());
        verify(wishlistRepository, times(1)).findById(wishId);
        verify(validation, times(1)).validateProductId(productId);
        verify(wishlistRepository, times(1)).existsByUser_UserIdAndProduct_ProductId(user.getUserId(), productId);
        verify(wishlistRepository, never()).deleteItemFromWish(anyLong(), anyLong());
    }


}