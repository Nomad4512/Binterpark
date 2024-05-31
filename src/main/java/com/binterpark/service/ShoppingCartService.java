package com.binterpark.service;

import com.binterpark.common.Validation;
import com.binterpark.domain.Product;
import com.binterpark.domain.ShoppingCart;
import com.binterpark.domain.User;
import com.binterpark.dto.ShoppingCartResponseDto;
import com.binterpark.repository.ProductRepository;
import com.binterpark.repository.ShoppingCartRepository;
import com.binterpark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Validation validation;

    public ShoppingCart addToCart(Long userId, Long productId, int quantity) {
        User user = validation.validateUserId(userId);
        Product product = validation.validateProductId(productId);
        validation.validatePositiveQuantity(quantity);

        ShoppingCart item = new ShoppingCart();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setDateAdded(LocalDateTime.now());

        return shoppingCartRepository.save(item);
    }

    public List<ShoppingCartResponseDto> getCartItems(Long userId) {

        List<ShoppingCart> cart = shoppingCartRepository.findByUserId(userId);
        return cart.stream()
                .map(this::convertCartInfoToDto)
                .collect(Collectors.toList());
    }

    public void removeFromCart(Long cartId, Long productId) {

        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cart ID : " + cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID : " + productId));

        shoppingCartRepository.deleteItemFromCart(cartId, productId);
    }

    private ShoppingCartResponseDto convertCartInfoToDto (ShoppingCart cart) {

        ShoppingCartResponseDto dto = new ShoppingCartResponseDto();
        dto.setCartId(cart.getCartId());
        dto.setUserId(cart.getUser().getUserId());
        dto.setProduct(cart.getProduct());
        dto.setQuantity(cart.getQuantity());
        dto.setDateAdded(cart.getDateAdded());

        return dto;
    }

}
