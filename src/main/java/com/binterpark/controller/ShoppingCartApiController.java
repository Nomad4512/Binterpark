package com.binterpark.controller;

import com.binterpark.dto.ShoppingCartResponseDto;
import com.binterpark.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartApiController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {

        shoppingCartService.addToCart(userId,productId,quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ShoppingCartResponseDto>> getCartItems(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingCartService.getCartItems(userId));
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartId, Long productId) {
        return ResponseEntity.ok().build();
    }
}
