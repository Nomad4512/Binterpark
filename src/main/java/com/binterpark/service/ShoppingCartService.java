package com.binterpark.service;

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

    public void addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));

        ShoppingCart item = new ShoppingCart();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setDateAdded(LocalDateTime.now());

        shoppingCartRepository.save(item);
    }

    public List<ShoppingCartResponseDto> getCartItems(Long userId) {

        List<ShoppingCart> cart = shoppingCartRepository.findByUserId(userId);
        return cart.stream()
                .map(this::convertCartInfoToDto)
                .collect(Collectors.toList());
    }

    public void removeFromCart(Long cartId, Long productId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cart Id : " + cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id : " + productId));
        shoppingCartRepository.deleteItemFromCart(cart.getCartId(),product.getProductId());
    }

    private ShoppingCartResponseDto convertCartInfoToDto (ShoppingCart cart) {
        User user = new User();
        user.setUserId(cart.getUser().getUserId());

        ShoppingCartResponseDto dto = new ShoppingCartResponseDto();
        dto.setCartId(cart.getCartId());
        dto.setUserId(user.getUserId());
        dto.setProduct(cart.getProduct());
        dto.setQuantity(cart.getQuantity());
        dto.setDateAdded(cart.getDateAdded());

        return dto;
    }
}
