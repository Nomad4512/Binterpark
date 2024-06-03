package com.binterpark.controller;

import com.binterpark.domain.ShoppingCart;
import com.binterpark.dto.ShoppingCartResponseDto;
import com.binterpark.service.ShoppingCartService;
import lombok.NoArgsConstructor;
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
    public ResponseEntity<String> addToCart(@RequestParam(value = "userId") Long userId,
                                            @RequestParam(value = "productId") Long productId,
                                            @RequestParam(value = "quantity") int quantity) {
        try {
            ShoppingCart cart = shoppingCartService.addToCart(userId,productId,quantity);
            return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("장바구니 담기 실패", e);
            return ResponseEntity.internalServerError().body("장바구니 추가 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCartItems(@PathVariable(name = "userId") Long userId) {
        try {
            List<ShoppingCartResponseDto> cartItems = shoppingCartService.getCartItems(userId);
            return ResponseEntity.ok(cartItems);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }catch (Exception e) {
            log.error("장바구니 목록 가져오기 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable(name = "cartId") Long cartId,
                                               @RequestParam(value = "productId") Long productId) {
        try {
            shoppingCartService.removeFromCart(cartId, productId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("장바구니 항목 제거 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }


}
