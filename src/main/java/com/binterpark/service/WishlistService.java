package com.binterpark.service;

import com.binterpark.common.Validation;
import com.binterpark.domain.Product;
import com.binterpark.domain.User;
import com.binterpark.domain.Wishlist;
import com.binterpark.dto.WishResponseDto;
import com.binterpark.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final Validation validation;
    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    public WishResponseDto addToWish (Long userId, Long productId) {
        User user = validation.validateUserId(userId);
        Product product = validation.validateProductId(productId);

        boolean isProductAlreadyAdded = wishlistRepository.existsByUser_UserIdAndProduct_ProductId(userId,productId);
        if (isProductAlreadyAdded) {
            throw new IllegalArgumentException("이미 등록된 상품 ID : " + productId);
        }

        Wishlist item = new Wishlist();
        item.setUser(user);
        item.setProduct(product);
        item.setDateAdded(LocalDateTime.now());

        wishlistRepository.save(item);
        return convertToDto(item);
    }

    public List<WishResponseDto> getWishList(Long userId) {
        validation.validateUserId(userId);
        List<Wishlist> items = wishlistRepository.findByUser_UserId(userId);
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public boolean removeItemFromWish(Long wishId, Long productId) {

        Wishlist wishlist = wishlistRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 찜 ID : " + wishId));
        validation.validateProductId(productId);

        boolean isProductAdded = wishlistRepository.existsByUser_UserIdAndProduct_ProductId(wishlist.getUser().getUserId(), productId);
        if (!isProductAdded) {
            throw new IllegalArgumentException("상품이 찜에 존재하지 않습니다. ID : " + productId);
        }

        try {
            wishlistRepository.deleteItemFromWish(wishId, productId);
            return true;
        } catch (Exception e){
            logger.error("찜에서 상품을 삭제 중 에러가 발생했습니다.", e);
            return false;
        }
    }

    private WishResponseDto convertToDto(Wishlist wishlist) {
        return new WishResponseDto(
                wishlist.getWishId(),
                wishlist.getUser().getUserId(),
                wishlist.getProduct().getProductId(),
                wishlist.getDateAdded()
        );
    }
}
