package com.binterpark.common;

import com.binterpark.domain.*;
import com.binterpark.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Validation {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final WishlistRepository wishlistRepository;

    //====================== ID 검증 ======================
    public User validateUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userId : " + userId));
    }

    public Book validateBookId(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 bookId : " + bookId));
    }

    public Product validateProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 productId : " + productId));
    }

    public ShoppingCart validateCartId(Long cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 cartId : " + cartId));
    }

    public Wishlist validateWishId(Long wishId) {
        return wishlistRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 wishId : " + wishId));
    }

    //====================== 수량 검증 ======================
    public int validatePositiveQuantity (int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("유효하지 않은 수량 : " + quantity);
        }
        return quantity;
    }

    public void validateLessThanStockQuantity (Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 bookId : " + bookId));
        int stockQuantity = book.getAvailableQuantity();
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("요청 수량(" + quantity + ")이 재고 수량(" + stockQuantity + ")보다 많습니다.");
        }
    }
}
